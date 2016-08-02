/*
 * Copyright © 2016 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.cask.cdap.security.authorization;

import co.cask.cdap.common.runtime.RuntimeModule;
import co.cask.cdap.security.spi.authorization.AuthorizationEnforcer;
import co.cask.cdap.security.spi.authorization.Authorizer;
import co.cask.cdap.security.spi.authorization.PrivilegesFetcher;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

/**
 * A module that contains bindings for {@link AuthorizationEnforcementService} and {@link PrivilegesFetcher}.
 */
public class AuthorizationEnforcementModule extends RuntimeModule {
  public static final String PRIVILEGES_FETCHER_PROXY_CACHE = "privileges-fetcher-proxy-cache";
  public static final String PRIVILEGES_FETCHER_PROXY_CLIENT = "privileges-fetcher-proxy-client";

  @Override
  public Module getInMemoryModules() {
    return new AbstractModule() {
      @Override
      protected void configure() {
        // bind AuthorizationEnforcementService as a singleton. This binding is used while starting/stopping
        // the service itself.
        bind(AuthorizationEnforcementService.class).to(DefaultAuthorizationEnforcementService.class)
          .in(Scopes.SINGLETON);
        // bind AuthorizationEnforcer to AuthorizationEnforcementService
        bind(AuthorizationEnforcer.class).to(AuthorizationEnforcementService.class).in(Scopes.SINGLETON);
        bind(PrivilegesFetcher.class).toProvider(AuthorizerAsPrivilegesFetcherProvider.class).in(Scopes.SINGLETON);
      }
    };
  }

  @Override
  public Module getStandaloneModules() {
    return new AbstractModule() {
      @Override
      protected void configure() {
        // bind AuthorizationEnforcementService as a singleton. This binding is used while starting/stopping
        // the service itself.
        bind(AuthorizationEnforcementService.class).to(DefaultAuthorizationEnforcementService.class)
          .in(Scopes.SINGLETON);
        // bind AuthorizationEnforcer to AuthorizationEnforcementService
        bind(AuthorizationEnforcer.class).to(AuthorizationEnforcementService.class).in(Scopes.SINGLETON);

        bind(PrivilegesFetcherProxyService.class).to(DefaultPrivilegesFetcherProxyService.class)
          .in(Scopes.SINGLETON);
        bind(PrivilegesFetcher.class).to(RemotePrivilegesFetcher.class);
        bind(PrivilegesFetcher.class)
          .annotatedWith(Names.named(PRIVILEGES_FETCHER_PROXY_CACHE))
          .to(PrivilegesFetcherProxyService.class);
        bind(PrivilegesFetcher.class)
          .annotatedWith(Names.named(PRIVILEGES_FETCHER_PROXY_CLIENT))
          .to(PrivilegesFetcherProxyClient.class);
      }
    };
  }

  /**
   * Used by program containers and system services (viz explore service, stream service) that need to enforce
   * authorization in distributed mode. For fetching privileges, these components are expected to proxy via a proxy
   * service, which in turn uses the authorization enforcement modules defined by #getProxyModule
   */
  @Override
  public Module getDistributedModules() {
    return new AbstractModule() {
      @Override
      protected void configure() {
        // bind AuthorizationEnforcementService as a singleton. This binding is used while starting/stopping
        // the service itself.
        bind(AuthorizationEnforcementService.class).to(DefaultAuthorizationEnforcementService.class)
          .in(Scopes.SINGLETON);
        // bind AuthorizationEnforcer to AuthorizationEnforcementService
        bind(AuthorizationEnforcer.class).to(AuthorizationEnforcementService.class).in(Scopes.SINGLETON);
        bind(PrivilegesFetcher.class).to(RemotePrivilegesFetcher.class);
      }
    };
  }

  /**
   * Returns an {@link AbstractModule} containing bindings for authorization enforcement to be used in the Master.
   */
  public AbstractModule getMasterModule() {
    return new AbstractModule() {
      @Override
      protected void configure() {
        // bind AuthorizationEnforcementService as a singleton. This binding is used while starting/stopping
        // the service itself.
        bind(AuthorizationEnforcementService.class).to(DefaultAuthorizationEnforcementService.class)
          .in(Scopes.SINGLETON);
        // bind AuthorizationEnforcer to AuthorizationEnforcementService
        bind(AuthorizationEnforcer.class).to(AuthorizationEnforcementService.class).in(Scopes.SINGLETON);

        // Master should have access to authorization backends, so no need to fetch privileges remotely
        bind(PrivilegesFetcher.class).toProvider(AuthorizerAsPrivilegesFetcherProvider.class);

        // Master is expected to have (kerberos) credentials to communicate with authorization backends. Hence, it
        // doesn't need to start a proxy to fetch privileges from authorization backends, so no need to bind
        // PrivilegesFetcherProxyService
      }
    };
  }

  public AbstractModule getProxyModule() {
    return new AbstractModule() {
      @Override
      protected void configure() {
        // bind AuthorizationEnforcementService as a singleton. This binding is used while starting/stopping
        // the service itself.
        bind(AuthorizationEnforcementService.class).to(DefaultAuthorizationEnforcementService.class)
          .in(Scopes.SINGLETON);
        // bind AuthorizationEnforcer to AuthorizationEnforcementService
        bind(AuthorizationEnforcer.class).to(AuthorizationEnforcementService.class).in(Scopes.SINGLETON);

        // The RemoteSystemOperations service acts as a proxy to Master for fetching privileges from authorization
        // backends, since it does not have access to make requests to authorization backends.
        // e.g. Apache Sentry currently does not support proxy authentication or issue delegation tokens. As a result,
        // all requests to Sentry need to be proxied via Master, which is whitelisted.
        // Hence, bind PrivilegesFetcher to a proxy implementation, that makes a proxy call to master for fetching
        // privileges
        // bind PrivilegesFetcherProxyService as a singleton. This binding is used while starting/stopping
        // the service itself.
        bind(PrivilegesFetcherProxyService.class).to(DefaultPrivilegesFetcherProxyService.class)
          .in(Scopes.SINGLETON);
        // inside program containers, for enforcing privileges, bind PrivilegeFetcher to a remote implementation
        // that can make a call to a dedicated proxy service
        bind(PrivilegesFetcher.class).to(RemotePrivilegesFetcher.class);
        // bind PrivilegesFetcher to the PrivilegesFetcherProxyService, so privileges can be fetched from a cache
        // inside the proxy if caching is enabled.
        bind(PrivilegesFetcher.class)
          .annotatedWith(Names.named(PRIVILEGES_FETCHER_PROXY_CACHE))
          .to(PrivilegesFetcherProxyService.class);
        // The PrivilegesFetcherProxyService itself uses a PrivilegesFetcher that proxies requests to master to
        // refresh cached privileges periodically.
        bind(PrivilegesFetcher.class)
          .annotatedWith(Names.named(PRIVILEGES_FETCHER_PROXY_CLIENT))
          .to(PrivilegesFetcherProxyClient.class);
      }
    };
  }

  /**
   * Provides {@link Authorizer} as the binding for {@link PrivilegesFetcher}.
   */
  private static class AuthorizerAsPrivilegesFetcherProvider implements Provider<PrivilegesFetcher> {
    private final AuthorizerInstantiator authorizerInstantiator;

    @Inject
    private AuthorizerAsPrivilegesFetcherProvider(AuthorizerInstantiator authorizerInstantiator) {
      this.authorizerInstantiator = authorizerInstantiator;
    }

    @Override
    public PrivilegesFetcher get() {
      return authorizerInstantiator.get();
    }
  }
}
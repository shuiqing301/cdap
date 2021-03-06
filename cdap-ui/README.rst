==============
CDAP UI Pack 1
==============

The CDAP UI Pack is a UI-only release that can be applied on top of an existing CDAP installation.

Details
=======
- Release Date: 03/23/2017
- Base CDAP Version: 4.1
- Release branch: release/4.1

Installation
============
Currently, the following manual steps need to be performed to install the CDAP UI Pack.
These steps will be automated in a later CDAP release.

UNIX/Linux Flavors
------------------
**Standalone CDAP**
::

  $ cd <CDAP_HOME>
  $ ./bin/cdap sdk stop
  $ zip -m -r ui-backup.zip ui
  $ unzip /path/to/download/cdap-ui-pack.zip
  $ ./bin/cdap sdk start


**Distributed CDAP**

*Note:* In the instructions below, ``<CDAP_HOME>`` is either:

- ``/opt/cdap`` on RPM (manual or Apache Ambari) installations; or
- the **CDAP** sub-directory of the **Parcel Directory** on Cloudera Manager installations (e.g. ``/opt/cloudera/parcels/CDAP``)

::

  $ cd <CDAP_HOME>                         
  $ /etc/init.d/cdap-ui stop
  $ zip -m -r ui-backup.zip ui
  $ unzip /path/to/download/cdap-ui-pack-4.1.0_p1.zip
  $ /etc/init.d/cdap-ui start



Windows
-------
1. Using the command prompt, stop the CDAP SDK::

    > cd <CDAP_HOME>
    > bin\cdap sdk stop

2. Open the ``<CDAP_HOME>`` directory in Explorer
3. Compress the ``ui`` to save a backup, by right-clicking on the ``ui`` directory and
   choosing **Send To** -> **Compressed (zipped) folder**
4. Delete the ``ui`` directory after the backup is completed
5. Extract the UI pack (cdap-ui-pack-4.1.0_p1.zip) in the ``<CDAP_HOME>`` directory, by right-clicking on the file,
   choosing **Extract All**, and specifying the path to the ``<CDAP_HOME>`` directory
6. A new ``ui`` directory should be created
7. Using the command prompt, start the CDAP SDK::

    > cd <CDAP_HOME>
    > bin\cdap sdk start


Steps to Update Data Preparation Capability
===========================================
1. After installing the CDAP UI Pack and restarting CDAP, from within the CDAP UI go to the Cask Market
2. From the **Solutions** category, follow the steps for the **Data Preparation** solution
3. Go to *Data Preparation* by clicking on the CDAP menu and then choosing *Data Preparation*
4. If a newer version of the *Data Preparation* libraries has been installed, the UI will show an **Update** button
5. Click the *Update* button to update to the newer version of *Data Preparation*


Release Notes
=============

New Features
------------
* `HYDRATOR-163 <http://issues.cask.co/browse/HYDRATOR-163>`__ - Add Placeholders to input boxes in node configuration
* `WRANGLER-77 <http://issues.cask.co/browse/WRANGLER-77>`__ - Added a dropdown on each column to provide click-through experience for directives in Data Preparation
* `WRANGLER-49 <http://issues.cask.co/browse/WRANGLER-49>`__ - Added click-through experience for split column directive in Data Preparation
* `WRANGLER-54 <http://issues.cask.co/browse/WRANGLER-54>`__ - Added click-through experience for filling null or empty cells in Data Preparation

Improvements
------------
* `CDAP-8501 <http://issues.cask.co/browse/CDAP-8501>`__ - Disabled preview button on clusters since preview is not supported in distributed env
* `CDAP-8861 <http://issues.cask.co/browse/CDAP-8861>`__ - Removed CDAP Version Range in market entities display
* `CDAP-8430 <http://issues.cask.co/browse/CDAP-8430>`__ - Improved "No Entities Found" message in the Overview to show Call(s) to Action
* `CDAP-8403 <http://issues.cask.co/browse/CDAP-8403>`__ - Added labels to CDAP Studio actions
* `CDAP-8900 <http://issues.cask.co/browse/CDAP-8900>`__ - Added the ability to update to a newer version of data preparation libraries if available
* `CDAP-7352 <http://issues.cask.co/browse/CDAP-7352>`__ - Made logviewer header row sticky
* `CDAP-4798 <http://issues.cask.co/browse/CDAP-4798>`__ - Improved user experience in explore page
* `CDAP-8964 <http://issues.cask.co/browse/CDAP-8964>`__ - Made Output Schema for sinks macro enabled
* `HYDRATOR-1364 <http://issues.cask.co/browse/HYDRATOR-1364>`__ - Removed most of the "__ui__" field
* `CDAP-8494 <http://issues.cask.co/browse/CDAP-8494>`__ - Fixed browser back button after switching to classic UI
* `CDAP-8828 <http://issues.cask.co/browse/CDAP-8828>`__ - Removed dialog to select pipeline type upon pipeline creation
* `CDAP-8396 <http://issues.cask.co/browse/CDAP-8396>`__ - Added call to action for namespace creation

Bugs
----
* `CDAP-8554 <http://issues.cask.co/browse/CDAP-8554>`__ - Fixed styling issues while showing Call(s) to actions in Application create wizard
* `CDAP-8412 <http://issues.cask.co/browse/CDAP-8412>`__ - Fixed overflow in namespace creation confirmation modal
* `CDAP-8433 <http://issues.cask.co/browse/CDAP-8433>`__ - Added units for memory for YARN stats on management page
* `CDAP-8950 <http://issues.cask.co/browse/CDAP-8950>`__ - Fixed link from stream overview to stream deatils
* `CDAP-8933 <http://issues.cask.co/browse/CDAP-8933>`__ - Added namespace name to the No entities found message
* `CDAP-8461 <http://issues.cask.co/browse/CDAP-8461>`__ - Clicking back from the Detail page view now opens the overview page with the overview pane opened
* `CDAP-8638 <http://issues.cask.co/browse/CDAP-8638>`__ - Opened each log in a new tab
* `CDAP-8668 <http://issues.cask.co/browse/CDAP-8668>`__ - Fixed UI to show ERROR, WARN and INFO logs by default
* `CDAP-8965 <http://issues.cask.co/browse/CDAP-8965>`__ - Removed Wrangle button from Wrangler Transform. Please use the Data Preparation UI for wrangling.
* `HYDRATOR-1419 <http://issues.cask.co/browse/HYDRATOR-1419>`__ - Fixed browser back button behavior after switching namespace


======================
License and Trademarks
======================

Copyright © 2017 Cask Data, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
either express or implied. See the License for the specific language governing permissions
and limitations under the License.

Cask is a trademark of Cask Data, Inc. All rights reserved.

Apache, Apache HBase, and HBase are trademarks of The Apache Software Foundation. Used with
permission. No endorsement by The Apache Software Foundation is implied by the use of these marks.

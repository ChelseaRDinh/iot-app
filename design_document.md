# IOT System

# Description:

A device management system that has a basic user interface, along with an administrative interface for system notifications, user management, and device registration/de-registration.

# System Specifications:
- You need to be running on at least 720p monitor.
- System DPI must be set to 100. We did not test on other DPIs.

# Usage:
To build the code, use **gradle build**.

To run our code as a stand-alone application, use the command **gradle run**. 

To access a basic user interface interface, use **user** as the username, and **user** as the password. Once logged in, you should receive one single window showing the IOT system home dashboard.

To access an admin interface, use **admin** as the username, and **admin** as the password. Once logged in, you should receive two windows: one window showing the IOT system home dashboard, and one window showing the admin console.

To run our unit tests, use the command **gradle test**. Check Travis for the status of our NonGUITests or run those tests manually.

Acceptance tests are marked in the test files. Sometimes testfx doesn't run the tests properly if you move your mouse once running the tests, or if your terminal is in front of the window. Sometimes it messes up running a bunch of tests back to back. Run failing tests again or maybe test in the app itself.

Some of our unit tests are incomplete, as in they only have acceptance checks, due to time constraints.

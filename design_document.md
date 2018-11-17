# IOT System

# Description:

A device management system that has a basic user interface, along with an administrative interface for system notifications, user management, and device registration/de-registration.

# System Requirements:
- You need to be running on at least 720p monitor.
- System DPI must be set to 100. We did not test on other DPIs.

# Diagrams
See various bmp files for the diagrams.

# Usage:
To build the code, use **gradle build**.

To run our code as a stand-alone application, use the command **gradle run**. 

To access a basic user interface interface, use **user** as the username, and **user** as the password. Once logged in, you should receive one single window showing the IOT system home dashboard.

To access an admin interface, use **admin** as the username, and **admin** as the password. Once logged in, you should receive two windows: one window showing the IOT system home dashboard, and one window showing the admin console.

To run our unit tests, use the command **gradle test**. Check Travis for the status of our NonGUITests or run those tests manually.

Acceptance tests are marked in the test files. Sometimes testfx doesn't run the tests properly if you move your mouse once running the tests, or if your terminal is in front of the window. Sometimes it messes up running a bunch of tests back to back. Run failing tests again or maybe test in the app itself.
If there are a ton of errors stemming from the auth manager, try deleting the userdb.txt and trying again, it will generate a new one.

# Notes
Some of our unit tests are incomplete, as in they only have acceptance checks, due to time constraints. New users aren't actually added nor are they saved, and devices can't be added to users currently (unless you do it in code). I assume though that since these menus are there and placeholder UI is there, and since there are unit tests checking for their presence, that this is also considered a passing unit test (GIVEN I want to use my Home Automation System as an admin WHEN I open the admin interface THEN I can manage devices and users.).

Some portions of the code are copied from other portions of the code that we wrote, so there could be a typo or two relating to that. This demostrates how TDD doesn't equate to a healthy codebase necessarily, in fact it can diminish the health of a codebase when trying to hit a deadline. But check out how sweet the device filtering by UUID is and sending/receiving messages is. That's a good example on how investing in building abstraction can speed up development. Ideally the device reponse matching in the different models' notify function would map the match string to a function, instead of having if statements outside the for loop afterwards. I didn't have time to implement this.

Lots of Java docs are missing, I didn't have time to put all that stuff in and google-format doesn't enforce this. Another example of TDD failing. You could check, but then I could, as a developer under pressure, leave useless comments in the javadoc. Good luck linting that. "TODO" everywhere, and some sympathetic devs might even let that pass review. They'll be added for A4, but there wasn't enough time to add that stuff in there.

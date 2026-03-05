This QA Test Automation Data-driven framework is consisting of below components(folder structure):

0. pom.xml: This is a Maven POM file located at the project-root level. All the project dependencies, build configurations, user profiles, etc are in it.

1. testng.xml: This is a TestNG xml file located at the project-root level. All the Test Suite configurations(test suites, groupings, and parallel execution, etc) are in it, which are to be run by Maven Test goal (i. e. mvn clean test).

2. test_data: You can put test data in excel files here. You can also specify run_flag (Y/N) to run only specific test data rows in the excel files.

3. test_results: Test Results are generated with timestamp for each run. These are extent reports with screenshots.

4. test_config: A global config file resides here, where you can set the test environment(QA, UAT, etc), application URL, test user details, run mode(GUI/Headless), hub URL for Selenium Grid, global variables, global timeout settings, etc.

5. test_cases: You can put your test cases in excel sheets here. These testcases are automated using Selenium WebDriver with Java in the project's src\test\java\com\qatest\tests folder.


How to run the automated test cases:
-From CLI, run "mvn clean test" command to invoke the automated test execution. You can also specify the TestNG testsuite XML filepath(comma-seperated) with "-Dsurefire.suiteXmlFiles=<filepath>" argument for "mvn clean test" command. If not specified, the TestNG testsuite XML file "testng.xml"(located at the project-root level) is used for "mvn clean test" command.

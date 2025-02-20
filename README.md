# Gherkin Steps for Selenium

Defines standard Gherkin steps for controlling a web browser through Selenium.

## Contributors

- Abdullah Alshammari ([a.alshammari.2@research.gla.ac.uk](mailto:a.alshammari.2@research.gla.ac.uk))
- Tim Storer ([timothy.storer@glasgow.ac.io](mailto:timothy.storer@glasgow.ac.uk))

## Getting Started

1. Clone this repository and the project to your local maven repository by running the command:

	`%> mvn install`
	
2. Add the following dependency to the pom file of your maven project:

```xml
  <dependency>
    <groupId>io.github.wadsg</groupId>
    <artifactId>wads-gherkin</artifactId>
    <version>1.3.1</version>
  </dependency>
```

3. Create a test runner class somewhere below `src/test` and annotate the class with the step suites to be included from wads-gherkin.  An example test runner is available in the test suite for wads-gherkin:

```java
package io.github.wadsg.selenium_bdd.test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
  features="src/test/resources/features",
  glue = {"io/ac/glasgow/selenium_bdd/steps/common","io/ac/glasgow/selenium_bdd/steps/html5"},
  plugin = {"json:target/cucumber.json","html:target/site/cucumber-pretty"})
public class TestRunner {}
```

The following suites are currently provided: 
 - Common `io/ac/glasgow/selenium_bdd/steps/common`
 - React `io/ac/glasgow/selenium_bdd/steps/react`
 - HTML5 `io/ac/glasgow/selenium_bdd/steps/html5`


4. Add feature files containing Gherkin features and scenarios containing one or more of the steps described below.

5. Run your test suite.

`mvn test`

## Supported Steps

### Given Steps (Preconditions)

#### Select a Browser

```gherkin
Given I am using the (Chrome|Firefox|Edge|IExplorer|Opera) web browser
```


Examples: 

```gherkin
Given I am using the "chrome" web browser
```

#### Open a Web Page

Optional: Configure your server name in config.properties. For example: server.name.myserver=https://courses.letskodeit.com.
This will allow you to only supply the page name instead of repeatedly typing the server name. 

```gherkin
Given I have opened the web page "page url"
```

Examples:

```gherkin
Given I have opened the web page "http://google.com"
```

```gherkin
Given I have opened the web page "myserver/signup"
```

#### Open a Web Page in a Tab

```gherkin
Given I have opened the webpage "page url" in tab "tab number"
```

Examples:

```gherkin
Given I have opened the webpage "http://google.com" in tab "2"
```

### When Steps (Actions)

#### Switching Tabs

```gherkin
When I switch to the tab titled "page title"
```

```gherkin
When I switch to tab "tab number"
```

Examples:

```gherkin
When I switch to tab "3"
```

```gherkin
When I switch to the tab titled "University of Glasgow"
```

#### Enter a value in a text field

```gherkin
When I enter the value "some value" into the text box (that is both nearest the label "some label" and below the label "some other label")|((below|above|nearest|with) the label "some label") 
```

Examples:

```gherkin
When I enter the value "gherkin" into the text box nearest the label "username"
```

```gherkin
When I enter the value "gherkin" into the text box that is both nearest the label "username" and below the label "second user"
```

```gherkin
When I enter the value "gherkin" into the text box below the label "username"
```

```gherkin
When I enter the value "gherkin" into the text box above the label "username"
```

```gherkin
When I enter the value "gherkin" into the text box with the label "username"
```

#### Enter a Value in a Password Field

```gherkin
When I enter the value "some value" into the password box (nearest|with) the label "some label"
```

Examples:

```gherkin
When I enter the value "cucumber" into the password box nearest the label "password"
```

```gherkin
When I enter the value "cucumber" into the password box with the label "password"
```

#### Enter a Value in a Number Box

```gherkin
When I enter the value "some value" into the number box (nearest|with) the label "some label"
```

Examples:

```gherkin
When I enter the value "31" into the number box nearest the label "age"
```

```gherkin
When I enter the value "31" into the number box with the label "age"
```

#### Choose a Date from a Picker

```gherkin
When I enter the date "mmddyyyy" into the date picker (nearest|with) the label "some label"
```

Examples:

```gherkin
When I enter the date "05192022" into the date picker nearest the label
"Hire date"
```

```gherkin
When I enter the date "05192022" into the date picker with the label
"Hire date"
```

#### Click a Button

```gherkin
When I click the (confirmation)? button labelled "some label"
```

```gherkin
When I click the "some label" button
```

```gherkin
When I click the button (nearest|above|below) the label "some label"
```

Examples:

```gherkin
When I click the button labelled "Sign up"
```

```gherkin
When I click the "Sign up" button
```

```gherkin
When I click the button nearest the label "password"
```

```gherkin
When I click the button above the label "username"
```

```gherkin
When I click the button below the label "username"
```

```gherkin
When I click the confirmation button labelled "Ok"
```

#### Click a Link

```gherkin
When I click the link labelled "some label"
```

Examples:

```gherkin
When I click the link labelled "HOME"
```

#### File Uploads

```gherkin
When I upload the file with the relative path "some path" by clicking the button labelled "some label"
```

```gherkin
When I upload the file "some path" by clicking the "some label" button
```

Examples:

```gherkin
When I upload the file with the relative path "src/test/resources/reagent.csv" by clicking the button labelled "upload file" 
```

```gherkin
When I upload the file "src/test/resources/reagent.csv" by clicking the "upload file" button
```

#### Check Boxes

```gherkin
When I (click|unselect) the check box button (nearest|with) the label "some label"
```

Examples:

```gherkin
When I click the check box button nearest the label "remember me"
```

```gherkin
When I click the check box button with the label "remember me"
```

```gherkin
When I unselect the check box button nearest the label "remember me"
```

```gherkin
When I unselect the check box button with the label "remember me"
```

#### Multiple Selection List


```gherkin
When I (un)?select the (value "some value")|(values "some value" and "some other value") from the multiple select list (nearest|with) the label "some label"
```

Examples:

```gherkin
When I select the value "apple" from the multiple select list nearest the label "Your favorite fruit?"
```

```gherkin
When I select the value "apple" from the multiple select list with the label "Your favorite fruit?"
```

```gherkin
When I unselect the value "apple" from the multiple select list nearest the label "Your favorite fruit?"
```

```gherkin
When I unselect the value "apple" from the multiple select list with the label "Your favorite fruit?"
```

```gherkin
When I select the values "apple" and "orange" from the multiple select list nearest the label "Your favorite fruits?"
```

```gherkin
When I select the values "apple" and "orange" from the multiple select list with the label "Your favorite fruits?"
```

```gherkin
When I unselect the values "apple" and "orange" from the multiple select list nearest the label "Your favorite fruits?"
```

```gherkin
When I unselect the values "apple" and "orange" from the multiple select list with the label "Your favorite fruits?"
```

#### Radio Buttons

```gherkin
When I click the radio button (nearest|with) the label "some label"
```

Examples:

```gherkin
When I click the radio button nearest the label "married"
```

```gherkin
When I click the radio button with the label "married"
```

#### Drop Down Lists

```gherkin
When I select the value "some value" from the drop down list (nearest|below|with) the label "some label"
```

Examples:

```gherkin
When I select the value "apple" from the drop down list nearest the label "Your favorite fruit?"
```

```gherkin
When I select the value "apple" from the drop down list below the label "Your favorite fruit?"
```

```gherkin
When I select the value "apple" from the drop down list with the label "Your favorite fruit?"
```

#### Time Delay

```gherkin
When I wait for "time in seconds" seconds
```

Examples:

```gherkin
When I wait for "10" seconds
```


### Then Steps (Assertions)

#### Navigated to a Page

```gherkin
Then I should be taken to the page "some page"
```

Examples:

```gherkin
Then I should be taken to the page "http://gmail.com/loggedin"
```

#### Check Box Selected

```gherkin
Then the check box button (nearest|with) the label "some label" is (un)?selected
```

Examples:

```gherkin
Then the check box button nearest the label "remember me" is selected
```

```gherkin
Then the check box button with the label "remember me" is selected
```

```gherkin
Then the check box button nearest the label "remember me" is unselected
```

```gherkin
Then the check box button with the label "remember me" is unselected
```

#### Multiple Select List Item Selection

```gherkin
Then the (value "some value")|(values "some value" and "some other value") in the multiple select list (nearest|with) the label "some label" is (un)?selected
```

Examples:

```gherkin
Then the value "apple" in the multiple select list nearest the label "Your favorite fruit?" is selected
```

```gherkin
Then the value "apple" in the multiple select list with the label "Your favorite fruit?" is selected
```

```gherkin
 Then the value "apple" in the multiple select list nearest the label "your favorite fruit?" is unselected
```

```gherkin
 Then the value "apple" in the multiple select list with the label "your favorite fruit?" is unselected
```

```gherkin
Then the values "apple" and "orange" in the multiple select list nearest the label "your favorite fruits?" are selected
```

```gherkin
Then the values "apple" and "orange" in the multiple select list with the label "your favorite fruits?" are selected
```

```gherkin
Then the values "apple" and "orange" in the multiple select list nearest the label "your favorite fruits?" are unselected
```

```gherkin
Then the values "apple" and "orange" in the multiple select list with the label "your favorite fruits?" are unselected
```

#### Radio Button Selection

```gherkin
Then the radio button (nearest|with) the label "some label" is selected
```

Examples:

```gherkin
Then the radio button nearest the label "married" is selected
```

```gherkin
Then the radio button with the label "married" is selected
```

#### Drop Down List Item Selection

```gherkin
Then the value "some value" in the drop down list (nearest|with) the label "some label" is selected
```

Examples:

```gherkin
Then the value "apple" in the drop down list nearest the label "Your favorite fruit?" is selected
```

```gherkin
Then the value "apple" in the drop down list with the label "Your favorite fruit?" is selected
```

#### Content Appears on a Page

```gherkin
Then content appears on the page including the word "some phrase"
```

```gherkin
Then I should see the message "some message"
```

Examples:

```gherkin
Then content appears on the page including the word "page updated!"
```

```gherkin
Then I should see the message "message sent successfully!"
```

#### File Download

```gherkin
Then the file "filename.extension" is downloaded
```

Examples:

```gherkin
Then the file "Samples_Report.csv" is downloaded
```

Note: Configue your system's file download directory in config.properties.
For example, download.directory=C:\\Users\\User\\Downloads

## Known Issues

Loading the browser driver in Background sections can cause a concurrency
exception. 
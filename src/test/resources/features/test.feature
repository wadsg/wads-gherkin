Feature: Testing

  Scenario: Text box
    Given I am using the "Chrome" web browser
    And I have opened the web page "./src/test/resources/html/test.html"
    When I enter the value "cucumber" into the text box nearest the label "Username"
    When I enter the value "gherkin" into the password box nearest the label "Password"
    And I click the button labelled "Sign in"
    Then I should be taken to the page "https://www.w3schools.com/?username=cucumber&password=gherkin"

  Scenario: Check box
    Given I am using the "Chrome" web browser
    And I have opened the web page "./src/test/resources/html/test.html"
    When I click the check box button nearest the label "Honda"
    Then the check box button nearest the label "Honda" is selected

  Scenario: Check box - unselect a value
    Given I am using the "Chrome" web browser
    And I have opened the web page "./src/test/resources/html/test.html"
    When I click the check box button nearest the label "Honda"
    And I unselect the check box button nearest the label "Honda"
    Then the check box button nearest the label "Honda" is unselected

  Scenario: Radio button
    Given I am using the "Chrome" web browser
    And I have opened the web page "./src/test/resources/html/test.html"
    When I click the radio button nearest the label "Ruby"
    Then the radio button nearest the label "Ruby" is selected

  Scenario: Drop down list
    Given I am using the "Chrome" web browser
    And I have opened the web page "./src/test/resources/html/test.html"
    When I select the value "Honda" from the drop down list nearest the label "Drop down list"
    Then the value "Honda" from the drop down list nearest the label "Drop down list" is selected

  Scenario: Multiple select - single value
    Given I am using the "Chrome" web browser
    And I have opened the web page "./src/test/resources/html/test.html"
    When I select the value "Apple" from the multiple select list nearest the label "Multiple select list"
    Then the value "Apple" in the multiple select list nearest the label "Multiple select list" is selected

  Scenario: Multiple select - unselect single value
    Given I am using the "Chrome" web browser
    And I have opened the web page "./src/test/resources/html/test.html"
    When I select the value "Apple" from the multiple select list nearest the label "Multiple select list"
    And I unselect the value "Apple" from the multiple select list nearest the label "Multiple select list"
    Then the value "Apple" in the multiple select list nearest the label "Multiple select list" is unselected

  Scenario: Multiple select - multiple values
    Given I am using the "Chrome" web browser
    And I have opened the web page "./src/test/resources/html/test.html"
    When I select the values "Apple" and "Orange" from the multiple select list nearest the label "Multiple select list"
    Then the values "Apple" and "Orange" in the multiple select list nearest the label "Multiple select list" are selected

  Scenario: Multiple select - unselect multiple values
    Given I am using the "Chrome" web browser
    And I have opened the web page "./src/test/resources/html/test.html"
    When I select the values "Apple" and "Peach" from the multiple select list nearest the label "Multiple select list"
    And I unselect the values "Apple" and "Peach" from the multiple select list nearest the label "Multiple select list"
    Then the values "Apple" and "Peach" in the multiple select list nearest the label "Multiple select list" are unselected

  Scenario: Button button
    Given I am using the "Chrome" web browser
    And I have opened the web page "./src/test/resources/html/test.html"
    And I click the button labelled "Open Window"

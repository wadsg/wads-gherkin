Feature: Login

  Scenario: Login scenario
   Given I am using the "Chrome" web browser
   And I have opened the web page "http://tutorialsninja.com/demo/index.php?route=account/login"
   When I enter the value "gherkin@test.com" into the text box nearest the label "E-Mail Address"
   And I enter the value "gherkin" into the password box nearest the label "Password"
   And I click the button labelled "Login"
   Then I should be taken to the page "http://tutorialsninja.com/demo/index.php?route=account/account"

 Scenario: Switch tabs using tab numbers
  Given I am using the "Chrome" web browser
  And I have opened the web page "http://google.com"
  And I have opened the webpage "http://google.com" in tab "2"
  And I have opened the webpage "http://google.com" in tab "3"
  When I switch to tab "2"

 Scenario: Switch tabs using tab titles
  Given I am using the "Chrome" web browser
  And I have opened the web page "http://google.com"
  And I have opened the webpage "http://gla.ac.uk" in tab "2"
  And I have opened the webpage "http://selenium.dev" in tab "3"
  When I switch to the tab titled "Google"
  When I switch to the tab titled "University of Glasgow"

  Scenario: Click links
   Given I am using the "Chrome" web browser
   And I have opened the web page "https://courses.letskodeit.com/practice"
   When I click the link labelled "HOME"
   Then I should be taken to the page "https://courses.letskodeit.com/"

  Scenario: Connect to database
   Given I am using the "Chrome" web browser
   And I have opened the web page "https://www.google.com/"
   And the following "Student" items stored in the web application
    | Surname  | Forename| Matriculation |
    | "Storer" | "Tim"   | "1234567s"    |

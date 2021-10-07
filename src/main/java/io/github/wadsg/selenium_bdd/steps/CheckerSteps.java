package io.github.wadsg.selenium_bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CheckerSteps {

    @Given("^I am using the \"(.*)\" web browser$")
    public void i_am_using_the_web_browser(String browser) {
        System.out.println("I am using the web browser" + browser);
    }
    @Given("^I have opened the web page \"(.*)\"$")
    public void i_have_opened_the_web_page(String urlOrPath) {
        System.out.println("I have opened the web page "+ urlOrPath);
    }

    @Given("^I have opened the webpage \"(.*)\" in tab \"(.*)\"$")
    public void i_have_opened_the_webpage_in_tab(String urlOrPath, String tab) {
        System.out.println("I have opened the webpage "+urlOrPath+" in tab "+tab);
    }

    @When("^I switch to tab \"(.*)\"$")
    public void i_switch_to_tab(String tab){
        System.out.println("I switch to tab "+tab);
    }

    @When("^I enter the value \"(.*)\" into the text box nearest the label \"(.*)\"$")
    public void i_enter_the_value_into_the_text_box_nearest_the_label(String value, String label) {
        System.out.println("I enter the value " + value +" into the text box nearest the label "+ label);
    }

    @When("^I enter the value \"(.*)\" into the password box nearest the label \"(.*)\"$")
    public void i_enter_the_value_into_the_password_box_nearest_the_label(String value, String label) {
        System.out.println("I enter the value " + value +" into the password box nearest the label "+ label);
    }

    @When("^I enter the value \"(.*)\" into the number box nearest the label \"(.*)\"$")
    public void i_enter_the_value_into_the_number_box_nearest_the_label(String value, String label) {
        System.out.println("I enter the value " + value +" into the number box nearest the label "+ label);
    }

    @When("^I enter the value \"(.*)\" into the text box below the label \"(.*)\"$")
    public void i_enter_the_value_into_the_text_box_below_the_label(String value, String label){
        System.out.println("I enter the value " + value +" into the text box below the label "+ label);
    }

    @When("^I enter the date \"(.*)\" into the date picker nearest the label \"(.*)\"$")
    public void i_enter_the_date_into_the_date_picker_nearest_the_label(String value, String label) {
        System.out.println("I enter the date " + value +" into the date picker nearest the label "+ label);
    }

    @When("^I click the button labelled \"(.*)\"$")
    public void i_click_the_button_labelled(String label) throws InterruptedException {
        System.out.println("I click the button labelled "+label);
    }

    @When("^I click the button nearest the label \"(.*)\"$")
    public void i_click_the_button_nearest_the_label(String label) {
        System.out.println("I click the button nearest the label "+label);
    }

    @When("^I click the button above the label \"(.*)\"$")
    public void i_click_the_button_above_the_label(String label) {
        System.out.println("I click the button above the label "+label);
    }

    @When("^I click the confirmation button labelled \"(.*)\"$")
    public void i_click_the_confirmation_button_labelled(String label){
        System.out.println("I click the confirmation button labelled "+label);
    }

    @When("^I upload the file with the relative path \"(.*)\" by clicking the button labelled \"(.*)\"$")
    public void i_upload_the_file_with_the_relative_path_by_clicking_a_button_labelled(String path, String label) {
        System.out.println("I upload the file with the relative path " + path + " by clicking the button labelled "+label);
    }

    @When("^I click the check box button nearest the label \"(.*)\"$")
    public void i_click_the_check_box_button_nearest_the_label(String label) {
        System.out.println("I click the check box button nearest the label "+label);
    }

    @When("^I unselect the check box button nearest the label \"(.*)\"$")
    public void i_unselect_the_check_box_button_nearest_the_label(String label) {
        System.out.println("I unselect the check box button nearest the label "+label);
    }

    @When("^I select the value \"(.*)\" from the multiple select list nearest the label \"(.*)\"$")
    public void i_select_the_value_from_the_multiple_select_list_nearest_the_label(String value, String label) {
        System.out.println("I select the value " + value + " from the multiple select list nearest the label "+label);
    }

    @When("^I unselect the value \"(.*)\" from the multiple select list nearest the label \"(.*)\"$")
    public void i_unselect_the_value_from_the_multiple_select_list_nearest_the_label(String value, String label) {
        System.out.println("I unselect the value " + value + " from the multiple select list nearest the label "+label);

    }

    @When("^I select the values \"(.*)\" and \"(.*)\" from the multiple select list nearest the label \"(.*)\"$")
    public void i_select_the_values_from_the_multiple_select_list_nearest_the_label(String value1, String value2, String label) {
        System.out.println("I select the values " + value1 + " and " + value2 +" from the multiple select list nearest the label "+label);
    }

    @When("^I unselect the values \"(.*)\" and \"(.*)\" from the multiple select list nearest the label \"(.*)\"$")
    public void i_unselect_the_values_from_the_multiple_select_list_nearest_the_label(String value1, String value2, String label) {
        System.out.println("I unselect the values " + value1 + " and " + value2 +" from the multiple select list nearest the label "+label);
    }

    @When("^I click the radio button nearest the label \"(.*)\"$")
    public void i_click_the_radio_button_nearest_the_label(String label) {
        System.out.println("I click the radio button nearest the label "+label);
    }

    @When("^I select the value \"(.*)\" from the drop down list nearest the label \"(.*)\"$")
    public void i_select_the_value_from_the_drop_down_list_nearest_the_label(String value, String label) {
        System.out.println("I select the value " + value + " from the drop down list nearest the label " +label);
    }

    @When("^I select the value \"(.*)\" from the drop down list below the label \\\"(.*)\"$")
    public void i_select_the_value_from_the_drop_down_list_below_the_label(String value, String label) {
        System.out.println("I select the value " + value + " from the drop down list below the label " +label);
    }

    @Then("^I should be taken to the page \"(.*)\"$")
    public void i_should_be_taken_to_the_page(String url) {
        System.out.println("I should be taken to the page "+url);
    }

    @Then("^the check box button nearest the label \"(.*)\" is selected")
    public void the_check_box_button_nearest_the_label_is_selected(String label) {
        System.out.println("the check box button nearest the label "+label+ " is selected");
    }

    @Then("^the check box button nearest the label \"(.*)\" is unselected")
    public void the_check_box_button_nearest_the_label_is_unselected(String label) {
        System.out.println("the check box button nearest the label "+label+ " is unselected");
    }


    @Then("^the value \"(.*)\" in the multiple select list nearest the label \"(.*)\" is selected")
    public void the_value_in_the_multiple_select_list_nearest_the_label_is_selected(String value, String label) {
        System.out.println("the value "+ value + " in the multiple select list nearest the label" + label + " is selected");
    }

    @Then("^the value \"(.*)\" in the multiple select list nearest the label \"(.*)\" is unselected")
    public void the_value_in_the_multiple_select_list_nearest_the_label_is_unselected(String value, String label) {
        System.out.println("the value "+ value + " in the multiple select list nearest the label" + label + " is unselected");
    }

    @Then("^the values \"(.*)\" and \"(.*)\" in the multiple select list nearest the label \"(.*)\" are selected")
    public void the_values_in_the_multiple_select_list_nearest_the_label_are_selected(String value1, String value2, String label) {
        System.out.println("the values "+ value1 +"and " + value2 +" in the multiple select list nearest the label " + label + "are selected");
    }

    @Then("^the values \"(.*)\" and \"(.*)\" in the multiple select list nearest the label \"(.*)\" are unselected")
    public void the_values_in_the_multiple_select_list_nearest_the_label_are_unselected(String value1, String value2, String label) {
        System.out.println("the values "+ value1 +"and " + value2 +" in the multiple select list nearest the label " + label + "are unselected");
    }

    @Then("I should see the message \"(.*)\"$")
    public void I_should_see_the_message(String status) throws InterruptedException {
        System.out.println("I should see the message "+status);
    }

    @Then("content appears on the page including the word \"(.*)\"$")
    public void content_appears_on_the_page_including_the_word(String word) throws InterruptedException {
        System.out.println("content appears on the page including the word "+word);
     }

    @Then("the file \"(.*)\" should be downloaded in the location \"(.*)\"$")
    public void the_file_should_be_downloaded(String fileName, String location) throws InterruptedException {
        System.out.println("the file "+ fileName +" should be downloaded in the location "+location);
    }

    @Then("^the value \"(.*)\" from the drop down list nearest the label \"(.*)\" is selected")
    public void the_value_from_the_drop_down_list_nearest_the_label_is_selected(String value, String label) {
        System.out.println("the value "+value+" from the drop down list nearest the label "+label+" is selected");
    }

    @Then("^the radio button nearest the label \"(.*)\" is selected")
    public void the_radio_button_nearest_the_label_is_selected(String label) {
        System.out.println("the radio button nearest the label "+label+" is selected");
    }
}

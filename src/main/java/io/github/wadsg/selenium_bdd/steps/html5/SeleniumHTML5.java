package io.github.wadsg.selenium_bdd.steps.html5;

import io.github.wadsg.selenium_bdd.steps.Utility;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import static io.github.wadsg.selenium_bdd.steps.Fixture.driver;
import static org.openqa.selenium.support.locators.RelativeLocator.withTagName;

public class SeleniumHTML5 {

    @When("^I click the radio button nearest the label \"(.*)\"$")
    public void i_click_the_radio_button_nearest_the_label(String label) {
            Utility.getInputWebElementNearest(label, "radio").click();
    }

    @When("^I click the radio button below the label \"(.*)\"$")
    public void i_click_the_radio_button_below_the_label(String label) {

        WebElement inputElement = null;

            try {
                inputElement = driver.findElement(By.xpath("//p[contains(.,'" + label + "')]"));
            }catch(NoSuchElementException noSuchElementException){

            }

        if(inputElement != null) {

            try {
                driver.findElement(withTagName("input").below(inputElement)).click();
            } catch (NoSuchElementException noSuchElementException) {

            }
        }

        if(inputElement != null) {

            try {
                driver.findElement(withTagName("button").below(inputElement)).click();
            } catch (NoSuchElementException noSuchElementException) {

            }
        }
    }

    @When("^I select the value \"(.*)\" from the drop down list nearest the label \"(.*)\"$")
    public void i_select_the_value_from_the_drop_down_list_nearest_the_label(String value, String label) {
        Utility.getSelectNearest(label).selectByVisibleText(value);
    }

    @When("^I select the value \"(.*)\" from the drop down list below the label \\\"(.*)\"$")
    public void i_select_the_value_from_the_drop_down_list_below_the_label(String value, String label) {
            WebElement labelElement = driver.findElement(By.xpath("//label[contains(.,'" + label+ "')]"));
            Select select = new Select(driver.findElement(withTagName("select").below(labelElement)));
            select.selectByVisibleText(value);
    }

    @When("^I select the value \"(.*)\" from the drop down list with label \"(.*)\"$")
    public void i_select_the_value_from_the_drop_down_list_with_label(String value, String label) {
        Utility.getSelectNearest(label).selectByVisibleText(value);
    }

    @Then("^the value \"(.*)\" from the drop down list nearest the label \"(.*)\" is selected")
    public void the_value_from_the_drop_down_list_nearest_the_label_is_selected(String value, String label) {
        Assert.assertEquals(value, Utility.getSelectNearest(label).getFirstSelectedOption().getText());
    }

    @Then("^the value \"(.*)\" from the drop down list with label \"(.*)\" is selected")
    public void the_value_from_the_drop_down_list_with_label_is_selected(String value, String label) {
        Assert.assertEquals(value, Utility.getSelectNearest(label).getFirstSelectedOption().getText());
    }

    @Then("^the radio button nearest the label \"(.*)\" is selected")
    public void the_radio_button_nearest_the_label_is_selected(String label) {
            Assert.assertTrue(Utility.getInputWebElementNearest(label, "radio").isSelected());
    }

    @Then("^the radio button with label \"(.*)\" is selected")
    public void the_radio_button_with_label_is_selected(String label) {
        Assert.assertTrue(Utility.getInputWebElementNearest(label, "radio").isSelected());
    }

}
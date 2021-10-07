package io.github.wadsg.selenium_bdd.steps.react;

import io.github.wadsg.selenium_bdd.steps.Utility;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.*;
import java.util.List;
import java.util.stream.Collectors;
import static org.openqa.selenium.support.locators.RelativeLocator.withTagName;
import static io.github.wadsg.selenium_bdd.steps.Fixture.driver;

public class SeleniumReact {

    @When("^I click the radio button nearest the label \"(.*)\"$")
    public void i_click_the_radio_button_nearest_the_label(String label) {

            WebElement element = null;
            try {
                element = getInputWebElementNearestLegend(label, "radio");
                element.click();
            }catch(NoSuchElementException noSuchElementException){

            }

            if(element==null) {
                try {
                    element = Utility.getInputWebElementNearest(label, "radio");
                    element.click();
                } catch (NoSuchElementException noSuchElementException) {

                }
            }
    }

    @When("^I click the radio button with label \"(.*)\"$")
    public void i_click_the_radio_button_with_label(String label) {

        WebElement element = null;
        try {
            element = getInputWebElementNearestLegend(label, "radio");
            element.click();
        }catch(NoSuchElementException noSuchElementException){

        }

        if(element==null) {
            try {
                element = Utility.getInputWebElementNearest(label, "radio");
                element.click();
            } catch (NoSuchElementException noSuchElementException) {

            }
        }
    }


    @When("^I select the value \"(.*)\" from the drop down list nearest the label \"(.*)\"$")
    public void i_select_the_value_from_the_drop_down_list_nearest_the_label(String value, String label) {
            Utility.getButtonNearestLabel(label).click();
            WebElement element = driver.findElement(By.linkText(value));
            element.click();
    }

    @When("^I select the value \"(.*)\" from the drop down list with label \"(.*)\"$")
    public void i_select_the_value_from_the_drop_down_list_with_label(String value, String label) {
        Utility.getButtonNearestLabel(label).click();
        WebElement element = driver.findElement(By.linkText(value));
        element.click();
    }

    @When("^I select the value \"(.*)\" from the drop down list below the label \\\"(.*)\"$")
    public void i_select_the_value_from_the_drop_down_list_below_the_label(String value, String label) {
        WebElement labelElement = null;
            try {
                labelElement = driver.findElement(By.xpath("//label[contains(.,'" + label + "')]"));
            }catch(NoSuchElementException noSuchElementException){

            }
            if(labelElement==null) {
                try {
                    labelElement = driver.findElement(By.xpath("//*[text()='" + label + "']"));
                } catch (NoSuchElementException noSuchElementException) {

                }
            }
            driver.findElement(withTagName("button").below(labelElement)).click();
            driver.findElement(By.linkText(value)).click();
        if(labelElement == null)
            throw new NoSuchElementException("");
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
            WebElement radioBtn = null;
            try{
                radioBtn = getInputWebElementNearestLegend(label, "radio");
            }catch(NoSuchElementException noSuchElementException){

            }
            if(radioBtn!=null)
                Assert.assertTrue(radioBtn.isSelected());
            else
                Assert.assertTrue(Utility.getInputWebElementNearest(label, "radio").isSelected());
    }

    @Then("^the radio button with label \"(.*)\" is selected")
    public void the_radio_button_with_label_is_selected(String label) {
        WebElement radioBtn = null;
        try{
            radioBtn = getInputWebElementNearestLegend(label, "radio");
        }catch(NoSuchElementException noSuchElementException){

        }
        if(radioBtn!=null)
            Assert.assertTrue(radioBtn.isSelected());
        else
            Assert.assertTrue(Utility.getInputWebElementNearest(label, "radio").isSelected());
    }

    private WebElement getInputWebElementNearestLegend(String legend, String type) {
        List<WebElement> legendElements = driver.findElements(By.xpath("//legend[contains(.,'" + legend + "')]"));

        WebElement legendElement = legendElements.get(0);

        List<WebElement> webElements =
                driver.findElements(withTagName("input").near(legendElement))
                        .stream()
                        .filter(e->e.getAttribute("type").equalsIgnoreCase(type))
                        .collect(Collectors.toList());

        return Utility.findNearestWebElementTo(legendElement, webElements);
    }

}
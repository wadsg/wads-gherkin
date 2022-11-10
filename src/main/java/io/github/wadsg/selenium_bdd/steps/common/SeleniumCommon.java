package io.github.wadsg.selenium_bdd.steps.common;
import io.github.wadsg.selenium_bdd.steps.Fixture;
import io.github.wadsg.selenium_bdd.steps.Utility;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.Select;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import static io.github.bonigarcia.wdm.DriverManagerType.*;
import static org.openqa.selenium.support.locators.RelativeLocator.byXPath;
import static org.openqa.selenium.support.locators.RelativeLocator.withTagName;

public class SeleniumCommon {

    @Given("^I am using the \"(.*)\" web browser$")
    public void i_am_using_the_web_browser(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager manager = WebDriverManager.getInstance(CHROME);
            ChromeOptions options = new ChromeOptions();
            manager.version("107");
            manager.timeout(10000).setup();
            options.setHeadless(false);
            Fixture.driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.getInstance(FIREFOX).setup();
            FirefoxOptions options = new FirefoxOptions();
            options.setHeadless(false);
            Fixture.driver = new FirefoxDriver(options);
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.getInstance(EDGE).setup();
            EdgeOptions options = new EdgeOptions();
            options.addArguments("headless");
            Fixture.driver = new EdgeDriver(options);
        } else if (browser.equalsIgnoreCase("iexplorer")) {
            WebDriverManager.getInstance(IEXPLORER).setup();
            Fixture.driver = new InternetExplorerDriver();
        } else if (browser.equalsIgnoreCase("opera")) {
            WebDriverManager.getInstance(OPERA).setup();
            Fixture.driver = new OperaDriver();
        } else {
            Assert.fail("Unknown browser selection.");
        }
    }

    @After
    public void after_scenario(){
        Fixture.driver.quit();
    }

    @Given("^I have opened the web page \"(.*)\"$")
    public void i_have_opened_the_web_page(String urlOrPath) {
        Path path = null;
        try {
            path = Paths.get(urlOrPath);
        } catch (InvalidPathException invalidPathException) {

        }

        if(path != null && urlOrPath.contains("myserver")){
            try {
                String extractedURL = extractURL(urlOrPath);
                URL url = new URL(extractedURL);
                Fixture.driver.get(url.toString());
            } catch (MalformedURLException malformedURLException) {
                Assert.fail("Invalid URL specification");
            }
            return;
        }

        if (path != null) {
            try{
                URL url = path.toUri().toURL();
                Fixture.driver.get(url.toString());
            } catch (MalformedURLException malformedURLException) {
                Assert.fail("URL could not be formed from valid path.");
            }
        } else {

            try {
                    URL url = new URL(urlOrPath);
                    Fixture.driver.get(url.toString());
            } catch (MalformedURLException malformedURLException) {
                    Assert.fail("Invalid URL specification");
            }

        }
    }

    @Given("^I have opened the webpage \"(.*)\" in tab \"(.*)\"$")
    public void i_have_opened_the_webpage_in_tab(String urlOrPath, String tab) {
        Path path = null;
        try {
            path = Paths.get(urlOrPath);
        } catch (InvalidPathException invalidPathException) {

        }

        if(path != null && urlOrPath.contains("myserver")){
            try {
                String extractedURL = extractURL(urlOrPath);
                URL url = new URL(extractedURL);
                Fixture.driver.switchTo().newWindow(WindowType.TAB);
                Fixture.driver.get(url.toString());
            } catch (MalformedURLException malformedURLException) {
                Assert.fail("Invalid URL specification");
            }
            return;
        }

        if (path != null) {
            try {
                URL url = path.toUri().toURL();
                Fixture.driver.switchTo().newWindow(WindowType.TAB);
                Fixture.driver.get(url.toString());
            } catch (MalformedURLException malformedURLException) {
                Assert.fail("URL could not be formed from valid path.");
            }
        } else {
            try {
                URL url = new URL(urlOrPath);
                Fixture.driver.switchTo().newWindow(WindowType.TAB);
                Fixture.driver.get(url.toString());
            } catch (MalformedURLException malformedURLException) {
                Assert.fail("Invalid URL specification");
            }
        }
    }

    @Given("^the following \"(.*)\" items stored in the web application$")
    public void the_following_items_stored_in_the_web_application(String tableName, DataTable table){
        List<List<String>> data = table.asLists();
        Properties property = loadConfigProperty();
        String dbUrl = property.getProperty("database.url");
        String dbUsername = property.getProperty("database.username");
        String dbPassword = property.getProperty("database.password");

        try(Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)){
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO "+tableName+" VALUES ("+data.get(1).get(0)+", "
                         +data.get(1).get(1)+", "+data.get(1).get(2)+")";
            statement.executeUpdate(sql);
            connection.close();
        } catch (SQLException e) {
        e.printStackTrace();
        }
    }

    @When("^I switch to tab \"(.*)\"$")
    public void i_switch_to_tab(String tab){
        int tabNumber = Integer.parseInt(tab);
        ArrayList<String> tabs = new ArrayList<String> (Fixture.driver.getWindowHandles());
        Fixture.driver.switchTo().window(tabs.get(tabNumber-1));
    }

    @When("^I switch to the tab titled \"(.*)\"$")
    public void i_switch_to_tab_titled(String pageTitle){
        String currentWindow = Fixture.driver.getWindowHandle();
        for(String windowHandle : Fixture.driver.getWindowHandles()){
            if (Fixture.driver.switchTo().window(windowHandle).getTitle().equals(pageTitle)) {
                Fixture.driver.switchTo().window(windowHandle);
                break;
            } else {
                Fixture.driver.switchTo().window(currentWindow);
            }
        }
    }

    @When("^I enter the value \"(.*)\" into the text box nearest the label \"(.*)\"$")
    public void i_enter_the_value_into_the_text_box_nearest_the_label(String value, String label) {
        Utility.getInputWebElementNearest(label, "text").sendKeys(value);
    }

    @When("^I enter the value \"(.*)\" into the text box below the label \"(.*)\"$")
    public void i_enter_the_value_into_the_text_box_below_the_label(String value, String label){

        WebElement labelElement = null;
        try {
            labelElement = Fixture.driver.findElement(By.xpath("//label[contains(.,'" + label + "')]"));
        }catch(NoSuchElementException noSuchElementException){

        }

        if(labelElement == null){
            try {
                labelElement = Fixture.driver.findElement(By.xpath("//h6[contains(.,'" + label + "')]"));
            }catch(NoSuchElementException noSuchElementException){

            }
        }

        if(labelElement != null)
            Fixture.driver.findElement(withTagName("input").below(labelElement)).sendKeys(value);
        else
            throw new NoSuchElementException("");
    }

    @When("^I enter the value \"(.*)\" into the text box above the label \"(.*)\"$")
    public void i_enter_the_value_into_the_text_box_above_the_label(String value, String label){

        WebElement labelElement = null;
        try {
            labelElement = Fixture.driver.findElement(By.xpath("//label[contains(.,'" + label + "')]"));
        }catch(NoSuchElementException noSuchElementException){

        }

        if(labelElement == null){
            try {
                labelElement = Fixture.driver.findElement(By.xpath("//h6[contains(.,'" + label + "')]"));
            }catch(NoSuchElementException noSuchElementException){

            }
        }

        if(labelElement != null)
            Fixture.driver.findElement(withTagName("input").above(labelElement)).sendKeys(value);
        else
            throw new NoSuchElementException("");
    }

    @When("^I enter the value \"(.*)\" into the text box that is both nearest the label \"(.*)\" and below the label \"(.*)\"$")
    public void i_enter_the_value_into_the_text_box_nearest_the_label_that_is_below_the_label(
            String value, String nearestLabel, String belowLabel){

        try {
            WebElement belowElement = Fixture.driver.findElement(By.xpath("//label[contains(.,'" + belowLabel + "')]"));

            try {
                WebElement nearestElement =
                        Fixture.driver.findElement(byXPath("//label[contains(.,'" + nearestLabel + "')]").
                                below(belowElement));

                Utility.getInputWebElementNearest(nearestElement, "text").sendKeys(value);

            } catch(NoSuchElementException noSuchElementException) {

            }
        }catch(NoSuchElementException noSuchElementException){

        }

    }

    @When("^I enter the value \"(.*)\" into the text box with the label \"(.*)\"$")
    public void i_enter_the_value_into_the_text_box_with_label(String value, String label) {

        WebElement labelElement = null;
        try {
            labelElement = Fixture.driver.findElement(By.xpath("//label[contains(.,'" + label + "')]"));
        }catch(NoSuchElementException noSuchElementException){

        }

        if(labelElement == null){
            try {
                labelElement = Fixture.driver.findElement(By.xpath("//h6[contains(.,'" + label + "')]"));
            }catch(NoSuchElementException noSuchElementException){

            }
        }

        WebElement inputElement = null;
        if(labelElement != null) {
            try{
                Utility.getInputWebElementNearest(labelElement, "text").sendKeys(value);
            }catch (NoSuchElementException noSuchElementException){

            }
            if(inputElement == null){
                try{
                    Fixture.driver.findElement(withTagName("input").below(labelElement)).sendKeys(value);
                }catch (NoSuchElementException noSuchElementException){

                }
            }

        }else
            throw new NoSuchElementException("");
    }

    @When("^I enter the value \"(.*)\" into the password box nearest the label \"(.*)\"$")
    public void i_enter_the_value_into_the_password_box_nearest_the_label(String value, String label) {
        Utility.getInputWebElementNearest(label, "password").sendKeys(value);
    }

    @When("^I enter the value \"(.*)\" into the password box with the label \"(.*)\"$")
    public void i_enter_the_value_into_the_password_box_with_label(String value, String label) {

        WebElement labelElement = null;
        try {
            labelElement = Fixture.driver.findElement(By.xpath("//label[contains(.,'" + label + "')]"));
        }catch(NoSuchElementException noSuchElementException){

        }

        if(labelElement == null){
            try {
                labelElement = Fixture.driver.findElement(By.xpath("//h6[contains(.,'" + label + "')]"));
            }catch(NoSuchElementException noSuchElementException){

            }
        }

        WebElement inputElement = null;
        if(labelElement != null) {
            try{
                Utility.getInputWebElementNearest(labelElement, "password").sendKeys(value);
            }catch (NoSuchElementException noSuchElementException){

            }
            if(inputElement == null){
                try{
                    Fixture.driver.findElement(withTagName("input").below(labelElement)).sendKeys(value);
                }catch (NoSuchElementException noSuchElementException){

                }
            }

        }else
            throw new NoSuchElementException("");
    }

    @When("^I enter the value \"(.*)\" into the number box nearest the label \"(.*)\"$")
    public void i_enter_the_value_into_the_number_box_nearest_the_label(String value, String label) {
        Utility.getInputWebElementNearest(label, "number").sendKeys(value);
    }

    @When("^I enter the value \"(.*)\" into the number box with the label \"(.*)\"$")
    public void i_enter_the_value_into_the_number_box_with_label(String value, String label) {
        WebElement labelElement = null;
        try {
            labelElement = Fixture.driver.findElement(By.xpath("//label[contains(.,'" + label + "')]"));
        }catch(NoSuchElementException noSuchElementException){

        }

        if(labelElement == null){
            try {
                labelElement = Fixture.driver.findElement(By.xpath("//h6[contains(.,'" + label + "')]"));
            }catch(NoSuchElementException noSuchElementException){

            }
        }

        WebElement inputElement = null;
        if(labelElement != null) {
            try{
                Utility.getInputWebElementNearest(labelElement, "number").sendKeys(value);
            }catch (NoSuchElementException noSuchElementException){

            }
            if(inputElement == null){
                try{
                    Fixture.driver.findElement(withTagName("input").below(labelElement)).sendKeys(value);
                }catch (NoSuchElementException noSuchElementException){

                }
            }

        }else
            throw new NoSuchElementException("");
    }

    @When("^I enter the date \"(.*)\" into the date picker nearest the label \"(.*)\"$")
    public void i_enter_the_date_into_the_date_picker_nearest_the_label(String value, String label) {
        Utility.getInputWebElementNearest(label, "date").sendKeys(value);
    }

    @When("^I enter the date \"(.*)\" into the date picker with the label \"(.*)\"$")
    public void i_enter_the_date_into_the_date_picker_with_label(String value, String label) {
        WebElement labelElement = null;
        try {
            labelElement = Fixture.driver.findElement(By.xpath("//label[contains(.,'" + label + "')]"));
        }catch(NoSuchElementException noSuchElementException){

        }

        if(labelElement == null){
            try {
                labelElement = Fixture.driver.findElement(By.xpath("//h6[contains(.,'" + label + "')]"));
            }catch(NoSuchElementException noSuchElementException){

            }
        }

        WebElement inputElement = null;
        if(labelElement != null) {
            try{
                Utility.getInputWebElementNearest(labelElement, "date").sendKeys(value);
            }catch (NoSuchElementException noSuchElementException){

            }
            if(inputElement == null){
                try{
                    Fixture.driver.findElement(withTagName("input").below(labelElement)).sendKeys(value);
                }catch (NoSuchElementException noSuchElementException){

                }
            }

        }else
            throw new NoSuchElementException("");
    }

    @When("^I click the button labelled \"(.*)\"$")
    public void i_click_the_button_labelled(String label) throws InterruptedException {
        Thread.sleep(1000);
        WebElement webElement = null;
        try{
            webElement = Fixture.driver.findElement(
                    By.xpath("//input[@type='submit' and @value='" + label + "']"));
        } catch(NoSuchElementException noSuchElementException){

        }

        if (webElement == null){
            try{
                webElement = Fixture.driver.findElement(By.xpath("//span[contains(.,'" + label + "')]"));
            } catch(NoSuchElementException noSuchElementException){

            }
        }

        if (webElement == null){
            try{
                webElement = Fixture.driver.findElement(By.xpath("//button[contains(.,'" + label + "')]"));
            } catch(NoSuchElementException noSuchElementException){

            }
        }

        if (webElement != null)
            webElement.click();
        else throw new NoSuchElementException("");

        Thread.sleep(1000);
    }

    @When("^I click the \"(.*)\" button$")
    public void i_click_the_button(String label) throws InterruptedException {
        Thread.sleep(1000);
        WebElement webElement = null;
        try{
            webElement = Fixture.driver.findElement(
                    By.xpath("//input[@type='submit' and @value='" + label + "']"));
        } catch(NoSuchElementException noSuchElementException){

        }

        if (webElement == null){
            try{
                webElement = Fixture.driver.findElement(By.xpath("//span[contains(.,'" + label + "')]"));
            } catch(NoSuchElementException noSuchElementException){

            }
        }

        if (webElement == null){
            try{
                webElement = Fixture.driver.findElement(By.xpath("//button[contains(.,'" + label + "')]"));
            } catch(NoSuchElementException noSuchElementException){

            }
        }

        if (webElement != null)
            webElement.click();
        else throw new NoSuchElementException("");

        Thread.sleep(1000);
    }

    @When("^I click the button nearest the label \"(.*)\"$")
    public void i_click_the_button_nearest_the_label(String label) {
        Utility.getButtonNearestLabel(label).click();
    }

    @When("^I click the button above the label \"(.*)\"$")
    public void i_click_the_button_above_the_label(String label) {
        WebElement labelElement1 = Fixture.driver.findElement(By.xpath("//button[contains(.,'" + label + "')]"));
        WebElement button = Fixture.driver.findElement(withTagName("button").above(labelElement1));
        JavascriptExecutor jse = (JavascriptExecutor) Fixture.driver;
        jse.executeScript("arguments[0].click()", button);
    }

    @When("^I click the button below the label \"(.*)\"$")
    public void i_click_the_button_below_the_label(String label) {
        WebElement labelElement1 = Fixture.driver.findElement(By.xpath("//button[contains(.,'" + label + "')]"));
        WebElement button = Fixture.driver.findElement(withTagName("button").below(labelElement1));
        JavascriptExecutor jse = (JavascriptExecutor) Fixture.driver;
        jse.executeScript("arguments[0].click()", button);
    }

    @When("^I click the confirmation button labelled \"(.*)\"$")
    public void i_click_the_confirmation_button_labelled(String label){
        WebElement button =  Fixture.driver.findElement(By.xpath("//button[contains(.,'" + label + "')]"));
        JavascriptExecutor jse = (JavascriptExecutor) Fixture.driver;
        jse.executeScript("arguments[0].click()", button);
    }

    @When("^I upload the file with the relative path \"(.*)\" by clicking the button labelled \"(.*)\"$")
    public void i_upload_the_file_with_the_relative_path_by_clicking_a_button_labelled(String path, String label) {
        WebElement fileInput = Fixture.driver.findElement(By.xpath("//input[@type='file']"));
        File file = new File(path);
        fileInput.sendKeys(file.getAbsolutePath());
    }

    @When("^I upload the file \"(.*)\" by clicking the \"(.*)\" button$")
    public void i_upload_the_file_by_clicking_the_button(String path, String label) {
        WebElement fileInput = Fixture.driver.findElement(By.xpath("//input[@type='file']"));
        File file = new File(path);
        fileInput.sendKeys(file.getAbsolutePath());
    }

    @When("^I click the check box button nearest the label \"(.*)\"$")
    public void i_click_the_check_box_button_nearest_the_label(String label) {
        Utility.getInputWebElementNearest(label, "checkbox").click();
    }

    @When("^I click the check box button with the label \"(.*)\"$")
    public void i_click_the_check_box_button_with_label(String label) {
        WebElement labelElement = null;
        try {
            labelElement = Fixture.driver.findElement(By.xpath("//label[contains(.,'" + label + "')]"));
        }catch(NoSuchElementException noSuchElementException){

        }

        if(labelElement == null){
            try {
                labelElement = Fixture.driver.findElement(By.xpath("//h6[contains(.,'" + label + "')]"));
            }catch(NoSuchElementException noSuchElementException){

            }
        }

        WebElement inputElement = null;
        if(labelElement != null) {
            try{
                Utility.getInputWebElementNearest(labelElement, "checkbox").click();
            }catch (NoSuchElementException noSuchElementException){

            }
            if(inputElement == null){
                try{
                    Fixture.driver.findElement(withTagName("input").below(labelElement)).click();
                }catch (NoSuchElementException noSuchElementException){

                }
            }

        }else
            throw new NoSuchElementException("");
    }

    @When("^I unselect the check box button nearest the label \"(.*)\"$")
    public void i_unselect_the_check_box_button_nearest_the_label(String label) {
        Utility.getInputWebElementNearest(label, "checkbox").click();
    }

    @When("^I unselect the check box button with the label \"(.*)\"$")
    public void i_unselect_the_check_box_button_with_label(String label) {
        WebElement labelElement = null;
        try {
            labelElement = Fixture.driver.findElement(By.xpath("//label[contains(.,'" + label + "')]"));
        }catch(NoSuchElementException noSuchElementException){

        }

        if(labelElement == null){
            try {
                labelElement = Fixture.driver.findElement(By.xpath("//h6[contains(.,'" + label + "')]"));
            }catch(NoSuchElementException noSuchElementException){

            }
        }

        WebElement inputElement = null;
        if(labelElement != null) {
            try{
                Utility.getInputWebElementNearest(labelElement, "checkbox").click();
            }catch (NoSuchElementException noSuchElementException){

            }
            if(inputElement == null){
                try{
                    Fixture.driver.findElement(withTagName("input").below(labelElement)).click();
                }catch (NoSuchElementException noSuchElementException){

                }
            }

        }else
            throw new NoSuchElementException("");
    }

    @When("^I select the value \"(.*)\" from the multiple select list nearest the label \"(.*)\"$")
    public void i_select_the_value_from_the_multiple_select_list_nearest_the_label(String value, String label) {
        Utility.getSelectNearest(label).selectByVisibleText(value);
    }

    @When("^I select the value \"(.*)\" from the multiple select list with the label \"(.*)\"$")
    public void i_select_the_value_from_the_multiple_select_list_with_label(String value, String label) {
        Utility.getSelectNearest(label).selectByVisibleText(value);
    }

    @When("^I unselect the value \"(.*)\" from the multiple select list nearest the label \"(.*)\"$")
    public void i_unselect_the_value_from_the_multiple_select_list_nearest_the_label(String value, String label) {
        Utility.getSelectNearest(label).deselectByVisibleText(value);
    }

    @When("^I unselect the value \"(.*)\" from the multiple select list with the label \"(.*)\"$")
    public void i_unselect_the_value_from_the_multiple_select_list_with_label(String value, String label) {
        Utility.getSelectNearest(label).deselectByVisibleText(value);
    }

    @When("^I select the values \"(.*)\" and \"(.*)\" from the multiple select list nearest the label \"(.*)\"$")
    public void i_select_the_values_from_the_multiple_select_list_nearest_the_label(String value1, String value2, String label) {
        Select select = Utility.getSelectNearest(label);
        select.selectByVisibleText(value1);
        select.selectByVisibleText(value2);
    }

    @When("^I select the values \"(.*)\" and \"(.*)\" from the multiple select list with the label \"(.*)\"$")
    public void i_select_the_values_from_the_multiple_select_list_with_label(String value1, String value2, String label) {
        Select select = Utility.getSelectNearest(label);
        select.selectByVisibleText(value1);
        select.selectByVisibleText(value2);
    }

    @When("^I unselect the values \"(.*)\" and \"(.*)\" from the multiple select list nearest the label \"(.*)\"$")
    public void i_unselect_the_values_from_the_multiple_select_list_nearest_the_label(String value1, String value2, String label) {
        Utility.getSelectNearest(label).deselectAll();
    }

    @When("^I unselect the values \"(.*)\" and \"(.*)\" from the multiple select list with the label \"(.*)\"$")
    public void i_unselect_the_values_from_the_multiple_select_list_with_label(String value1, String value2, String label) {
        Utility.getSelectNearest(label).deselectAll();
    }

    @When("^I wait for \"(.*)\" seconds$")
    public void i_wait_for_seconds(String seconds) throws InterruptedException {
        String millis = seconds + "000";
        Thread.sleep(Long.parseLong(millis));
    }

    @When("I click the link labelled \"(.*)\"$")
    public void i_click_the_link_labeled(String label){
        WebElement element = Fixture.driver.findElement(By.linkText(label));
        element.click();
    }

    @Then("^I should be taken to the page \"(.*)\"$")
    public void i_should_be_taken_to_the_page(String url) {
        Assert.assertEquals(url, Fixture.driver.getCurrentUrl());
    }

    @Then("^the check box button nearest the label \"(.*)\" is selected")
    public void the_check_box_button_nearest_the_label_is_selected(String label) {
        Assert.assertTrue(Utility.getInputWebElementNearest(label, "checkbox").isSelected());
    }

    @Then("^the check box button with the label \"(.*)\" is selected")
    public void the_check_box_button_with_label_is_selected(String label) {
        Assert.assertTrue(Utility.getInputWebElementNearest(label, "checkbox").isSelected());
    }

    @Then("^the check box button nearest the label \"(.*)\" is unselected")
    public void the_check_box_button_nearest_the_label_is_unselected(String label) {
        Assert.assertFalse(Utility.getInputWebElementNearest(label, "checkbox").isSelected());
    }

    @Then("^the check box button with the label \"(.*)\" is unselected")
    public void the_check_box_button_with_label_is_unselected(String label) {
        Assert.assertFalse(Utility.getInputWebElementNearest(label, "checkbox").isSelected());
    }

    @Then("^the value \"(.*)\" in the multiple select list nearest the label \"(.*)\" is selected")
    public void the_value_in_the_multiple_select_list_nearest_the_label_is_selected(String value, String label) {
        Select select = Utility.getSelectNearest(label);
        List<String> optionLabels = select.getAllSelectedOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        Assert.assertTrue(value, optionLabels.contains(value));
    }

    @Then("^the value \"(.*)\" in the multiple select list with the label \"(.*)\" is selected")
    public void the_value_in_the_multiple_select_list_with_label_is_selected(String value, String label) {
        Select select = Utility.getSelectNearest(label);
        List<String> optionLabels = select.getAllSelectedOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        Assert.assertTrue(value, optionLabels.contains(value));
    }

    @Then("^the value \"(.*)\" in the multiple select list nearest the label \"(.*)\" is unselected")
    public void the_value_in_the_multiple_select_list_nearest_the_label_is_unselected(String value, String label) {
        Select select = Utility.getSelectNearest(label);
        List<String> optionLabels = select.getAllSelectedOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        Assert.assertFalse(value, optionLabels.contains(value));
    }

    @Then("^the value \"(.*)\" in the multiple select list with the label \"(.*)\" is unselected")
    public void the_value_in_the_multiple_select_list_with_label_is_unselected(String value, String label) {
        Select select = Utility.getSelectNearest(label);
        List<String> optionLabels = select.getAllSelectedOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        Assert.assertFalse(value, optionLabels.contains(value));
    }

    @Then("^the values \"(.*)\" and \"(.*)\" in the multiple select list nearest the label \"(.*)\" are selected")
    public void the_values_in_the_multiple_select_list_nearest_the_label_are_selected(String value1, String value2, String label) {
        Select select = Utility.getSelectNearest(label);
        List<String> optionLabels = select.getAllSelectedOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        Assert.assertTrue(value1, optionLabels.contains(value1));
        Assert.assertTrue(value2, optionLabels.contains(value2));
    }

    @Then("^the values \"(.*)\" and \"(.*)\" in the multiple select list with the label \"(.*)\" are selected")
    public void the_values_in_the_multiple_select_list_with_label_are_selected(String value1, String value2, String label) {
        Select select = Utility.getSelectNearest(label);
        List<String> optionLabels = select.getAllSelectedOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        Assert.assertTrue(value1, optionLabels.contains(value1));
        Assert.assertTrue(value2, optionLabels.contains(value2));
    }

    @Then("^the values \"(.*)\" and \"(.*)\" in the multiple select list nearest the label \"(.*)\" are unselected")
    public void the_values_in_the_multiple_select_list_nearest_the_label_are_unselected(String value1, String value2, String label) {
        Select select = Utility.getSelectNearest(label);
        List<String> optionLabels = select.getAllSelectedOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        Assert.assertFalse(value1, optionLabels.contains(value1));
        Assert.assertFalse(value2, optionLabels.contains(value2));
    }

    @Then("^the values \"(.*)\" and \"(.*)\" in the multiple select list with the label \"(.*)\" are unselected")
    public void the_values_in_the_multiple_select_list_with_label_are_unselected(String value1, String value2, String label) {
        Select select = Utility.getSelectNearest(label);
        List<String> optionLabels = select.getAllSelectedOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        Assert.assertFalse(value1, optionLabels.contains(value1));
        Assert.assertFalse(value2, optionLabels.contains(value2));
    }

    @Then("^I should see the message \"(.*)\"$")
    public void I_should_see_the_message(String status) throws InterruptedException {
        Thread.sleep(3000);
        String message = null;
        try {
            message = Fixture.driver.findElement(By.xpath("//p[contains(.,\"" + status + "\")]")).getText();
        } catch (org.openqa.selenium.InvalidSelectorException invalidSelectorException){

        }
        if (message == null){
            try {
                message = Fixture.driver.findElement(By.xpath("//p[contains(.,'" + status + "')]")).getText();
            } catch (org.openqa.selenium.InvalidSelectorException invalidSelectorException){

            }
        }
        Assert.assertEquals(message, status);
    }

    @Then("^I should not see the message \"(.*)\"$")
    public void I_should_not_see_the_message(String status) throws InterruptedException {
        Thread.sleep(2000);
        String message = null;
        try {
            message = Fixture.driver.findElement(By.xpath("//p[contains(.,\"" + status + "\")]")).getText();
        } catch (org.openqa.selenium.NoSuchElementException noSuchElementException){

        }
        if (message == null){
            try {
                message = Fixture.driver.findElement(By.xpath("//p[contains(.,'" + status + "')]")).getText();
            } catch (org.openqa.selenium.NoSuchElementException noSuchElementException){

            }
        }

        if(message!=null) {
            if (message.equalsIgnoreCase(status))
                Assert.assertFalse(true);
        }else{
                Assert.assertTrue(true);
        }
    }

    @Then("^content appears on the page including the word \"(.*)\"$")
    public void content_appears_on_the_page_including_the_word(String word) throws InterruptedException {
        Thread.sleep(2000);
        String message = null;
        try {
            message = Fixture.driver.findElement(By.xpath("//h6[contains(.,'" + word + "')]")).getText();
        } catch(NoSuchElementException noSuchElementException){

        }

        if (message == null){
            try{
                message = Fixture.driver.findElement(By.xpath("//h5[contains(.,'" + word + "')]")).getText();
            } catch(NoSuchElementException noSuchElementException){

            }
        }

        if (message == null){
            try{
                message = Fixture.driver.findElement(By.xpath("//h4[contains(.,'" + word + "')]")).getText();
            } catch(NoSuchElementException noSuchElementException){

            }
        }

        if (message == null){
            try{
                message = Fixture.driver.findElement(By.xpath("//th[contains(.,'" + word + "')]")).getText();
            } catch(NoSuchElementException noSuchElementException){

            }
        }

        if(message!=null) {
            if (message.equalsIgnoreCase(word))
                Assert.assertTrue(true);
            else
                Assert.assertTrue(false);
        }else throw new NoSuchElementException("");
    }

    @Then("^the file \"(.*)\" is downloaded$")
    public void the_file_is_downloaded(String fileName) throws InterruptedException {
        Thread.sleep(1000);
        String location = null;
        try (InputStream input = new FileInputStream("src/test/resources/config.properties")) {
            Properties property = new Properties();
            property.load(input);
            location = property.getProperty("download.directory");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if(location.length()!=0) {
            Assert.assertTrue(isTheFileDownloaded(location, fileName));
        }else{
            location = System.getProperty("user.home") + File.separator + "Downloads";
            Assert.assertTrue(isTheFileDownloaded(location, fileName));
        }
    }

    private boolean isTheFileDownloaded(String downloadPath, String fileName) {

        File directory = new File(downloadPath);
        File[] directoryContents = directory.listFiles();

        if(directoryContents == null)
            return false;
        for (File file: directoryContents) {
            if (file.getName().equals(fileName))
                return true;
        }
        return false;
    }

    private String extractURL(String url) {
        String newUrl = null;
        try (InputStream input = new FileInputStream("src/test/resources/config.properties")) {
            Properties property = new Properties();
            property.load(input);
            String serverName = property.getProperty("server.name.myserver");
            if(url.contains("/")) {
                String page = url.substring(url.indexOf("/"));
                newUrl = serverName.concat(page);
            }else{
                newUrl = serverName;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return newUrl;
    }

    private Properties loadConfigProperty(){
        Properties property = new Properties();
        try (InputStream input = new FileInputStream("src/test/resources/config.properties")) {
            property.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return property;
    }
}

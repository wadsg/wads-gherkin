package io.github.wadsg.selenium_bdd.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import java.util.List;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.locators.RelativeLocator.withTagName;
import static io.github.wadsg.selenium_bdd.steps.Fixture.driver;

public class Utility {


    public static WebElement getButtonNearestLabel(String label) {

        WebElement labelElement = null;
        try {
            labelElement = driver.findElement(By.xpath("//label[contains(.,'" + label + "')]"));
        }catch(NoSuchElementException noSuchElementException){

        }

        if(labelElement == null){
            try {
                labelElement = driver.findElement(By.xpath("//h6[contains(.,'" + label + "')]"));
            }catch(NoSuchElementException noSuchElementException){

            }
        }

        if(labelElement != null) {
            List<WebElement> webElements =
                    driver.findElements(withTagName("button").near(labelElement))
                            .stream()
                            .collect(Collectors.toList());
            return findNearestWebElementTo(labelElement, webElements);
        }else throw new NoSuchElementException("");
    }

    public static WebElement getInputWebElementNearest(String label, String type) {

        WebElement labelElement = driver.findElement(By.xpath("//label[contains(.,'" + label + "')]"));
        return getInputWebElementNearest(labelElement, type);
    }

    public static WebElement getInputWebElementNearest(WebElement webElement, String type) {
        List<WebElement> webElements =
                driver.findElements(withTagName("input").near(webElement))
                        .stream()
                        .filter(e->e.getAttribute("type").equalsIgnoreCase(type))
                        .collect(Collectors.toList());

        return findNearestWebElementTo(webElement, webElements);
    }

    public static Select getSelectNearest(String label){
        WebElement labelElement = driver.findElement(By.xpath("//label[contains(.,'" + label+ "')]"));
        List<WebElement> selectElements = driver.findElements(withTagName("select").near(labelElement));
        return new Select(findNearestWebElementTo(labelElement, selectElements));
    }

    public static WebElement findNearestWebElementTo(WebElement origin, List<WebElement> candidates){
        WebElement closest = candidates.get(0);
        for (WebElement webElement: candidates)
            if (distanceBetween(origin, webElement) < distanceBetween(origin, closest))
                closest = webElement;

        return closest;
    }

    private static Double distanceBetween(WebElement webElement1, WebElement webElement2){
        Point point1 = webElement1.getLocation();
        Point point2 = webElement2.getLocation();
        return Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
    }
}

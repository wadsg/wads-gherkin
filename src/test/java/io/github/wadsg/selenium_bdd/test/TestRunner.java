package io.github.wadsg.selenium_bdd.test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"io/github/wadsg/selenium_bdd/steps/common", "io/github/wadsg/selenium_bdd/steps/react"},
        plugin = {"json:target/cucumber.json","html:target/site/cucumber-pretty"}
)
public class TestRunner {
}

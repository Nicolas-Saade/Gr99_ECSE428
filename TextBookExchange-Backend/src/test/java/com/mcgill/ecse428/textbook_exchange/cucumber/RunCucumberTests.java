package com.mcgill.ecse428.textbook_exchange.cucumber;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.mcgill.ecse428.textbook_exchange.cucumber.stepdefinitions",
    plugin = {
        "pretty",
        "junit:build/test-results/cucumber.xml",   
        "html:build/reports/cucumber-report.html"    
    }
)

public class RunCucumberTests {
}

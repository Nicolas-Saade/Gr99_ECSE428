package com.mcgill.ecse428.textbook_exchange.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import io.cucumber.junit.platform.engine.Constants;

@Suite
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "com.mcgill.ecse428.textbook_exchange.cucumber.steps")
public class RunCucumberTests {
    // Empty class; annotations do the work.
}
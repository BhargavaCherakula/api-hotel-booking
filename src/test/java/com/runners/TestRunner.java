package com.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/admin.feature",
        glue = {"com.steps"},
        plugin = {"pretty", "html:target/cucumber-reports.html",
                "json:target/cucumber.json",
                "junit:target/cucumber.xml"},
        tags = "@Positive",
        monochrome = true
)

public class TestRunner {
}

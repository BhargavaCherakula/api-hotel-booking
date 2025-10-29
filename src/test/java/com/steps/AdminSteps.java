package com.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminSteps {
    @Given("the secure admin credentials have been provisioned")
    public void theSecureAdminCredentialsHaveBeenProvisioned() {
    }

    @When("the admin sends a POST request to the login endpoint")
    public void theAdminSendsAPOSTRequestToTheLoginEndpoint() {

    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int arg0) {
    }

    @And("the response should contain a non-empty cache key")
    public void theResponseShouldContainANonEmptyCacheKey() {
    }

    @Given("the admin has invalid credentials {string} and {string}")
    public void theAdminHasInvalidCredentialsAnd(String arg0, String arg1) {

    }

    @And("the response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String arg0) {
    }
}

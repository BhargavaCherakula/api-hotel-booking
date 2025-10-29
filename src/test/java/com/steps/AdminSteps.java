package com.steps;

import com.base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.model.request.LoginRequest;
import com.utils.ConfigReader;
import com.utils.Log4jUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AdminSteps extends BaseTest {
    public static String username;
    public static String password;
    public static Response response;
    public static String cacheKey;
    private final LoginRequest loginRequest = new LoginRequest();


    @Given("the secure admin credentials have been provisioned")
    public void theSecureAdminCredentialsHaveBeenProvisioned() {
        username = ConfigReader.getUsername();
        password = ConfigReader.getPassword();
        assertThat("Username should not be null or empty", username, not(blankOrNullString()));
        assertThat("Password should not be null or empty", password, not(blankOrNullString()));
        Log4jUtils.info("Admin credentials have been successfully provisioned.");
    }

    @When("the admin sends a POST request to the login endpoint")
    public void theAdminSendsAPOSTRequestToTheLoginEndpoint() throws JsonProcessingException {
        Log4jUtils.info("Sending POST request to login endpoint with admin credentials.");
        loginRequest.setUserName(username);
        loginRequest.setPassword(password);
        response = postLoginRequest(loginRequest);
        Log4jUtils.info("Login request submitted successfully.");
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        Log4jUtils.info("Validating response status code.");
        assertThat("Unexpected response code", response.getStatusCode(), equalTo(statusCode));
        Log4jUtils.info("Response status code verified: " + response.getStatusCode());
    }

    @And("the response should contain a non-empty cache key")
    public void theResponseShouldContainANonEmptyCacheKey() {
        Log4jUtils.info("Extracting cache key (token) from login response.");
        cacheKey = response.jsonPath().getString("token");
        setCacheKey(cacheKey);
        assertThat("Cache key should not be null or empty", cacheKey, not(blankOrNullString()));
        Log4jUtils.info("Cache key received successfully and stored for future requests.");
    }

    @Given("the admin has invalid credentials {string} and {string}")
    public void theAdminHasInvalidCredentialsAnd(String user, String pass) {
        username = user;
        password = pass;
        Log4jUtils.info("Initialized admin login, with invalid credentials: \nusername =" + username + " \npassword =" + password);
    }

    @And("the response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String expectedMessage) {
        Log4jUtils.info("Validating error message in response.");
        String actualMessage = response.jsonPath().getString("error");
        assertThat("Error message should match", actualMessage, containsString(expectedMessage));
        Log4jUtils.info("Error message validation passed. Received message: " + actualMessage);
    }
}

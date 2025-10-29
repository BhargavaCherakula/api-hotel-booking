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

public class BookingSteps extends BaseTest {
    public static String cacheKey;
    private final LoginRequest loginRequest = new LoginRequest();

    @Given("A valid admin user retrieves the authentication token successfully")
    public void aValidAdminUserRetrievesTheAuthenticationTokenSuccessfully() throws JsonProcessingException {
        Log4jUtils.info("Initializing admin authentication process...");
        loginRequest.setUserName(ConfigReader.getUsername());
        loginRequest.setPassword(ConfigReader.getPassword());
        cacheKey = postLoginRequest(loginRequest).jsonPath().getString("token");
        setCacheKey(cacheKey);
        Log4jUtils.info("Authentication token retrieved and cached successfully. \n token="+cacheKey);
    }

    @Then("user should book the room successfully with status as {int}")
    public void userShouldBookTheRoomSuccessfullyWithStatusAs(int arg0) {

    }

    @Then("user should receive an appropriate error message with status as {int}")
    public void userShouldReceiveAnAppropriateErrorMessageWithStatusAs(int arg0) {

    }

    @Given("user creates a room booking with following details")
    public void userCreatesARoomBookingWithFollowingDetails() {

    }

    @And("user should receive an error response {string}")
    public void userShouldReceiveAnErrorResponse(String arg0) {
    }

    @Given("the user performs a room search and receives a response with status code {int}")
    public void theUserPerformsARoomSearchAndReceivesAResponseWithStatusCode(int arg0) {
        System.out.println("Hello...");
        
    }

    @Then("the user verifies the available room types and their features")
    public void theUserVerifiesTheAvailableRoomTypesAndTheirFeatures() {
        
    }

    @When("the user retrieves the booking ID from the booking confirmation response")
    public void theUserRetrievesTheBookingIDFromTheBookingConfirmationResponse() {
        
    }

    @And("the user updates the booking using the retrieved booking ID with the following details")
    public void theUserUpdatesTheBookingUsingTheRetrievedBookingIDWithTheFollowingDetails() {
        
    }

    @Then("the booking should be updated successfully with status code {int}")
    public void theBookingShouldBeUpdatedSuccessfullyWithStatusCode(int arg0) {
        
    }

    @When("the user deletes the booking using the booking ID")
    public void theUserDeletesTheBookingUsingTheBookingID() {
        
    }

    @Then("the user searches for the deleted booking and should receive the following error response")
    public void theUserSearchesForTheDeletedBookingAndShouldReceiveTheFollowingErrorResponse() {
    }

    @And("the admin verifies that the updated user details are correctly reflected in the room summary")
    public void theAdminVerifiesThatTheUpdatedUserDetailsAreCorrectlyReflectedInTheRoomSummary() {
        
    }

    @Then("the admin confirms that the same user details are updated in the booking report:")
    public void theAdminConfirmsThatTheSameUserDetailsAreUpdatedInTheBookingReport() {
    }
}

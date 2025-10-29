package com.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BookingSteps {

    @Given("A valid admin user retrieves the authentication token successfully")
    public void aValidAdminUserRetrievesTheAuthenticationTokenSuccessfully() {
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
}

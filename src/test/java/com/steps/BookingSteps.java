package com.steps;

import com.base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.utils.ConfigReader;
import com.utils.Log4jUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BookingSteps extends BaseTest {
    public static String cacheKey;
    public static Response response;
    public static int bookingId;

    @Given("A valid admin user retrieves the authentication token successfully")
    public void aValidAdminUserRetrievesTheAuthenticationTokenSuccessfully() throws JsonProcessingException {
        Log4jUtils.info("Initializing admin authentication process...");
        loginRequest.setUserName(ConfigReader.getUsername());
        loginRequest.setPassword(ConfigReader.getPassword());
        cacheKey = postLoginRequest().jsonPath().getString("token");
        setCacheKey(cacheKey);
        Log4jUtils.info("Authentication token retrieved and cached successfully. \n token=" + cacheKey);
    }

    @Given("user creates a room booking with following details")
    public void userCreatesARoomBookingWithFollowingDetails(DataTable dataTable) throws JsonProcessingException {
        List<List<String>> bookingData = dataTable.asLists(String.class);
        bookingRequest.setRoomid(getRoomId(bookingData.get(0).get(6)));
        bookingRequest.setFirstname(bookingData.get(0).get(0));
        bookingRequest.setLastname(bookingData.get(0).get(1));
        bookingRequest.setEmail(bookingData.get(0).get(2));
        bookingRequest.setPhone(bookingData.get(0).get(3));
        dates.setCheckin(bookingData.get(0).get(4));
        dates.setCheckout(bookingData.get(0).get(5));
        bookingRequest.setBookingdates(dates);
        response = postBookingRequest();
        Log4jUtils.info("Booking request posted successfully! \n Response: \n" + response.asString());
    }

    @Then("user should book the room successfully with status as {int}")
    public void userShouldBookTheRoomSuccessfullyWithStatusAs(int statusCode) {
        Log4jUtils.info("Validating response status code.");
        assertThat("Unexpected response code", response.getStatusCode(), equalTo(statusCode));
        Log4jUtils.info("Response status code verified: " + response.getStatusCode());
    }

    @Then("user should receive an appropriate error message with status as {int}")
    public void userShouldReceiveAnAppropriateErrorMessageWithStatusAs(int statusCode) {
        Log4jUtils.info("Validating error response status code.");
        assertThat("Unexpected response code", response.getStatusCode(), equalTo(statusCode));
        Log4jUtils.info("Response status code verified: " + response.getStatusCode());

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

    @Given("the user sends a request to retrieve all available rooms and receives a {int} OK response")
    public void theUserSendsARequestToRetrieveAllAvailableRoomsAndReceivesAOKResponse(int arg0) {

    }

    @Then("the user should see the following room types with correct accessibility, image, description, features, and price information")
    public void theUserShouldSeeTheFollowingRoomTypesWithCorrectAccessibilityImageDescriptionFeaturesAndPriceInformation() {
    }
}

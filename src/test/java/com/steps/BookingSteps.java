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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.containsString;

public class BookingSteps extends BaseTest {
    public static String cacheKey;
    public static Response response;
    public static int bookingId;

    @Given("A valid admin user retrieves the authentication token successfully")
    public void getAuthToken() throws JsonProcessingException {
        Log4jUtils.info("Initializing admin authentication process...");
        loginRequest.setUserName(ConfigReader.getUsername());
        loginRequest.setPassword(ConfigReader.getPassword());
        cacheKey = postLoginRequest().jsonPath().getString("token");
        setCacheKey(cacheKey);
        Log4jUtils.info("Authentication token retrieved and cached successfully. \n token=" + cacheKey);
    }

    @Given("user creates a room booking with following details")
    public void createBooking(DataTable dataTable) throws JsonProcessingException {
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
    public void verifyBookingStatus(int statusCode) {
        Log4jUtils.info("Validating response status code.");
        assertThat("Unexpected response code", response.getStatusCode(), equalTo(statusCode));
        Log4jUtils.info("Response status code verified: " + response.getStatusCode());
    }

    @Then("user should receive an appropriate error message with status as {int}")
    public void verifyErrorStatus(int statusCode) {
        Log4jUtils.info("Validating error response status code.");
        assertThat("Unexpected response code", response.getStatusCode(), equalTo(statusCode));
        Log4jUtils.info("Response status code verified: " + response.getStatusCode());

    }

    @And("user should receive an error response {string}")
    public void verifyErrorMessage(String error) {
        Log4jUtils.info("Validating API error response.");
        assertThat("Erro messagee is not showing as expected",
                response.jsonPath().getList("errors").get(0).toString(), containsString(error));
        Log4jUtils.info("Error response validation passed. The expected error message '{}' was found." + error);
    }

    @Then("the user verifies the available room types and their features")
    public void verifyRoomDetails() {


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

    @Given("the user sends a request to retrieve all available rooms")
    public void getAllRooms() {
        response = getListOfRooms();
    }

    @Then("the user should see the following room types with correct accessibility, image, description, features, and price information")
    public void verifyRoomInfo(DataTable dataTable) {
        Log4jUtils.info("Starting validation of room details against API response...");
        List<List<String>> roomData = dataTable.asLists(String.class);
        List<Map<String, Object>> roomsList = response.jsonPath().getList("rooms");

        String roomType = roomData.get(0).get(0);
        boolean accessible = Boolean.parseBoolean(roomData.get(0).get(1));
        String image = roomData.get(0).get(2);
        String description = roomData.get(0).get(3);
        List<String> features = Arrays.asList(roomData.get(0).get(4).split(","));
        int price = Integer.parseInt(roomData.get(0).get(5));

        //-- to check the expected room object under rooms array
        Map<String, Object> actualRoom = null;
        for (Map<String, Object> room : roomsList) {
            if (room.get("type").equals(roomType)) {
                actualRoom = room;
                break;
            }
        }
        if (actualRoom == null) {
            throw new AssertionError("Expected room type is not found in the response: " + roomType);
        }

        Log4jUtils.info("Found room type '" + roomType + "' in response. Validating attributes...");

        assertThat("The accessible mismatch for the room" + roomType,
                actualRoom.get("accessible"), equalTo(accessible));
        assertThat("The Image path mismatch for the room: " + roomType,
                actualRoom.get("image"), equalTo(image));
        assertThat("The description mismatch for room: " + roomType,
                actualRoom.get("description").toString(), containsString(description));
        assertThat("The price mismatch for room: " + roomType,
                ((Number) actualRoom.get("roomPrice")).intValue(), equalTo(price));
        List<String> actualFeatures = (List<String>) actualRoom.get("features");
        for (String feature : features) {
            assertThat("The feature missing in the room: " + roomType,
                    actualFeatures, hasItem(feature));
        }
        Log4jUtils.info("All room details successfully validated against expected feature file data.");
    }

    @Given("the user performs a room search by room type as {string}")
    public void searchByRoomType(String roomType) {
        int id = getRoomId(roomType);
        response = getRequestWithoutBody(id);
    }

    @Given("user searches for available rooms between the specified check-in and check-out dates")
    public void searchByDates(DataTable dataTable) {
        List<List<String>> bookingData = dataTable.asLists(String.class);
        String checkInDate = bookingData.get(0).get(0);
        String checkOutDate = bookingData.get(0).get(1);
        response = getRequestWithoutBody(checkInDate, checkOutDate);
    }

    @Then("user should receives a response with status code {int}")
    public void userShouldReceivesAResponseWithStatusCode(int statusCode) {
        Log4jUtils.info("Validating response status code.");
        assertThat("Unexpected response code", response.getStatusCode(), equalTo(statusCode));
        Log4jUtils.info("Response status code verified: " + response.getStatusCode());
        Log4jUtils.info("Response payload:\n " + response.asString());
    }
}

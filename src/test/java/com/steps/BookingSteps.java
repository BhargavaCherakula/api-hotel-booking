package com.steps;

import com.base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public static Response response;
    public static int bookingId;
    public static List<String> getDates;

    @Given("A valid admin user retrieves the authentication token successfully")
    public void getAuthToken() throws JsonProcessingException {
        Log4jUtils.info("Initializing admin authentication process...");
        String cacheKey = getTokenKey();
        setCacheKey(cacheKey);
        Log4jUtils.info("Authentication token retrieved and cached successfully. \n token=" + cacheKey);
    }

    @Given("user creates a room booking with following details")
    public void createBooking(DataTable dataTable) throws JsonProcessingException, InterruptedException {
        Thread.sleep(3000);
        List<List<String>> bookingData = dataTable.asLists(String.class);
        getDates = getRandomCheckInAndCheckOutDates("yyyy-MM-dd");
        bookingRequest.setRoomid(getRoomId(bookingData.get(1).get(4)));
        bookingRequest.setFirstname(bookingData.get(1).get(0));
        bookingRequest.setLastname(bookingData.get(1).get(1));
        bookingRequest.setEmail(bookingData.get(1).get(2));
        bookingRequest.setPhone(bookingData.get(1).get(3));
        bookingRequest.setDepositpaid(Boolean.parseBoolean(bookingData.get(1).get(5)));
        dates.setCheckin(getDates.get(0));
        dates.setCheckout(getDates.get(1));
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

    @Then("user should receive an error status as {int}")
    public void verifyErrorStatus(int statusCode) {
        Log4jUtils.info("Validating error response status code.");
        assertThat("Unexpected response code", response.getStatusCode(), equalTo(statusCode));
        Log4jUtils.info("Response status code verified: " + response.getStatusCode());

    }

    @And("user should receive an error response {string}")
    public void verifyErrorMessage(String error) {
        Log4jUtils.info("Validating API error response.");
        assertThat("Error message is not showing as expected", response.jsonPath().getList("errors").get(0).toString(), containsString(error));
        Log4jUtils.info("Error response validation passed. The expected error message '{}' was found." + error);
    }

    @When("the user retrieves the booking ID from the booking confirmation response")
    public void getBookingId() {
        bookingId = response.jsonPath().getInt("bookingid");
        Log4jUtils.info("Booking Id fetched from the response. \n Booking Id = " + bookingId);
    }

    @And("the user updates the booking using the retrieved booking ID with the following details")
    public void updateBooking(DataTable dataTable) throws JsonProcessingException, InterruptedException {
        Thread.sleep(2000);
        List<List<String>> updatedBookingData = dataTable.asLists(String.class);
        getDates = getRandomCheckInAndCheckOutDates("yyyy-MM-dd");
        updateBookingRequest.setBookingid(bookingId);
        updateBookingRequest.setRoomid(getRoomId(updatedBookingData.get(1).get(2)));
        updateBookingRequest.setFirstname(updatedBookingData.get(1).get(0));
        updateBookingRequest.setLastname(updatedBookingData.get(1).get(1));
        updateBookingRequest.setDepositpaid(Boolean.parseBoolean(updatedBookingData.get(1).get(3)));
        dates.setCheckin(getDates.get(0));
        dates.setCheckout(getDates.get(1));
        updateBookingRequest.setBookingdates(dates);
        response = putUpdatedBookingRequest(bookingId);
        Log4jUtils.info("Booking request posted successfully! \n Response: \n" + response.asString());
    }

    @When("the user deletes the booking using the booking ID")
    public void deletesBookingByBookingID() throws InterruptedException, JsonProcessingException {
        Thread.sleep(2000);
        response = deleteBookingRequest(bookingId);
        Log4jUtils.info("user has successfully deleted booking..." + response.asString());

    }

    @Then("the user searches for the deleted booking")
    public void searchDeletedBookingById() throws JsonProcessingException {
        response = getBookingRequest(bookingId);

    }

    @When("the user retrieves the room summary details by room type {string}")
    public void getRoomSummaryResponse(String roomType) throws JsonProcessingException {
        response = getRoomSummary(getRoomId(roomType));
    }

    @And("the user confirms that the created or updated booking details appear correctly in the room summary")
    public void bookingConfirmationInRoomSummary(DataTable dataTable) throws JsonProcessingException, InterruptedException {
        Thread.sleep(4000);
        List<List<String>> bookingData = dataTable.asLists(String.class);
        String firstName = bookingData.get(1).get(0);
        String lastName = bookingData.get(1).get(1);
        int roomId = getRoomId(bookingData.get(1).get(2));

        List<Map<String, Object>> bookings = response.jsonPath().getList("bookings");
        Map<String, Object> expectedBooking = null;

        for (Map<String, Object> booking : bookings) {
            int bookingID = (int) booking.get("bookingid");
            if (bookingID == bookingId) {
                expectedBooking = booking;
                break;
            }
        }
        if (expectedBooking == null) {
            throw new AssertionError("Booking ID '" + bookingId + "' not found in the " + bookingData.get(1).get(2) +
                    " room type response, while searching with room id: " + roomId + ", Looks like BUG");
        }

        Log4jUtils.info("Found booking details for the user '" + firstName + "' in response. Validating details");

        assertThat("The room-id is mismatch for the booking response", expectedBooking.get("roomid"), equalTo(roomId));
        assertThat("The firstname is mismatch in the booking response", expectedBooking.get("firstname"), equalTo(firstName));
        assertThat("The lastname is mismatch in the booking response", expectedBooking.get("lastname"), equalTo(lastName));

        // Extract and validate check-in and check-out
        Map<String, String> actualBookingDates = (Map<String, String>) expectedBooking.get("bookingdates");
        String actualCheckIn = actualBookingDates.get("checkin");
        String actualCheckOut = actualBookingDates.get("checkout");
        assertThat("Check-in date mismatch for booking ID ", actualCheckIn, equalTo(getDates.get(0)));
        assertThat("Check-out date mismatch for booking ID ", actualCheckOut, equalTo(getDates.get(1)));

        Log4jUtils.info("user details successfully validated in the booking summary response...");
    }


    @When("the user retrieves the booking report")
    public void theUserRetrievesTheBookingReport() throws JsonProcessingException {
        response = getBookingReport();
    }

    @Then("the user confirms that the same user details are displaying in the booking report:")
    public void verifyUserDetailsInReport(DataTable dataTable) {
        List<List<String>> bookingData = dataTable.asLists(String.class);
        String title = bookingData.get(1).get(0);
        List<Map<String, Object>> bookingReports = response.jsonPath().getList("report");
        Map<String, Object> matchedReport = null;
        for (Map<String, Object> report : bookingReports) {
            if (report.get("title").toString().split("-")[0].trim().contains(title)) {
                matchedReport = report;
            }
        }
        if (matchedReport == null) {
            throw new AssertionError("Expected title not found in the booking report: " + title);
        }

        assertThat("Report title mismatch!",
                matchedReport.get("title").toString().split("-")[0].trim(), containsString(title));

        Log4jUtils.info("user details successfully validated in the booking report response...");
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

        assertThat("The accessible mismatch for the room" + roomType, actualRoom.get("accessible"), equalTo(accessible));
        assertThat("The Image path mismatch for the room: " + roomType, actualRoom.get("image"), equalTo(image));
        assertThat("The description mismatch for room: " + roomType, actualRoom.get("description").toString(), containsString(description));
        assertThat("The price mismatch for room: " + roomType, ((Number) actualRoom.get("roomPrice")).intValue(), equalTo(price));

        List<String> actualFeatures = (List<String>) actualRoom.get("features");
        for (String feature : features) {
            assertThat("The feature missing in the room: " + roomType, actualFeatures, hasItem(feature));
        }
        Log4jUtils.info("All room details successfully validated against expected feature file data.");
    }

    @Given("the user performs a room search by room type as {string}")
    public void searchByRoomType(String roomType) {
        int id = getRoomId(roomType);
        response = getRequestWithoutBody(id);
    }

    @Given("user generates random check-in and check-out dates")
    public void getRandomDates() {
        getDates = getRandomCheckInAndCheckOutDates("yyyy-MM-dd");
        Log4jUtils.info("Generated Check-in: " + getDates.get(0) + " | Check-out: " + getDates.get(1));
    }

    @When("user sends a request to search available rooms using generated dates")
    public void searchAvailableRooms() {
        response = getRequestWithoutBody(getDates.get(0), getDates.get(1));
        Log4jUtils.info("Room search request sent for dates: " + dates);
    }

    @Then("user should receives a response with status code {int}")
    public void userShouldReceivesAResponseWithStatusCode(int statusCode) {
        Log4jUtils.info("Validating response status code.");
        assertThat("Unexpected response code", response.getStatusCode(), equalTo(statusCode));
        Log4jUtils.info("Response status code verified: " + response.getStatusCode());
    }


    @And("user should receive an error as {string}")
    public void userShouldReceiveAnErrorAs(String error) {
        Log4jUtils.info("Validating API error response by searching deleted booking.");
        assertThat("Error message is not showing as expected", response.jsonPath().get("error").toString(), containsString(error));
        Log4jUtils.info("Error response validation passed. The expected error message '{}' was found." + error);
    }
}

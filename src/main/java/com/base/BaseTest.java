package com.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.request.BookingDates;
import com.model.request.BookingRequest;
import com.model.request.LoginRequest;
import com.utils.ConfigReader;
import com.utils.Log4jUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BaseTest {
    //=== Endpoint Resources ===
    private static final String res_login = "/api/auth/login";
    private static final String res_booking = "/api/booking";
    private static final String res_room = "/api/room";
    private static final String res_report = "/api/report";

    protected LoginRequest loginRequest = new LoginRequest();
    protected BookingRequest bookingRequest = new BookingRequest();
    protected BookingDates dates = new BookingDates();
    ObjectMapper objectMapper = new ObjectMapper();
    private String cacheKey;

    /**
     * to post the login request and return the response
     *
     * @throws JsonProcessingException if error occurs while converting JSON request
     */
    public Response postLoginRequest() throws JsonProcessingException {
        String reqBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(loginRequest);
        RestAssured.baseURI = ConfigReader.getBaseUrl();
        return RestAssured
                .given().contentType("application/json").accept("application/json").body(reqBody)
                .when().post(res_login)
                .then().extract().response();
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    /**
     * to get the room id based on the room type
     * @param roomType
     * @return
     */
    public int getRoomId(String roomType) {
        switch (roomType.toLowerCase()) {
            case "single":
                return 1;
            case "double":
                return 2;
            case "suite":
                return 3;
            default:
                return 1;
        }
    }

    /**
     * Sends a POST request to create a new room booking.
     * @return
     * @throws JsonProcessingException
     */
    public Response postBookingRequest() throws JsonProcessingException {
        String reqBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookingRequest);
        Log4jUtils.info("Booking Request Payload: \n" + reqBody);
        RestAssured.baseURI = ConfigReader.getBaseUrl();
        return RestAssured
                .given().contentType("application/json").accept("application/json").body(reqBody)
                .when().post(res_booking)
                .then().extract().response();
    }

    /**
     * Sends a simple GET request to retrieve details for a specific room by its ID
     * @param roomId
     * @return
     */
    public Response getRequestWithoutBody(int roomId) {
        String baseUrl = ConfigReader.getBaseUrl();
        String path = res_room + "/" + roomId;
        Log4jUtils.info("Sending GET request to: " + baseUrl + path);
        return RestAssured
                .given()
                .baseUri(baseUrl)
                .when()
                .get(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a GET request to retrieve available rooms for a specific check-in and check-out date range
     * @param checkIn
     * @param checkOut
     * @return
     */
    public Response getRequestWithoutBody(String checkIn, String checkOut) {
        String baseUrl = ConfigReader.getBaseUrl();
        Log4jUtils.info(String.format(
                "Sending GET request to: %s%s?checkin=%s&checkout=%s",
                baseUrl, res_room, checkIn, checkOut));
        return RestAssured
                .given()
                .baseUri(baseUrl)
                .queryParam("checkin", checkIn)
                .queryParam("checkout", checkOut)
                .when()
                .get(res_room)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a GET request to retrieve all available rooms without applying any filters.
     * @return
     */
    public Response getListOfRooms() {
        String baseUrl = ConfigReader.getBaseUrl();
        Log4jUtils.info("Sending GET request to: " + baseUrl + res_room);
        return RestAssured
                .given()
                .baseUri(baseUrl)
                .when()
                .get(res_room)
                .then()
                .extract()
                .response();
    }

}

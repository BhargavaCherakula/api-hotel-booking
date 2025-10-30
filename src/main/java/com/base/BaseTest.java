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


    public Response postBookingRequest() throws JsonProcessingException {
        String reqBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookingRequest);
        Log4jUtils.info("Booking Request Payload: \n" + reqBody);
        RestAssured.baseURI = ConfigReader.getBaseUrl();
        return RestAssured
                .given().contentType("application/json").accept("application/json").body(reqBody)
                .when().post(res_booking)
                .then().extract().response();
    }

}

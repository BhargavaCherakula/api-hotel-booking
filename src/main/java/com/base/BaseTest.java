package com.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.request.LoginRequest;
import com.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BaseTest {
    //=== Endpoint Resources ===
    private static final String res_login = "/api/auth/login";
    private static final String res_booking = "/api/booking";
    private static final String res_room = "/api/room";
    private static final String res_report = "/api/report";

    ObjectMapper objectMapper = new ObjectMapper();
    private String cacheKey;

    /**
     * to post the login request and return the response
     *
     * @throws JsonProcessingException if error occurs while converting JSON request
     */
    public Response postLoginRequest(LoginRequest loginRequest) throws JsonProcessingException {
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
}

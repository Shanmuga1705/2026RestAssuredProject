package com.example.api;

import com.example.utils.payloads.Payload;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PlacesApi {
    private static final String API_KEY = "qaclick123";

    public Response addPlace() {
        return given()
                .queryParam("key", API_KEY)
                .header("Content-Type", "application/json")
                .body(Payload.addPlace())
                .when()
                .post("/maps/api/place/add/json");
    }

    public Response updatePlace(String placeId, String address) {
        return given()
                .queryParam("key", API_KEY)
                .header("Content-Type", "application/json")
                .body(Payload.updatePlace(placeId, address))
                .when()
                .put("/maps/api/place/update/json");
    }

    public Response getPlace(String placeId) {
        return given()
                .queryParam("place_id", placeId)
                .queryParam("key", API_KEY)
                .when()
                .get("/maps/api/place/get/json");
    }

    public Response deletePlace(String placeId) {
        return given()
                .queryParam("key", API_KEY)
                .queryParam("place_id", placeId)
                .when()
                .delete("/maps/api/place/delete/json");
    }
}

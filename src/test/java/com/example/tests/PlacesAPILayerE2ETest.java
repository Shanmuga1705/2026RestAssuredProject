package com.example.tests;

import com.example.api.PlacesApi;
import com.example.utils.JsonUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class PlacesAPILayerE2ETest {
    private final PlacesApi placesApi = new PlacesApi();
    private String placeId;
    private String newAddress = "70 winter walk, USA";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
    }

    @Test
    public void testAddPlace() {
        Response response = placesApi.addPlace();

        response.then().assertThat().statusCode(200)
                .body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.52 (Ubuntu)");

        JsonPath jsonPath = JsonUtils.stringToJsonPath(response.asString());
        placeId = jsonPath.getString("place_id");
        System.out.println("Place ID: " + placeId);
    }

    @Test(dependsOnMethods = "testAddPlace")
    public void testUpdatePlace() {
        placesApi.updatePlace(placeId, newAddress)
                .then().assertThat().log().all().statusCode(200)
                .body("msg", equalTo("Address successfully updated"));
    }

    @Test(dependsOnMethods = "testUpdatePlace")
    public void testGetPlace() {
        Response response = placesApi.getPlace(placeId);
        response.then().assertThat().log().all().statusCode(200);

        JsonPath jsonPath = JsonUtils.stringToJsonPath(response.asString());
        String actualAddress = jsonPath.getString("address");
        System.out.println("Actual Address: " + actualAddress);

        assert actualAddress.equals(newAddress);
    }

    @Test(dependsOnMethods = "testGetPlace")
    public void testDeletePlace() {
        placesApi.deletePlace(placeId)
                .then().assertThat().log().all().statusCode(200);
    }
}

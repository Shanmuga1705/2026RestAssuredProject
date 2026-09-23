package com.example.tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import com.example.utils.JsonUtils;
import com.example.utils.payloads.Payload;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;//for assertions like equalTo

public class PlacesAPIE2ETest {

    private String placeId;
    private String newAddress = "70 winter walk, USA";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
    }

    @Test
    public void testAddPlace() {
        //Add Place API
        String response = given().log().all().queryParam("key","qaclick123")
        .header("Content-Type","application/json")
        .body(Payload.addPlace()) //getting the body from another class method
        .when().post("/maps/api/place/add/json")
        .then().assertThat().statusCode(200)
        .body("scope", equalTo("APP")) //equalTo is a static method from hamcrest library
        .header("Server","Apache/2.4.52 (Ubuntu)").extract().response().asString(); //extracting the response as string 

        System.out.println(response); //printing. the response
        //JsonPath is a class from RestAssured which helps to parse the response and get the values from it
        JsonPath js = new JsonPath(response); 
        placeId = js.getString("place_id");
        System.out.println("Place ID: " + placeId);
    }

    @Test(dependsOnMethods = {"testAddPlace"})
    public void testUpdatePlace() {
        //Update Place API
        given().log().all().queryParam("key", "qaclick123")
        .header("Content-Type","application/json")
        .body(Payload.updatePlace(placeId, newAddress)) //getting the body from another class method
        .when().put("/maps/api/place/update/json")
        .then().assertThat().log().all().statusCode(200)
        .body("msg", equalTo("Address successfully updated"));
    }

    @Test(dependsOnMethods = {"testUpdatePlace"})
    public void testGetPlace() {
        //Get Place API
        String getResponse = given().log().all().queryParam("place_id", placeId)
        .queryParam("key", "qaclick123")
        .when().get("/maps/api/place/get/json")
        .then().assertThat().log().all().statusCode(200)
        .extract().response().asString();

        JsonPath js1 = JsonUtils.stringToJsonPath(getResponse); //parsing the response to JsonPath object
        String actualAddress = js1.getString("address");
        System.out.println("Actual Address: " + actualAddress);
        assertEquals(actualAddress, newAddress); //asserting the actual address with the new address
        //assert actualAddress.equals(newAddress); //asserting the actual address with the new address
    }

    @Test(dependsOnMethods = {"testGetPlace"})
    public void testDeletePlace() {
        //Delete Place API
        given().log().all().queryParam("key", "qaclick123")
        .queryParam("place_id", placeId)
        .when().delete("/maps/api/place/delete/json")
        .then().assertThat().log().all().statusCode(200);
    }

}

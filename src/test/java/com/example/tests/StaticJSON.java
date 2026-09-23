package com.example.tests;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.Test;

import com.example.utils.JsonUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class StaticJSON { //
    @Test
    public void addBook() throws IOException
    {
        RestAssured.baseURI="https://rahulshettyacademy.com";
        String resp=given().queryParam("key", "qaclick123")
                .header("Content-Type","application/json")
                .body(GenerateStringFromResource("C:\\work\\Addbookdetails.json")) //reading the json file from the path and passing it to the body
                .when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath js= JsonUtils.stringToJsonPath(resp);//parsing the response to JsonPath object
        String placeId=js.getString("place_id");
        System.out.println("Place Id is: " + placeId);
    }
    
    public static String GenerateStringFromResource(String path) throws IOException {
    return new String(Files.readAllBytes(Paths.get(path)));//reading the file and  readAllBytes returns a byte array to convert it to string 
    }
}

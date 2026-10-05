package com.example.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

import com.example.utils.payloads.Credentials;
import com.example.utils.payloads.LoginResponse;
import org.testng.annotations.Test;

public class ECommerceAPITest {

    @Test
    public void login() {
        
        RequestSpecification request = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
             //   .addHeader("Content-Type", "application/json")
                .setContentType(ContentType.JSON).build();   

        Credentials credentials = new Credentials();
        credentials.setUserEmail("jshanmugam@euclid.com");
        credentials.setUserPassword("Window44$");
        
        
        LoginResponse loginResponse = given()
            .spec(request).body(credentials)
        .when()
            .post("/api/ecom/auth/login")
        .then()
            .statusCode(200)
            .extract().response().as(LoginResponse.class);// Extracting the response as a LoginResponse object

        System.out.println(loginResponse.getToken());
        System.out.println(loginResponse.getUserId());
        System.out.println(loginResponse.getMessage());

    }
}

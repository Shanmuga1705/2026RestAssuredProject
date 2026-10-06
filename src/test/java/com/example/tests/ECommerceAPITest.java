package com.example.tests;

import java.io.File;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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
        String token = loginResponse.getToken();
        System.out.println(loginResponse.getUserId());
        String userId = loginResponse.getUserId();
        System.out.println(loginResponse.getMessage());

        //Add a product to the cart

        RequestSpecification addProductBaseRequest = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", loginResponse.getToken()).build();

        File productImage = new File("src/test/resources/laptop-small.png");

        RequestSpecification addProductRequest = given().log().all().spec(addProductBaseRequest)
                .param("productName", "Laptop")
                .param("productAddedBy", loginResponse.getUserId())
                .param("productCategory", "Electronics")
                .param("productSubCategory", "Laptops")
                .param("productPrice", "45000")
                .param("productDescription", "Lenova")
                .param("productFor", "men")
                .multiPart("productImage", productImage);

        Response addProductResponse = addProductRequest.when().post("/api/ecom/product/add-product")
                .then().log().all().assertThat().statusCode(201).extract().response();

                String productId = addProductResponse.jsonPath().getString("productId");
                System.out.println("Product ID: " + productId);

        


    }
}

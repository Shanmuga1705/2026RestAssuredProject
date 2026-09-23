package com.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import org.testng.annotations.Test;

import com.example.utils.JsonUtils;
import com.example.utils.payloads.Payload;

import org.testng.annotations.DataProvider;

public class DynamicJSON {

    @Test(dataProvider = "BooksData") //using data provider to pass multiple data sets to the test method
    public void addBook(String isbn, String aisle)
    {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all().header("Content-Type","application/json")
                .body(Payload.addBook(isbn, aisle)) //getting the body from another class method
                .when().post("/Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath js = JsonUtils.stringToJsonPath(response);
        String msg = js.getString("Msg");
        System.out.println(msg);

        String id = js.getString("ID");
        System.out.println(id);           
    }

    @Test(dataProvider = "BooksData", dependsOnMethods = "addBook")
    public void deleteBook(String isbn, String aisle)
    {
        given().log().all().header("Content-Type", "application/json")
                .body(Payload.deleteBook(isbn + aisle))
                .when().delete("/Library/DeleteBook.php")
                .then().assertThat().statusCode(200)
                .body("msg", org.hamcrest.Matchers.equalTo("book is successfully deleted"));
    }

    @DataProvider(name ="BooksData")
    public Object[][] getData()
    {
        return new Object[][] {{"abc","123"},{"def","456"},{"ghi","789"}}; //array of array object to store multiple data sets
    }
}

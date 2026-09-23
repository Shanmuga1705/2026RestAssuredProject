package com.example.tests;
import org.testng.annotations.Test;
import com.example.utils.payloads.Payload;

import org.testng.Assert;
import io.restassured.path.json.JsonPath;

public class ComplexNestedJSONParse {
    @Test
    public void testComplexJSONParse() {

        JsonPath js = new JsonPath(Payload.coursePrice()); // Create a JsonPath object from the JSON response

        int count = js.getInt("courses.size()");
        System.out.println("Number of courses: " + count);

        int purchaseAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println("Purchase Amount: " + purchaseAmount);

        String firstCourseTitle = js.getString("courses[0].title");
        System.out.println("Title of the first course: " + firstCourseTitle);

        System.out.println("Course Titles and Prices:");
        for (int i = 0; i < count; i++) {
            String courseTitle = js.getString("courses[" + i + "].title");
            int coursePrice = js.getInt("courses[" + i + "].price");
            System.out.println(courseTitle + " >> " + coursePrice);
        }

        System.out.println("Number of copies sold for RPA course:");
        for (int i = 0; i < count; i++) {
            String courseTitle = js.getString("courses[" + i + "].title");
            if (courseTitle.equalsIgnoreCase("RPA")) {
                int courseCopies = js.getInt("courses[" + i + "].copies");
                System.out.println(courseCopies);
                break; // Exit the loop once the RPA course is found
            }
        }

        System.out.println("Verifying the sum of all course prices and copies matches the purchase amount:");
        int totalAmount = 0;
        
        for (int i = 0; i < count; i++) {
            int coursePrice = js.getInt("courses[" + i + "].price");
            int courseCopies = js.getInt("courses[" + i + "].copies");
            totalAmount += coursePrice * courseCopies;
        }

        System.out.println("Total Amount: " + totalAmount);
        Assert.assertEquals(totalAmount, purchaseAmount);
    }
}

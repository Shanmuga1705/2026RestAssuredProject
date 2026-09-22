package com.example.utils;

import io.restassured.path.json.JsonPath;

public class JsonUtils {
    public static JsonPath stringToJsonPath(String responseBody) {
        JsonPath js = new JsonPath(responseBody);
        return js;
    }
}

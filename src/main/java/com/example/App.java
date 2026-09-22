package com.example;

public class App {
    public String greet() {
        return "Hello from Rest_Assured Project";
    }

    public static void main(String[] args) {
        System.out.println(new App().greet());
    }
}

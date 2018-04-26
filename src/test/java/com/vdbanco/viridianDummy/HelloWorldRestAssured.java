package com.vdbanco.viridianDummy;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;

public class HelloWorldRestAssured {

    @Test
    public void makeSureThatGoogleIsUp() {
        given().when().get("http://www.google.com").then().statusCode(200);
    }
}

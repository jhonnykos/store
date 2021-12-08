package com.learnup.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CategoryTests {
    public static final String CATEGORY_ENDPOINT = "categories/{id}";
    public static Properties properties = new Properties();
    @BeforeEach
    public void setUp() throws IOException {
        properties.load(new FileInputStream("src/test/resources/application.properties"));
        RestAssured.baseURI = properties.getProperty("baseUri");
    }

    @Test
    public void getCategory(){
        ValidatableResponse validatableResponse = given()
                .when()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .get(CATEGORY_ENDPOINT,3)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("id", equalTo(3));
//        assertThat(validatableResponse.body("id", equalTo(3)), is(true));
    }

    @Test
    public void getCategoryResponse(){
        Response response = given()
                .when()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .get(CATEGORY_ENDPOINT,3)
                .prettyPeek();
//        assertThat(response.statusCode(),equalTo(200));
        assertThat(response.body().jsonPath().get("products[0].categoryTitle"), equalTo("Food"));
//        assertThat(validatableResponse.body("id", equalTo(3)), is(true));
    }

    @Test
    public void getProductResponse(){
        Response response = given()
                .when()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .get("/products")
                .prettyPeek();
//        assertThat(response.statusCode(),equalTo(200));
//        assertThat(response.body().jsonPath().get("products[0].categoryTitle"), equalTo("Food"));
//        assertThat(validatableResponse.body("id", equalTo(3)), is(true));
    }
}

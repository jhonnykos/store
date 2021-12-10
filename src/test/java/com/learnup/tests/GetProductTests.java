package com.learnup.tests;

import com.github.javafaker.Faker;
import com.learnup.dto.Product;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.learnup.tests.CategoryTests.properties;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductTests {
    public static final String PRODUCT_ENDPOINT = "products/{id}";
    public static final String PRODUCT_ENDPOINT_ALL = "products";

    private Product product;
    private Integer id;

    @BeforeEach
    public void setUp() throws IOException {
        properties.load(new FileInputStream("src/test/resources/application.properties"));
        RestAssured.baseURI = properties.getProperty("baseUri");

        Faker faker = new Faker();

        product = Product.builder()
                .price(500)
                .title(faker.food().dish())
                .categoryTitle("Food")
                .build();

        postProduct();
    }

    public void postProduct() {
        id = given()
                .body(product)
                .header("Content-Type", "application/json")
                .when()
                .post(PRODUCT_ENDPOINT_ALL)
                .prettyPeek()
                .body()
                .as(Product.class)
                .getId();
    }

    @Test
    public void getAllProducts() {
        Response response = given()
                .when()
                .log()
                .all()
                .get(PRODUCT_ENDPOINT_ALL)
                .prettyPeek();
        List responseBody = response.body().as(List.class);
        assertThat(response.statusCode(), equalTo(200));
        assertThat(responseBody.size() >= 1, equalTo(true));
    }

    @Test
    public void getProductPositive() {
        Response response = given()
                .when()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .get(PRODUCT_ENDPOINT, id)
                .prettyPeek();
        Product responseBody = response.as(Product.class);
        assertThat(response.statusCode(), equalTo(200));
        assertThat(responseBody.getId(), equalTo(id));
        assertThat(responseBody.getTitle(), equalTo(product.getTitle()));
        assertThat(responseBody.getPrice(), equalTo(product.getPrice()));
        assertThat(responseBody.getPrice(), equalTo(product.getPrice()));
    }

    @Test
    public void getProductNonExist() {
        tearDown();
        Response response = given()
                .when()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .get(PRODUCT_ENDPOINT, id)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(404));
        postProduct();
    }

    @Test
    public void getProductNegativeId() {
        Response response = given()
                .when()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .get(PRODUCT_ENDPOINT, -id)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(404));
    }

    @Test
    public void getProductStringId() {
        Response response = given()
                .when()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .get(PRODUCT_ENDPOINT, "seven")
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(400));
    }

    @Test
    public void getProductStringNullId() {
        Response response = given()
                .when()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .get(PRODUCT_ENDPOINT, "null")
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(400));
    }

    @AfterEach
    public void tearDown() {
        when()
                .delete(PRODUCT_ENDPOINT, id)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}

package com.learnup.tests;

import com.learnup.dto.Product;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static com.learnup.tests.CategoryTests.properties;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductTest {
    public static final String PRODUCT_ENDPOINT = "products/{id}";
    public static final String PRODUCT_ENDPOINT_ALL = "products";

    private Product product;
    private Integer id;
    private Integer price;
    private String title;
    private String categoryTitle;

    @BeforeEach
    public void setUp() throws IOException {
        properties.load(new FileInputStream("src/test/resources/application.properties"));
        RestAssured.baseURI = properties.getProperty("baseUri");

        price = 500;
        title = "Tomato";
        categoryTitle = "Food";

        product = Product.builder()
                .price(price)
                .title(title)
                .categoryTitle(categoryTitle)
                .build();

        postProduct();
    }

    public void postProduct() {
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .when()
                .post("http://80.78.248.82:8189/market/api/v1/products")
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void getAllProducts() {
        Response response = given()
                .when()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .get(PRODUCT_ENDPOINT_ALL)
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(200));
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
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.body().jsonPath().get("id"), equalTo(id));
        assertThat(response.body().jsonPath().get("title"), equalTo(title));
        assertThat(response.body().jsonPath().get("categoryTitle"), equalTo(categoryTitle));
        assertThat(response.body().jsonPath().get("price"), equalTo(price));
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
    public void getProductEmptyId() {
        Response response = given()
                .when()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .get(PRODUCT_ENDPOINT, "")
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

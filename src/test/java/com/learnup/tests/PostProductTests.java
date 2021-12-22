package com.learnup.tests;

import com.learnup.dto.Product;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static com.learnup.tests.CategoryTests.properties;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;

public class PostProductTests {
    public static final String PRODUCT_ENDPOINT = "products";

    private Product product;
    private Integer id;
    private Integer price;
    private String title;
    private String categoryTitle;

    @BeforeEach
    void setUp() throws IOException {
        properties.load(new FileInputStream("src/test/resources/application.properties"));
        RestAssured.baseURI = properties.getProperty("baseUri");

        price = 300;
        title = "Egg";
        categoryTitle = "Food";

        product = Product.builder()
                .price(price)
                .title(title)
                .categoryTitle(categoryTitle)
                .build();
    }

    @Test
    public void postProductPositive() {
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(201)
                .body("title", equalTo(title), "price", equalTo(price),
                        "categoryTitle", equalTo(categoryTitle))
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductNegativePrice() {
        product.setPrice(-price);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(404)
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductZeroPrice() {
        product.setPrice(0);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(404)
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductNullPrice() {
        product.setPrice(null);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(404)
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductDecimalPrice() {
        product.setPrice(100.25);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(201)
                .body("title", equalTo(title), "price", equalTo(100.25),
                        "categoryTitle", equalTo(categoryTitle))
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductDecimalPriceZero() {
        product.setPrice(100.00);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(201)
                .body("title", equalTo(title), "price", equalTo(100),
                        "categoryTitle", equalTo(categoryTitle))
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductDecimalPrice3() {
        product.setPrice(100.251);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(404)
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }


    @Test
    public void postProductTitleWithSpaces() {
        title = "Bean sprout";
        product.setTitle(title);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(201)
                .body("title", equalTo(title))
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductTitleWithTrailingSpace() {
        title = " " + title;
        product.setTitle(title);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(201)
                .body("title", equalTo(title.trim()))
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductTitleWithLeadingSpace() {
        title += " ";
        product.setTitle(title);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(201)
                .body("title", equalTo(title.trim()))
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductEmptyTitle() {
        title = "";
        product.setTitle(title);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(404)
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductNullTitle() {
        title = null;
        product.setTitle(title);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(404)
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductNumberTitle() {
        title = "45";
        product.setTitle(title);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(404)
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductInvalidCategory() {
        categoryTitle = "Car";
        product.setCategoryTitle(categoryTitle);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(404)
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductNumberCategory() {
        categoryTitle = "45";
        product.setCategoryTitle(categoryTitle);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(404)
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductEmptyCategory() {
        categoryTitle = "";
        product.setCategoryTitle(categoryTitle);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(404)
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @Test
    public void postProductNullCategory() {
        categoryTitle = null;
        product.setCategoryTitle(categoryTitle);
        id = given()
                .body(product.toString())
                .header("Content-Type", "application/json")
                .log()
                .all()
                .expect()
                .statusCode(404)
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .jsonPath()
                .get("id");
    }

    @AfterEach
    public void tearDown() {
        when()
                .delete("http://80.78.248.82:8189/market/api/v1/products/{id}", id)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
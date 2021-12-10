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

import static com.learnup.tests.CategoryTests.properties;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class PostProductTests {
    public static final String PRODUCT_ENDPOINT = "products";

    private Product product;
    private Integer id;

    @BeforeEach
    void setUp() throws IOException {
        properties.load(new FileInputStream("src/test/resources/application.properties"));
        RestAssured.baseURI = properties.getProperty("baseUri");

        Faker faker = new Faker();
        product = Product.builder()
                .price(300)
                .title(faker.food().dish())
                .categoryTitle("Food")
                .build();
    }

    @Test
    public void postProductPositive() {
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(201));
        assertThat(id, is(not(nullValue())));
        assertThat(responseBody.getTitle(), equalTo(product.getTitle()));
        assertThat(responseBody.getPrice(), equalTo(product.getPrice()));
        assertThat(responseBody.getCategoryTitle(), equalTo(product.getCategoryTitle()));
    }

    @Test
    public void postProductNegativePrice() {
        product.setPrice(-500);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(404));
    }

    @Test
    public void postProductZeroPrice() {
        product.setPrice(0);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(404));
    }

    @Test
    public void postProductNullPrice() {
        product.setPrice(null);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(404));
    }

    @Test
    public void postProductTitleWithSpaces() {
        String title = "Bean sprout";
        product.setTitle(title);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(201));
        assertThat(id, is(not(nullValue())));
        assertThat(responseBody.getTitle(), equalTo(title));
        assertThat(responseBody.getPrice(), equalTo(product.getPrice()));
        assertThat(responseBody.getCategoryTitle(), equalTo(product.getCategoryTitle()));
    }

    @Test
    public void postProductTitleWithTrailingSpace() {
        String title = "Egg ";
        product.setTitle(title);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(201));
        assertThat(id, is(not(nullValue())));
        assertThat(responseBody.getTitle(), equalTo(title.trim()));
        assertThat(responseBody.getPrice(), equalTo(product.getPrice()));
        assertThat(responseBody.getCategoryTitle(), equalTo(product.getCategoryTitle()));
    }

    @Test
    public void postProductTitleWithLeadingSpace() {
        String title = " Egg";
        product.setTitle(title);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(201));
        assertThat(id, is(not(nullValue())));
        assertThat(responseBody.getTitle(), equalTo(title.trim()));
        assertThat(responseBody.getPrice(), equalTo(product.getPrice()));
        assertThat(responseBody.getCategoryTitle(), equalTo(product.getCategoryTitle()));
    }

    @Test
    public void postProductEmptyTitle() {
        String title = "";
        product.setTitle(title);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(404));
    }

    @Test
    public void postProductNullTitle() {
        String title = null;
        product.setTitle(title);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(404));
    }

    @Test
    public void postProductNumberTitle() {
        String title = "45";
        product.setTitle(title);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(404));
    }

    @Test
    public void postProductInvalidCategory() {
        String categoryTitle = "Car";
        product.setCategoryTitle(categoryTitle);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(404));
    }

    @Test
    public void postProductCategoryWithLeadingSpace() {
        String categoryTitle = " Food";
        product.setCategoryTitle(categoryTitle);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(201));
        assertThat(id, is(not(nullValue())));
        assertThat(responseBody.getPrice(), equalTo(product.getPrice()));
        assertThat(responseBody.getTitle(), equalTo(product.getTitle()));
        assertThat(responseBody.getCategoryTitle(), equalTo(categoryTitle.trim()));
    }


    @Test
    public void postProductCategoryWithTrailingSpace() {
        String categoryTitle = "Food ";
        product.setCategoryTitle(categoryTitle);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(201));
        assertThat(id, is(not(nullValue())));
        assertThat(responseBody.getPrice(), equalTo(product.getPrice()));
        assertThat(responseBody.getTitle(), equalTo(product.getTitle()));
        assertThat(responseBody.getCategoryTitle(), equalTo(categoryTitle.trim()));
    }

    @Test
    public void postProductNumberCategory() {
        String categoryTitle = "45";
        product.setCategoryTitle(categoryTitle);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(404));
    }

    @Test
    public void postProductEmptyCategory() {
        String categoryTitle = "";
        product.setCategoryTitle(categoryTitle);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(404));
    }

    @Test
    public void postProductNullCategory() {
        String categoryTitle = null;
        product.setCategoryTitle(categoryTitle);
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(404));
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
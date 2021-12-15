package com.learnup.tests.products;

import com.github.javafaker.Faker;
import com.learnup.dto.Product;
import com.learnup.tests.BaseTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.learnup.Endpoints.PRODUCT_ENDPOINT;
import static com.learnup.Endpoints.PRODUCT_ENDPOINT_ID;
import static com.learnup.enums.CategoryType.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.Method.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductTests extends BaseTest {

    private Product product;
    private Integer id;
    private RequestSpecification postProductRequestSpec;
    private ResponseSpecification postProductResponseSpec;

    @BeforeEach
    public void setUp() throws IOException {

        Faker faker = new Faker();
        product = Product.builder()
                .price(500)
                .title(faker.food().dish())
                .categoryTitle(FOOD.getCategoryName())
                .build();

        postProductRequestSpec = getPostProductRequestSpec(product);
        postProductResponseSpec = getPostProductResponseSpec(product, POST);
        postProduct();
    }

    public void postProduct() {
        id = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek()
                .body()
                .as(Product.class)
                .getId();
    }

    @Test
    public void getAllProducts() {
        Response response =
                when()
                        .get(PRODUCT_ENDPOINT)
                        .prettyPeek();
        List responseBody = response.body().as(List.class);
        assertThat(responseBody.size() >= 1, equalTo(true));
    }

    @Test
    public void getProductPositive() {
        Response response =
                when()
                        .get(PRODUCT_ENDPOINT_ID, id)
                        .prettyPeek();
        Product responseBody = response.as(Product.class);
    }

    @Test
    public void getProductNonExist() {
        tearDown();
        Response response = given()
                .response()
                .spec(productFailResponseSpec_404)
                .when()
                .get(PRODUCT_ENDPOINT_ID, id)
                .prettyPeek();
        postProduct();
    }

    @Test
    public void getProductNegativeId() {
        Response response = given()
                .response()
                .spec(productFailResponseSpec_404)
                .when()
                .get(PRODUCT_ENDPOINT_ID, -id)
                .prettyPeek();
    }

    @Test
    public void getProductStringId() {
        Response response = given()
                .response()
                .spec(productFailResponseSpec_400)
                .when()
                .get(PRODUCT_ENDPOINT_ID, "seven")
                .prettyPeek();
    }

    @Test
    public void getProductStringNullId() {
        Response response = given()
                .response()
                .spec(productFailResponseSpec_400)
                .when()
                .get(PRODUCT_ENDPOINT_ID, "null")
                .prettyPeek();
    }

    @AfterEach
    public void tearDown() {
        given()
                .response()
                .spec(deleteProductResponseSpec)
                .when()
                .delete(PRODUCT_ENDPOINT_ID, id)
                .prettyPeek();
    }
}

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

import static com.learnup.Endpoints.PRODUCT_ENDPOINT;
import static com.learnup.Endpoints.PRODUCT_ENDPOINT_ID;
import static com.learnup.enums.CategoryType.*;
import static io.restassured.RestAssured.*;
import static io.restassured.http.Method.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class PostProductTests extends BaseTest {

    private Product product;
    private Integer id;
    private RequestSpecification postProductRequestSpec;
    private ResponseSpecification postProductResponseSpec;
    private ResponseSpecification postProductFailResponseSpec;

    @BeforeEach
    public void setUp() throws IOException {
        Faker faker = new Faker();
        product = Product.builder()
                .price(300)
                .title(faker.food().dish())
                .categoryTitle(FOOD.getCategoryName())
                .build();
        postProductFailResponseSpec = productFailResponseSpec_404;
    }

    public void setSpec() {
        postProductRequestSpec = getPostProductRequestSpec(product);
        postProductResponseSpec = getPostProductResponseSpec(product, POST);
    }

    @Test
    public void postProductPositive() {
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductMinPrice() {
        product.setPrice(1);
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductLargePrice() {
        product.setPrice(1_000_000);
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductNegativePrice() {
        product.setPrice(-500);
        setSpec();
        Response response = given(postProductRequestSpec, postProductFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductZeroPrice() {
        product.setPrice(0);
        setSpec();
        Response response = given(postProductRequestSpec, postProductFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductNullPrice() {
        product.setPrice(null);
        setSpec();
        Response response = given(postProductRequestSpec, postProductFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductTitleWithSpaces() {
        String title = "Bean sprout";
        product.setTitle(title);
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductTitleWithTrailingSpace() {
        String title = "Egg ";
        product.setTitle(title);
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductTitleWithLeadingSpace() {
        String title = " Egg";
        product.setTitle(title);
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductEmptyTitle() {
        String title = "";
        product.setTitle(title);
        setSpec();
        Response response = given(postProductRequestSpec, postProductFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductNullTitle() {
        String title = null;
        product.setTitle(title);
        setSpec();
        Response response = given(postProductRequestSpec, postProductFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductNumberTitle() {
        String title = "45";
        product.setTitle(title);
        setSpec();
        Response response = given(postProductRequestSpec, postProductFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductInvalidCategory() {
        String categoryTitle = "Car";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        Response response = given(postProductRequestSpec, postProductFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductCategoryWithLeadingSpace() {
        String categoryTitle = " Food";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        Response response = given(postProductRequestSpec, postProductFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
        assertThat(response.statusCode(), equalTo(201));
    }

    @Test
    public void postProductCategoryWithTrailingSpace() {
        String categoryTitle = "Food ";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        Response response = given(postProductRequestSpec, postProductFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductNumberCategory() {
        String categoryTitle = "45";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        Response response = given(postProductRequestSpec, postProductFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductEmptyCategory() {
        String categoryTitle = "";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        Response response = given(postProductRequestSpec, postProductFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
    }

    @Test
    public void postProductNullCategory() {
        String categoryTitle = null;
        product.setCategoryTitle(categoryTitle);
        setSpec();
        Response response = given(postProductRequestSpec, postProductFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        Product responseBody = response.body().as(Product.class);
        id = responseBody.getId();
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
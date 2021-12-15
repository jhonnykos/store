package com.learnup.tests.products;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.github.javafaker.Faker;
import com.learnup.asserts.IsProduct;
import com.learnup.dto.Product;
import com.learnup.tests.BaseTest;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.learnup.Endpoints.PRODUCT_ENDPOINT;
import static com.learnup.Endpoints.PRODUCT_ENDPOINT_ID;
import static com.learnup.asserts.IsCategoryExists.isCategoryExists;
import static com.learnup.asserts.IsProduct.*;
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

    @BeforeEach
    public void setUp() throws IOException {
        Faker faker = new Faker();
        product = Product.builder()
                .price(300)
                .title(faker.food().dish())
                .categoryTitle(FOOD.getCategoryName())
                .build();
    }

    public void setSpec() {
        postProductRequestSpec = getPostProductRequestSpec(product);
        postProductResponseSpec = getPostProductResponseSpec(product);
    }

    public void setIdForDelete(Response response) {
        if (checkProduct(response)) {
            id = response.body().as(Product.class).getId();
        }
    }

    public boolean checkProduct(Response response) {
        return isProduct().matches(response);
    }

    @Test
    public void postProductPositive() {
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductMinPrice() {
        product.setPrice(1);
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductLargePrice() {
        product.setPrice(1_000_000);
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductNegativePrice() {
        product.setPrice(-500);
        setSpec();
        Response response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductZeroPrice() {
        product.setPrice(0);
        setSpec();
        Response response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductNullPrice() {
        product.setPrice(null);
        setSpec();
        Response response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductTitleWithSpaces() {
        String title = "Bean sprout";
        product.setTitle(title);
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductTitleWithTrailingSpace() {
        String title = "Egg ";
        product.setTitle(title);
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductTitleWithLeadingSpace() {
        String title = " Egg";
        product.setTitle(title);
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductEmptyTitle() {
        String title = "";
        product.setTitle(title);
        setSpec();
        Response response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductNullTitle() {
        String title = null;
        product.setTitle(title);
        setSpec();
        Response response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductNumberTitle() {
        String title = "45";
        product.setTitle(title);
        setSpec();
        Response response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductInvalidCategory() {
        String categoryTitle = "Car";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        Response response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertThat(categoryTitle, is(not(isCategoryExists())));
        setIdForDelete(response);
    }

    @Test
    public void postProductCategoryWithLeadingSpace() {
        String categoryTitle = " Food";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertThat(response.body().as(Product.class).getCategoryTitle(), isCategoryExists());
        setIdForDelete(response);
    }

    @Test
    public void postProductCategoryWithTrailingSpace() {
        String categoryTitle = "Food ";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        Response response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertThat(response.body().as(Product.class).getCategoryTitle(), isCategoryExists());
        setIdForDelete(response);
    }

    @Test
    public void postProductNumberCategory() {
        String categoryTitle = "45";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        Response response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductEmptyCategory() {
        String categoryTitle = "";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        Response response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @Test
    public void postProductNullCategory() throws UnrecognizedPropertyException {
        String categoryTitle = null;
        product.setCategoryTitle(categoryTitle);
        setSpec();
        Response response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        setIdForDelete(response);
    }

    @AfterEach
    public void tearDown() {
        if (id != null) {
            given()
                    .response()
                    .spec(deleteProductResponseSpec)
                    .when()
                    .delete(PRODUCT_ENDPOINT_ID, id)
                    .prettyPeek();
        }
    }
}
package com.learnup.tests.products;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.github.javafaker.Faker;
import com.learnup.asserts.IsProduct;
import com.learnup.asserts.common.ProductDbAsserts;
import com.learnup.dto.Product;
import com.learnup.tests.BaseTest;
import io.qameta.allure.*;
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
import static com.learnup.asserts.common.ProductDbAsserts.*;
import static com.learnup.enums.CategoryType.*;
import static io.restassured.RestAssured.*;
import static io.restassured.http.Method.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Tests for products")
@Story("Post Product tests")
@Severity(SeverityLevel.CRITICAL)
public class PostProductTests extends BaseTest {

    private Product product;
    private Integer id;
    private RequestSpecification postProductRequestSpec;
    private ResponseSpecification postProductResponseSpec;
    private Response response;

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

    public void setIdForDelete() {
        if (checkProduct(response)) {
            id = response.body().as(Product.class).getId();
        }
    }

    public boolean checkProduct(Response response) {
        return isProduct().matches(response);
    }

    @Test
    @Step("Post product")
    public void postProductPositive() {
        setSpec();
        response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @Test
    @Step("Post product with price 1")
    public void postProductMinPrice() {
        product.setPrice(1);
        setSpec();
        response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @Test
    @Step("Post product with price 1_000_000")
    public void postProductLargePrice() {
        product.setPrice(1_000_000);
        setSpec();
        response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @Test
    @Step("Post product with price -500")
    public void postProductNegativePrice() {
        product.setPrice(-500);
        setSpec();
        response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertFalse(ProductDbAsserts.isProductExistDbByFieldsAssert(product, productsMapper));
    }

    @Test
    @Step("Post product with decimal price 100.25")
    public void postProductDecimalPrice(){
        product.setPrice(100.25);
        setSpec();
        response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
    }

    @Test
    @Step("Post product with decimal integer price 100.00")
    public void postProductDecimalPriceZero(){
        product.setPrice(100.00);
        setSpec();
        response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
    }

    @Test
    @Step("Post product with decimal price and 3 numbers 100.123")
    public void postProductDecimalPrice3(){
        product.setPrice(100.123);
        setSpec();
        response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
    }

    @Test
    @Step("Post product with price 0")
    public void postProductZeroPrice() {
        product.setPrice(0);
        setSpec();
        response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertFalse(ProductDbAsserts.isProductExistDbByFieldsAssert(product, productsMapper));
    }

    @Test
    @Step("Post product with null price")
    public void postProductNullPrice() {
        product.setPrice(null);
        setSpec();
        response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertFalse(ProductDbAsserts.isProductExistDbByFieldsAssert(product, productsMapper));
    }

    @Test
    @Step("Post product with 2 words title")
    public void postProductTitleWithSpaces() {
        String title = "Bean sprout";
        product.setTitle(title);
        setSpec();
        response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @Test
    @Step("Post product with title with trailing space")
    public void postProductTitleWithTrailingSpace() {
        String title = "Egg ";
        product.setTitle(title);
        setSpec();
        response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @Test
    @Step("Post product with title with leading space")
    public void postProductTitleWithLeadingSpace() {
        String title = " Egg";
        product.setTitle(title);
        setSpec();
        response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @Test
    @Step("Post product with title with empty space")
    public void postProductEmptyTitle() {
        String title = "";
        product.setTitle(title);
        setSpec();
        response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertFalse(ProductDbAsserts.isProductExistDbByFieldsAssert(product, productsMapper));
    }

    @Test
    @Step("Post product with null title")
    public void postProductNullTitle() {
        String title = null;
        product.setTitle(title);
        setSpec();
        response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertFalse(ProductDbAsserts.isProductExistDbByFieldsAssert(product, productsMapper));
    }

    @Test
    @Step("Post product with title consisting of numbers")
    public void postProductNumberTitle() {
        String title = "45";
        product.setTitle(title);
        setSpec();
        response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertFalse(ProductDbAsserts.isProductExistDbByFieldsAssert(product, productsMapper));
    }

    @Test
    @Step("Post product with invalid category")
    public void postProductInvalidCategory() {
        String categoryTitle = "Car";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertThat(categoryTitle, is(not(isCategoryExists())));
        assertFalse(ProductDbAsserts.isProductExistDbByFieldsAssert(product, productsMapper));
    }

    @Test
    @Step("Post product with category with leading space")
    public void postProductCategoryWithLeadingSpace() {
        String categoryTitle = " Food";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertThat(response.body().as(Product.class).getCategoryTitle(), isCategoryExists());
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @Test
    @Step("Post product with category with trailing space")
    public void postProductCategoryWithTrailingSpace() {
        String categoryTitle = "Food ";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        response = given(postProductRequestSpec, postProductResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertThat(response.body().as(Product.class).getCategoryTitle(), isCategoryExists());
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @Test
    @Step("Post product with category consisting of numbers")
    public void postProductNumberCategory() {
        String categoryTitle = "45";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertFalse(ProductDbAsserts.isProductExistDbByFieldsAssert(product, productsMapper));
    }

    @Test
    @Step("Post product with empty category")
    public void postProductEmptyCategory() {
        String categoryTitle = "";
        product.setCategoryTitle(categoryTitle);
        setSpec();
        response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertFalse(ProductDbAsserts.isProductExistDbByFieldsAssert(product, productsMapper));
    }

    @Test
    @Step("Post product with null category")
    public void postProductNullCategory() throws UnrecognizedPropertyException {
        String categoryTitle = null;
        product.setCategoryTitle(categoryTitle);
        setSpec();
        response = given(postProductRequestSpec, productFailResponseSpec)
                .post(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertFalse(ProductDbAsserts.isProductExistDbByFieldsAssert(product, productsMapper));
    }

    @AfterEach
    public void tearDown() {
        setIdForDelete();
        if (id != null) {
            productsMapper.deleteByPrimaryKey(id.longValue());
        }
    }
}
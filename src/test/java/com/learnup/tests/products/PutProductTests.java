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
import static com.learnup.enums.CategoryType.FOOD;
import static com.learnup.enums.CategoryType.FURNITURE;
import static io.restassured.RestAssured.given;

public class PutProductTests extends BaseTest {
    private Product product;
    private Product productPut;
    private Integer id;
    private RequestSpecification postProductRequestSpec;
    private ResponseSpecification postProductResponseSpec;
    private RequestSpecification putProductRequestSpec;
    private ResponseSpecification putProductResponseSpec;

    @BeforeEach
    public void setUp() throws IOException {
        Faker faker = new Faker();
        product = Product.builder()
                .price(300)
                .title(faker.food().dish())
                .categoryTitle(FOOD.getCategoryName())
                .build();

        postProductRequestSpec = getPostProductRequestSpec(product);
        postProductResponseSpec = getPostProductResponseSpec(product);
        postProduct();

        productPut = Product.builder()
                .id(id)
                .price(500)
                .title(faker.food().dish())
                .categoryTitle(FURNITURE.getCategoryName())
                .build();
    }

    public void setSpecPut() {
        putProductRequestSpec = getPostProductRequestSpec(productPut);
        putProductResponseSpec = getPostProductResponseSpec(productPut);
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
    public void putProductPositive() {
        setSpecPut();
        Response response = given(putProductRequestSpec, putProductResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
    }

    @Test
    public void putProductSameTitle() {
        productPut.setTitle(product.getTitle());
        setSpecPut();
        Response response = given(putProductRequestSpec, putProductResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
    }

    @Test
    public void putProductSamePrice() {
        productPut.setPrice(product.getPrice());
        setSpecPut();
        Response response = given(putProductRequestSpec, putProductResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
    }

    @Test
    public void putProductSameCategory() {
        productPut.setCategoryTitle(product.getCategoryTitle());
        setSpecPut();
        Response response = given(putProductRequestSpec, putProductResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
    }

    @Test
    public void putProductInvalidId() {
        productPut.setId(-1);
        setSpecPut();
        Response response = given(putProductRequestSpec, productFailResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
    }

    @Test
    public void putProductDeleteId() {
        tearDown();
        setSpecPut();
        Response response = given(putProductRequestSpec, productFailResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
        id = null;
    }

    @Test
    public void putProductSameProduct() {
        productPut = product;
        productPut.setId(id);
        setSpecPut();
        Response response = given(putProductRequestSpec, putProductResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
    }

    @AfterEach
    public void tearDown() {
        if (id != null) {
            given()
                    .response()
                    .spec(deleteProductResponseSpec)
                    .when()
                    .delete(PRODUCT_ENDPOINT_ID, id)
                    .prettyPeek()
                    .then()
                    .statusCode(200);
        }
    }
}

package com.learnup.tests.products;

import com.github.javafaker.Faker;
import com.learnup.dto.Product;
import com.learnup.tests.BaseTest;
import io.qameta.allure.*;
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
import static com.learnup.asserts.IsCategoryExists.isCategoryExists;
import static com.learnup.asserts.IsProductArray.isProductArray;
import static com.learnup.asserts.common.ProductDbAsserts.*;
import static com.learnup.enums.CategoryType.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Epic("Tests for products")
@Story("Get Product tests")
@Severity(SeverityLevel.BLOCKER)
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
        postProductResponseSpec = getPostProductResponseSpec(product);
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
    @Description("Получить все продукты")
    @Step("Get all products")
    public void getAllProducts() {
        Response response = when()
                .get(PRODUCT_ENDPOINT)
                .prettyPeek();
        List responseBody = response.body().as(List.class);
        assertThat(responseBody, isProductArray());
    }

    @Test
    @Description("Получить продукт по id")
    @Step("Get product")
    public void getProductPositive() {
        Response response =
                when()
                        .get(PRODUCT_ENDPOINT_ID, id)
                        .prettyPeek();
        Product responseBody = response.as(Product.class);
        assertThat(responseBody.getCategoryTitle(), isCategoryExists());
        assertThat(responseBody.getId(), equalTo(id));
        productDbAsserts(responseBody, productsMapper);
    }

    @Test
    @Description("Получить продукт по id, который удален или не создан")
    @Step("Get product that doesn't exist")
    public void getProductNonExist() {
        tearDown();
        Response response = given()
                .response()
                .spec(productFailResponseSpec)
                .when()
                .get(PRODUCT_ENDPOINT_ID, id)
                .prettyPeek();
        assertFalse(isProductExistDbByIdAssert(productsMapper, id));
        id = null;
    }

    @Test
    @Description("Получить продукт по отрицательному id")
    @Step("Get product with negative id")
    public void getProductNegativeId() {
        tearDown();
        id = -id;
        Response response = given()
                .response()
                .spec(productFailResponseSpec)
                .when()
                .get(PRODUCT_ENDPOINT_ID, -id)
                .prettyPeek();
        assertFalse(isProductExistDbByIdAssert(productsMapper, id));
    }

    @Test
    @Description("Получить продукт по строковому id")
    @Step("Get product with string id")
    public void getProductStringId() {
        Response response = given()
                .response()
                .spec(productFailResponseSpec)
                .when()
                .get(PRODUCT_ENDPOINT_ID, "seven")
                .prettyPeek();
    }

    @Test
    @Description("Получить продукт по id равному null")
    @Step("Get product with null id")
    public void getProductStringNullId() {
        tearDown();
        id = null;
        Response response = given()
                .response()
                .spec(productFailResponseSpec)
                .when()
                .get(PRODUCT_ENDPOINT_ID, "null")
                .prettyPeek();
        assertFalse(isProductExistDbByIdAssert(productsMapper, id));
    }

    @AfterEach
    public void tearDown() {
        if (id != null) {
            productsMapper.deleteByPrimaryKey(id.longValue());
        }
    }
}

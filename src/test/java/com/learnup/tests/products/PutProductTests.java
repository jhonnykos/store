package com.learnup.tests.products;

import com.github.javafaker.Faker;
import com.learnup.asserts.common.ProductDbAsserts;
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

import static com.learnup.Endpoints.PRODUCT_ENDPOINT;
import static com.learnup.Endpoints.PRODUCT_ENDPOINT_ID;
import static com.learnup.asserts.common.ProductDbAsserts.productDbAsserts;
import static com.learnup.enums.CategoryType.FOOD;
import static com.learnup.enums.CategoryType.FURNITURE;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Epic("Tests for products")
@Story("Put Product tests")
@Severity(SeverityLevel.NORMAL)
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
    @Step("Put product")
    public void putProductPositive() {
        setSpecPut();
        Response response = given(putProductRequestSpec, putProductResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @Test
    @Step("Put product where title is the same")
    public void putProductSameTitle() {
        productPut.setTitle(product.getTitle());
        setSpecPut();
        Response response = given(putProductRequestSpec, putProductResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @Test
    @Step("Put product where price is the same")
    public void putProductSamePrice() {
        productPut.setPrice(product.getPrice());
        setSpecPut();
        Response response = given(putProductRequestSpec, putProductResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @Test
    @Step("Put product where category is the same")
    public void putProductSameCategory() {
        productPut.setCategoryTitle(product.getCategoryTitle());
        setSpecPut();
        Response response = given(putProductRequestSpec, putProductResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @Test
    @Step("Put product with invalid id")
    public void putProductInvalidId() {
        productPut.setId(-1);
        setSpecPut();
        Response response = given(putProductRequestSpec, productFailResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertFalse(ProductDbAsserts.isProductExistDbByFieldsAssert(productPut, productsMapper));
    }

    @Test
    @Step("Put product with id that was deleted")
    public void putProductDeleteId() {
        tearDown();
        setSpecPut();
        Response response = given(putProductRequestSpec, productFailResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
        assertFalse(ProductDbAsserts.isProductExistDbByFieldsAssert(productPut, productsMapper));
        id = null;
    }

    @Test
    @Step("Put product where all fields is the same")
    public void putProductSameProduct() {
        productPut = product;
        productPut.setId(id);
        setSpecPut();
        Response response = given(putProductRequestSpec, putProductResponseSpec)
                .put(PRODUCT_ENDPOINT)
                .prettyPeek();
        productDbAsserts(response.as(Product.class), productsMapper);
    }

    @AfterEach
    public void tearDown() {
        if (id != null) {
            productsMapper.deleteByPrimaryKey(id.longValue());
        }
    }
}

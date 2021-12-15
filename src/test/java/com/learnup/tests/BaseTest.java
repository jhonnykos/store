package com.learnup.tests;

import com.learnup.asserts.IsStatusCodeFail;
import com.learnup.asserts.IsStatusCodeSuccess;
import com.learnup.dto.Product;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.learnup.asserts.IsProduct.isProduct;
import static com.learnup.asserts.IsStatusCodeFail.*;
import static com.learnup.asserts.IsStatusCodeSuccess.*;
import static com.learnup.fields.ProductFields.*;
import static io.restassured.filter.log.LogDetail.*;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.Method.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.matchesRegex;

public abstract class BaseTest {
    public static Properties properties = new Properties();
    public static RequestSpecification logRequestSpecification;
    public static ResponseSpecification responseSpecification;
    public static ResponseSpecification deleteProductResponseSpec;
    public static ResponseSpecification categoriesResponseSpec;
    public static ResponseSpecification productFailResponseSpec;

    @SneakyThrows
    @BeforeAll
    public static void beforeAll() {
        properties.load(new FileInputStream("src/test/resources/application.properties"));
        RestAssured.baseURI = properties.getProperty("baseUri");

        logRequestSpecification = new RequestSpecBuilder()
                .log(METHOD)
                .log(URI)
                .log(BODY)
                .build();
        RestAssured.requestSpecification = logRequestSpecification;

        responseSpecification = new ResponseSpecBuilder()
                .log(ALL)
                .expectResponseTime(lessThan(2000l), TimeUnit.MILLISECONDS)
                .build();
        RestAssured.responseSpecification = responseSpecification;

        deleteProductResponseSpec = new ResponseSpecBuilder()
                .expectContentType("")
                .expectStatusCode(200)
                .build();

        categoriesResponseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .expectResponseTime(lessThan(2000l), TimeUnit.MILLISECONDS)
                .expectStatusLine(containsString("HTTP/1.1 200"))
                .build();

        productFailResponseSpec = new ResponseSpecBuilder()
                .expectBody(is(not(isProduct())))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(isStatusCodeFail())
                .expectStatusLine(containsString("HTTP/1.1 4"))
                .build();
    }

    public static RequestSpecification getPostProductRequestSpec(Product product) {
        return new RequestSpecBuilder()
                .setBody(product)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification getPostProductResponseSpec(Product product) {
        String title = product.getTitle();
        String categoryTitle = product.getCategoryTitle();

        if (title != null) {
            title = title.trim();
        }
        if (categoryTitle != null) {
            categoryTitle = categoryTitle.trim();
        }

        return new ResponseSpecBuilder()
                .expectStatusCode(isStatusCodeSuccess())
                .expectStatusLine(containsString("HTTP/1.1 2"))
                .expectBody(ID, is(not(nullValue())))
                .expectBody(TITLE, equalTo(title))
                .expectBody(PRICE, equalTo(product.getPrice()))
                .expectBody(CATEGORY, equalTo(categoryTitle))
                .build();
    }
}

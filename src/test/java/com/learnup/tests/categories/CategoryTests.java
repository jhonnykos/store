package com.learnup.tests.categories;

import com.learnup.dto.Category;
import com.learnup.tests.BaseTest;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.learnup.Endpoints.CATEGORY_ENDPOINT;
import static com.learnup.enums.CategoryType.FOOD;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CategoryTests extends BaseTest {

    @BeforeAll
    public static void setUp() throws IOException {

    }

    @Test
    public void getCategory(){
        ValidatableResponse validatableResponse =
                when()
                .get(CATEGORY_ENDPOINT,FOOD.getId())
                .prettyPeek()
                .then()
                .body("id", equalTo(FOOD.getId()));
//        assertThat(validatableResponse.body("id", equalTo(3)), is(true));
    }

    @Test
    public void getCategoryResponse(){
        given()
                .response()
                .spec(categoriesResponseSpec)
                .when()
                .get(CATEGORY_ENDPOINT,FOOD.getId());
    }

    @Test
    public void getProductResponse(){
        Response response =
               when()
                .get("/products")
                .prettyPeek();
//        assertThat(response.statusCode(),equalTo(200));
//        assertThat(response.body().jsonPath().get("products[0].categoryTitle"), equalTo("Food"));
//        assertThat(validatableResponse.body("id", equalTo(3)), is(true));
    }

    @Test
    public void getCategoryWithAsserts(){
        Response response =
                when()
                .get(CATEGORY_ENDPOINT, FOOD.getId())
                .prettyPeek();
        Category responseBody = response.body().as(Category.class);
        assertThat(responseBody.getProducts().get(0).getCategoryTitle(), equalTo("Food"));
//      assertThat(validatableResponse.body("id", equalTo(3)), is(true));
    }

    @Test
    public void getCategoryWithAssertsAfterTests() {
        Category response =
                when()
                .get(CATEGORY_ENDPOINT, FOOD.getId())
                .prettyPeek()
                .body()
                .as(Category.class);

        response.getProducts().forEach(
                e -> assertThat(e.getCategoryTitle(), equalTo(FOOD.getCategoryName()))
        );
    }
}

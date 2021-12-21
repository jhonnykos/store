package com.learnup.tests.categories;

import com.learnup.dto.Category;
import com.learnup.enums.CategoryType;
import com.learnup.tests.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.learnup.Endpoints.CATEGORY_ENDPOINT;
import static com.learnup.asserts.IsCategoryExists.isCategoryExists;
import static com.learnup.enums.CategoryType.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Tests for categories")
@Story("Get Category tests")
@Severity(SeverityLevel.MINOR)
public class CategoryTests extends BaseTest {

    @Test
    @Description("Получить категорию Food")
    @Step("Get category")
    public void getCategory() {
        ValidatableResponse validatableResponse =
                when()
                        .get(CATEGORY_ENDPOINT, FOOD.getId())
                        .prettyPeek()
                        .then()
                        .body("id", equalTo(FOOD.getId()));
    }

    @Test
    @Description("Получить категорию Food с помощью спецификации")
    @Step("Get category")
    public void getCategoryResponse() {
        given()
                .response()
                .spec(categoriesResponseSpec)
                .when()
                .get(CATEGORY_ENDPOINT, FOOD.getId());
    }

    @Test
    @Description("Получить категорию Food и применить assertThat")
    @Step("Get category")
    public void getCategoryWithAsserts() {
        Response response =
                when()
                        .get(CATEGORY_ENDPOINT, FOOD.getId())
                        .prettyPeek();
        Category responseBody = response.body().as(Category.class);
        assertThat(responseBody.getProducts().get(0).getCategoryTitle(), equalTo(FOOD.getCategoryName()));
        assertThat(responseBody.getId(), equalTo(FOOD.getId()));
    }

    @Test
    @Description("Получить категорию Food и применить as")
    @Step("Get category")
    public void getCategoryWithAssertsAfterTests() {
        Category response =
                when()
                        .get(CATEGORY_ENDPOINT, FOOD.getId())
                        .prettyPeek()
                        .body()
                        .as(Category.class);

        response.getProducts().forEach(
                e -> {
                    assertThat(e.getCategoryTitle(), isCategoryExists());
                    assertThat(e.getCategoryTitle(), equalTo(FOOD.getCategoryName()));
                }
        );
    }
}

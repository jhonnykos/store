package com.learnup.asserts;

import com.learnup.dto.Product;
import io.qameta.allure.Step;
import lombok.experimental.UtilityClass;

import static com.learnup.asserts.IsCategoryExists.isCategoryExists;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@UtilityClass
public class CommonAsserts {

    @Step
    public Integer postProductPositiveAsserts(Product product, Product responseBody){
        Integer id = responseBody.getId();
        assertThat(id, is(not(nullValue())));
        assertThat(responseBody.getCategoryTitle(), isCategoryExists());
        assertThat(responseBody.getTitle(), equalTo(product.getTitle()));
        assertThat(responseBody.getPrice(), equalTo(product.getPrice()));
        assertThat(responseBody.getCategoryTitle(), equalTo(product.getCategoryTitle()));
        return id;
    }
}

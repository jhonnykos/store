package com.learnup.asserts.common;

import com.learnup.db.dao.ProductsMapper;
import com.learnup.db.model.Products;
import com.learnup.db.model.ProductsExample;
import com.learnup.dto.Product;
import com.learnup.enums.CategoryType;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.learnup.enums.CategoryType.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@UtilityClass
public class ProductDbAsserts {
    @Step
    public void productDbAsserts(Product responseBody, ProductsMapper productsMapper) {
        Products products = productsMapper.selectByPrimaryKey(responseBody.getId().longValue());
        assertThat(responseBody.getTitle(), equalTo(products.getTitle()));
        assertThat(responseBody.getPrice(), equalTo(products.getPrice()));
        assertThat(findByCategoryTitle(responseBody.getCategoryTitle()).getId(), equalTo(products.getCategory_id().intValue()));
    }

    @Step
    public boolean isProductExistDbByIdAssert(ProductsMapper productsMapper, Integer id) {
        if (id == null) {
            return (productsMapper.selectByPrimaryKey(null) != null);
        }
        return (productsMapper.selectByPrimaryKey(id.longValue()) != null);
    }

    @Step
    public boolean isProductExistDbByFieldsAssert(Product product, ProductsMapper productsMapper) {
        ProductsExample productsExample = new ProductsExample();
        productsExample.createCriteria()
                .andCategory_idEqualTo((long) findByCategoryTitle(product.getCategoryTitle()).getId())
                .andPriceEqualTo((Integer) product.getPrice())
                .andTitleEqualTo(product.getTitle());
        List<Products> products = productsMapper.selectByExample(productsExample);
        return products.size() != 0;
//        if (id == null) {
//            return (productsMapper.selectByPrimaryKey(null) != null);
//        }
//        return (productsMapper.selectByPrimaryKey(id.longValue()) != null);
    }
}
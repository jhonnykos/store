package com.learnup.asserts;

import com.learnup.dto.Product;
import com.learnup.enums.CategoryType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.NoArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

@NoArgsConstructor
public class IsProduct extends TypeSafeMatcher<ResponseBody> {
    public static Matcher<ResponseBody> isProduct() {
        return new IsProduct();
    }
    @Override
    protected boolean matchesSafely(ResponseBody responseBody) {
        try{
            responseBody.as(Product.class);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("It is not product!");
    }

}
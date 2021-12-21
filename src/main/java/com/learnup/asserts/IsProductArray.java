package com.learnup.asserts;

import com.learnup.dto.Product;
import com.learnup.enums.CategoryType;
import com.learnup.fields.ProductFields;
import lombok.NoArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashMap;
import java.util.List;

import static com.learnup.fields.ProductFields.*;

@NoArgsConstructor
public class IsProductArray extends TypeSafeMatcher<List> {

    public static Matcher<List> isProductArray() {
        return new IsProductArray();
    }
    @Override
    protected boolean matchesSafely(List products) {
        try {
            if(products.size() == 0) {
                return true;
            }
            products.forEach(
                    p -> {
                        HashMap<String,?> pMap = (HashMap<String, ?>) p;
                        pMap.get(ID);
                        pMap.get(TITLE);
                        pMap.get(PRICE);
                        pMap.get(CATEGORY);
                    }
            );
            return true;
        }
        catch (IllegalArgumentException | ClassCastException e){
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Not List<Product> type");
    }
}

package com.learnup.asserts;

import com.learnup.dto.Product;
import com.learnup.enums.CategoryType;
import lombok.NoArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
@NoArgsConstructor
public class IsCategoryExists extends TypeSafeMatcher<String> {
    public static Matcher<String> isCategoryExists() {
        return new IsCategoryExists();
    }
    @Override
    protected boolean matchesSafely(String actualCategoryTitle) {
        return CategoryType.findByCategoryTitle(actualCategoryTitle) != null;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("No such category in our dictionary");
    }

}

package com.learnup.asserts;

import lombok.NoArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class IsStatusCodeFail extends TypeSafeMatcher<Integer> {
    public static Matcher<Integer> isStatusCodeFail() {
        return new IsStatusCodeFail();
    }
    @Override
    protected boolean matchesSafely(Integer statusCode) {
        return Pattern.matches("4\\d{2}",Integer.toString(statusCode));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Status code is not fail!");
    }

}
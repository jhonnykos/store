package com.learnup.asserts;

import lombok.NoArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class IsStatusCodeSuccess extends TypeSafeMatcher<Integer> {
    public static Matcher<Integer> isStatusCodeSuccess() {
        return new IsStatusCodeSuccess();
    }
    @Override
    protected boolean matchesSafely(Integer statusCode) {
        return Pattern.matches("2\\d{2}",Integer.toString(statusCode));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Status code is not success!");
    }

}
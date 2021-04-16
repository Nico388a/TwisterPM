package com.example.twisterpm;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Rule;

import static org.junit.Assert.*;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainMessageActivity.class);

    @Test
    public void UITest() {
        onView(withText("Write a message")).check(matches(isDisplayed()));
        onView(withId(R.id.mainMessageEditTextSendMessage)).perform(typeText("nicolai"));
        onView(withText("Add message")).check(matches(isDisplayed()));
        onView(withId(R.id.ContentMainMessageAddButton)).perform(click());
//        onView(withId(R.id.MainMessageActivityRecycler)).check(matches(withText("[nicolai]")));

//        onView(withId(R.id.mainMessageEditTextSendMessage)).perform(typeText("nicolaiuitest"));
//        onView(withId(R.id.ContentMainMessageAddButton)).perform(click());
//        onView(withId(R.id.ContentMainMessageTextView)).check(matches(withText("[nicolai, nicolaiuitest]")));
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.twisterpm", appContext.getPackageName());
    }
}
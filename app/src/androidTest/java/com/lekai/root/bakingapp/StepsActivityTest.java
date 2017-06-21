package com.lekai.root.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by oluwalekefakorede on 21/06/2017.
 */
@RunWith(AndroidJUnit4.class)
public class StepsActivityTest {

    private static final String INTRODUCTION = "Recipe Introduction" ;
    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> stepsActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource(){
        mIdlingResource = stepsActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void performItemClick(){
        onView(withId(R.id.recipe_recycler_view)).perform(actionOnItemAtPosition(1, click()));

        onView(withId(R.id.steps_recyclerview)).perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.txt_instruction)).check(matches(withText(INTRODUCTION)));
    }

    @After
    public void unRegisterIdlingResource(){
        if(mIdlingResource != null){
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}

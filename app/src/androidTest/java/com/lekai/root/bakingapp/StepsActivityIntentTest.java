package com.lekai.root.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Created by oluwalekefakorede on 21/06/2017.
 */
@RunWith(AndroidJUnit4.class)
public class StepsActivityIntentTest {

    private static final String STEP_EXTRA = "step";
    private IdlingResource mIdlingResource;
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(
            MainActivity.class);
    @Before
    public void registerIdlingResource() {
        mIdlingResource = intentsTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);

        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }
    /**
     * Clicks on a RecyclerViewItem and checks if it has intent with extra with the key STEP_EXTRA
     */
    @Test
    public void clickRecyclerViewItemHasIntentWithAKey() {
        onView(withId(R.id.recipe_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        //Check to make sure the device is not a large screen device so as to check the intent
        if (onView(withId(R.id.recipe_container)) == null) {
            // Click on the Step List RecyclerView item at position 0
            onView(withId(R.id.steps_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            //Checks if the key is present
            intended(hasExtraWithKey(STEP_EXTRA));
        }
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}

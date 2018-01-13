package edu.upc.pes.agora;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.pes.agora.Presentation.CreateProposalActivity;
import edu.upc.pes.agora.Presentation.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Oriol on 10/1/2018.
 */

@RunWith(AndroidJUnit4.class)
public class CCreateProposalScreenTest {

    @Rule
    public ActivityTestRule<CreateProposalActivity> mProposalsActivityTestRule =
            new ActivityTestRule(CreateProposalActivity.class);

    @Test
    public void clickCreateButton_createsProposal() throws Exception {
        Intents.init();

        onView(withId(R.id.titulo)).perform(replaceText("Create Proposal Test"));
        onView(withId(R.id.descripcion)).perform(replaceText("C Test"));
        onView(withId(R.id.cate)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Culture"))).perform(click());

        onView(withId(R.id.createButton)).perform(closeSoftKeyboard(), click());
        onView(withId(R.id.createButton)).perform(closeSoftKeyboard(), click());

        //onView(withId(R.id.swipelayout)).perform(swipeUp());
        //onView(withId(R.id.swipelayout)).perform(swipeUp());
        //onView(withText("Create Proposal Test")).check(matches(isDisplayed()));

        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void clickCancelButton_createsProposal() throws Exception {
        onView(withId(R.id.titulo)).perform(replaceText("Create Proposal Test"));
        onView(withId(R.id.descripcion)).perform(replaceText("C Test"));
        onView(withId(R.id.cate)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Culture"))).perform(click());

        onView(withId(R.id.resetButton)).perform(closeSoftKeyboard(), click());
        onView(withId(R.id.resetButton)).perform(closeSoftKeyboard(), click());

        onView(withId(R.id.titulo)).check(matches(withText("")));
        onView(withId(R.id.descripcion)).check(matches(withText("")));
        //onView(withId(R.id.cate)).check(matches(withText("Select a category")));
    }

}
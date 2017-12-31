package edu.upc.pes.agora;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.pes.agora.Presentation.CreateProposalActivity;
import edu.upc.pes.agora.Presentation.MainActivity;
import edu.upc.pes.agora.Presentation.MyProposalsActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
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
 * Created by Jaume on 31/12/2017.
 */

@RunWith(AndroidJUnit4.class)
public class TMyProposalsScreenTest {

    @Rule
    public ActivityTestRule<MyProposalsActivity> mMyProposalsActivityTestRule =
            new ActivityTestRule(MyProposalsActivity.class);

    @Test
    public void clickCreateButton_createsProposal() throws Exception {
        onView(withId(R.id.recyclerView)).perform(swipeUp());
        onView(withId(R.id.recyclerView)).perform(swipeUp());
        onView(withText("DotzeMil")).check(matches(isDisplayed()));
    }

}

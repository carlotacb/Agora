package edu.upc.pes.agora;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.AdapterView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.pes.agora.Presentation.CreateProposalActivity;
import edu.upc.pes.agora.Presentation.DetailsProposalActivity;
import edu.upc.pes.agora.Presentation.MainActivity;
<<<<<<< HEAD
=======
import edu.upc.pes.agora.Presentation.CreateProposalActivity;
>>>>>>> develop

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Oriol on 26/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ProposalsScreenTest {

    @Rule
    public ActivityTestRule<CreateProposalActivity> mProposalsActivityTestRule =
            new ActivityTestRule(CreateProposalActivity.class);

    @Test
    public void clickCreateButton_createsProposal() throws Exception {
        Intents.init();
        onView(withId(R.id.titulo)).perform(replaceText("DotzeMil"));
        onView(withId(R.id.descripcion)).perform(replaceText("Torneig de futbol sala per recaptar fons per la Marató de TV3"));
        onView(withId(R.id.cate)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Culture"))).perform(click());
        onView(withId(R.id.createButton)).perform(closeSoftKeyboard(), click());
        onView(withId(R.id.createButton)).perform(closeSoftKeyboard(), click());
        onView(withId(R.id.swipelayout)).perform(swipeUp());
        onView(withId(R.id.swipelayout)).perform(swipeUp());
        onView(withText("DotzeMil")).check(matches(isDisplayed()));

        //ViewInteraction appCompatButton2 = onView(allOf(withId(R.id.btnLernMore), withParent(withText("Vuitcents"))));
        //appCompatButton2.perform(click());

        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void clickCancelButton_createsProposal() throws Exception {
        onView(withId(R.id.titulo)).perform(replaceText("DeuMil"));
        onView(withId(R.id.descripcion)).perform(replaceText("Torneig de futbol sala per recaptar fons per la Marató de TV3"));
        onView(withId(R.id.cate)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Culture"))).perform(click());

        onView(withId(R.id.resetButton)).perform(closeSoftKeyboard(), click());
        onView(withId(R.id.resetButton)).perform(closeSoftKeyboard(), click());

        onView(withId(R.id.titulo)).check(matches(withText("")));
        onView(withId(R.id.descripcion)).check(matches(withText("")));
        //onView(withId(R.id.cate)).check(matches(withText("Select a category")));
    }

}

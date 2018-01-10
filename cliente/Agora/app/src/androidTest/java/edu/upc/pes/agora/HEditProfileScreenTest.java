package edu.upc.pes.agora;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.pes.agora.Presentation.EditProfileActivity;
import edu.upc.pes.agora.Presentation.MyProfileActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Jaume on 10/1/2018.
 */

@RunWith(AndroidJUnit4.class)
public class HEditProfileScreenTest {

    //private String mNameProfileToBetyped;
    //private String mPostalCodeToBetyped;
    //private String mNeighborhoodToBetyped;
    //private String mDateToBetyped;

    @Rule
    public ActivityTestRule<EditProfileActivity> mEditProfileActivityTestRule =
            new ActivityTestRule(EditProfileActivity.class);

    /*@Before
    void initValidString() {
        mNameProfileToBetyped = "Oriol Saborido Orús";
        mPostalCodeToBetyped = "08201";
        mNeighborhoodToBetyped = "Centre";
        mDateToBetyped = "19/04/1994";
    }*/

    @Test
    public void editInformation_and_clickSave_editsProfile() throws Exception {
        Intents.init();

        onView(withId(R.id.nameprofile)).perform(replaceText("Test"));
        onView(withId(R.id.codipostal)).perform(closeSoftKeyboard(), replaceText("00000"));
        onView(withId(R.id.descript)).perform(closeSoftKeyboard(), replaceText("Testing"));
        onView(withId(R.id.fecha)).perform(closeSoftKeyboard(), replaceText("01/01/1970"));
        onView(withId(R.id.sexo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undefined"))).perform(click());

        onView(withId(R.id.swipelayout)).perform(swipeUp());
        onView(withId(R.id.aceptar)).perform(closeSoftKeyboard(), click());
        onView(withId(R.id.aceptar)).perform(closeSoftKeyboard(), click());

        onView(withId(R.id.nameprofile)).check(matches(withText("Test")));
        onView(withId(R.id.codipostal)).check(matches(withText("0")));
        onView(withId(R.id.description)).check(matches(withText("Testing")));
        onView(withId(R.id.born)).check(matches(withText("01/01/1970")));
        onView(withId(R.id.sexo)).check(matches(withText("Undefined")));

        intended(hasComponent(MyProfileActivity.class.getName()));
        intended(hasComponent(MyProfileActivity.class.getName()));

        Intents.release();
    }

    @Test
    public void editInformation_and_clickCancel_doesnteditProfile() throws Exception {
        Intents.init();

        onView(withId(R.id.nameprofile)).perform(replaceText(""));
        onView(withId(R.id.codipostal)).perform(closeSoftKeyboard(), replaceText(""));
        onView(withId(R.id.descript)).perform(closeSoftKeyboard(), replaceText("Testing"));
        onView(withId(R.id.fecha)).perform(closeSoftKeyboard(), replaceText(""));
        onView(withId(R.id.sexo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undefined"))).perform(click());

        onView(withId(R.id.swipelayout)).perform(swipeUp());
        onView(withId(R.id.cancelar)).perform(closeSoftKeyboard(), click());
        onView(withId(R.id.cancelar)).perform(closeSoftKeyboard(), click());

        onView(withId(R.id.nameprofile)).check(matches(withText("Test")));
        onView(withId(R.id.codipostal)).check(matches(withText("0")));
        onView(withId(R.id.description)).check(matches(withText("Testing")));
        onView(withId(R.id.born)).check(matches(withText("01/01/1970")));
        onView(withId(R.id.sexo)).check(matches(withText("Undefined")));

        intended(hasComponent(MyProfileActivity.class.getName()));

        Intents.release();
    }

}
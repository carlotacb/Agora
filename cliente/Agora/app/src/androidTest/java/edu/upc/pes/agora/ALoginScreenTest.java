package edu.upc.pes.agora;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.pes.agora.Presentation.LoginActivity;
import edu.upc.pes.agora.Presentation.RegisterActivity;
import edu.upc.pes.agora.Presentation.MainActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/**
 * Created by Jaume on 10/1/2018.
 */

@RunWith(AndroidJUnit4.class)
public class ALoginScreenTest {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityTestRule =
            new ActivityTestRule(LoginActivity.class);

    @Test
    public void clickRegisterButton_showsRegisterActivity() throws Exception {
        Intents.init();
        onView(withId(R.id.btnRegister)).perform(scrollTo(), click());
        onView(withId(R.id.btnRegister)).perform(scrollTo(), click());
        //onView(withId(R.id.btnRegister)).perform(closeSoftKeyboard(), click());
        //onView(withId(R.id.btnRegister)).perform(closeSoftKeyboard(), click());
        intended(hasComponent(RegisterActivity.class.getName()));
        Intents.release();
        //onView(withId(R.id.identifier)).check(matches(isDisplayed()));
    }

    @Test
    public void clickLoginButton_loginsUser() throws Exception {
        Intents.init();
        onView(withId(R.id.username)).perform(typeText("Test"));
        onView(withId(R.id.password)).perform(typeText("test"));
        onView(withId(R.id.btnLogin)).perform(closeSoftKeyboard(), click());
        onView(withId(R.id.btnLogin)).perform(closeSoftKeyboard(), click());
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
        //onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
    }

}
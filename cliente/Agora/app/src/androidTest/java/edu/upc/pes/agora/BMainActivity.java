package edu.upc.pes.agora;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.pes.agora.Presentation.CreateProposalActivity;
import edu.upc.pes.agora.Presentation.LoginActivity;
import edu.upc.pes.agora.Presentation.MainActivity;
import edu.upc.pes.agora.Presentation.MyProfileActivity;
import edu.upc.pes.agora.Presentation.MyProposalsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

/**
 * Created by Oriol on 30/12/2017.
 */

@RunWith(AndroidJUnit4.class)
public class BMainActivity {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule(MainActivity.class);

    @Test
    public void clickCreateProposalButton_showsCreateProposalsActivity() throws Exception {
        Intents.init();
        onView(withId(R.id.fab)).perform(closeSoftKeyboard(), click());
        intended(hasComponent(CreateProposalActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void clickOnHomeItem_ShowsMainActivity() {
        Intents.init();
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_myporposals));

        intended(hasComponent(MyProposalsActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void clickOnMyProposalsItem_ShowsMyProposalsActivity() {
        Intents.init();
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_myporposals));

        intended(hasComponent(MyProposalsActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void clickOnMyProfileItem_ShowsMyProfileActivity() {
        Intents.init();
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_perfilprinc));

        intended(hasComponent(MyProfileActivity.class.getName()));
        Intents.release();
    }

}

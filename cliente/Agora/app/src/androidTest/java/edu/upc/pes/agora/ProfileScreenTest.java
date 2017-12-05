package edu.upc.pes.agora;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.pes.agora.Presentation.EditProfileActivity;
import edu.upc.pes.agora.Presentation.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Oriol on 26/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ProfileScreenTest {

    @Rule
    public ActivityTestRule<ProfileActivity> mProfileActivityTestRule =
            new ActivityTestRule(ProfileActivity.class);

    @Test
    public void clickEditProfileButton_showsEditProfileActivity() throws Exception {
        Intents.init();
        onView(withId(R.id.editarperfil)).perform(click());
        intended(hasComponent(EditProfileActivity.class.getName()));
        Intents.release();
    }

}

package edu.upc.pes.agora;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.pes.agora.Presentation.MainActivity;
import edu.upc.pes.agora.Presentation.ProposalsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Oriol on 26/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ProposalsScreenTest {

    @Rule
    public ActivityTestRule<ProposalsActivity> mProposalsActivityTestRule =
            new ActivityTestRule(ProposalsActivity.class);

    @Test
    public void clickCreateButton_createsProposal() throws Exception {
        Intents.init();
        onView(withId(R.id.titulo)).perform(replaceText("Torneig de futbol sala solidari"));
        onView(withId(R.id.descripcion)).perform(replaceText("Torneig de futbol sala per recaptar fons per la Marató de TV3"));
        onView(withId(R.id.createButton)).perform(closeSoftKeyboard(), click());
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

}

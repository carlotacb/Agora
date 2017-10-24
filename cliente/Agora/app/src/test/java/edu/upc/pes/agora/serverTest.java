package edu.upc.pes.agora;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class serverTest extends ActivityUnitTestCase<RegisterActivity> {

    String userName = "u";
    String pass = "pass";
    private Intent _startIntent;
   // private CountDownLatch lock = new CountDownLatch(1);

    //public serverTest(Class<RegisterActivity> activityClass) { super(activityClass); }

    public serverTest() { super(RegisterActivity.class); }

    protected void setUp() throws Exception {
        super.setUp();
        _startIntent = new Intent(Intent.ACTION_MAIN);
    }

    // amb aixo creo un usuari, sha de canviar el "u"
    public void ltest() throws IOException {
        String id = "4773431860";
    //    boolean b = (new HttpHelper()).verifyData(id,userName,pass,pass);
     //   assertTrue(b);
    }

    public  void test1 (){
        assertTrue(false);
    }

    public void testRegister_invalidId() throws InterruptedException, ExecutionException {
        startActivity(_startIntent,null,null);

        getActivity().identifier.setText("1232");
        getActivity().username.setText("user");
        getActivity().password1.setText("123");
        getActivity().password2.setText("123");
        getActivity()._registered = true;
        getActivity().register();

        //lock.await(16000, TimeUnit.MILLISECONDS);
        getActivity()._postAsyncTask.get();
        Boolean b = getActivity()._registered;

        assertFalse(b);
        assertTrue(b);
    }
/*
    @Test
    public void loginTest_correcte() {

        boolean b = (new HttpHelper()).verifyLogin(userName,pass);
        assertTrue(b);
    }

    @Test
    public void loginTest_incorrecte() {
        String passIncorrect = "passIncorrect";
        boolean b = (new HttpHelper()).verifyLogin(userName,passIncorrect);
        assertFalse(b);
    }
    */
}
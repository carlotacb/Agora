package edu.upc.pes.agora;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class serverTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    String userName = "u";
    String pass = "pass";

    // amb aixo creo un usuari, sha de canviar el "u"
    public void test1() throws IOException {
        String id = "4773431860";
        boolean b = HttpHelper.verifyData(id,userName,pass,pass);
        assertTrue(b);
    }

    @Test
    public void registerTest_invalidId(){
        String id = "incorrecte";
        boolean b = HttpHelper.verifyData(id,userName,pass,pass);
        assertFalse(b);
    }

    @Test
    public void loginTest_correcte() {
      //  boolean b = HttpHelper.login(userName,pass);//
       // assertTrue(b);
    }

    @Test
    public void loginTest_incorrecte() {
        String passIncorrect = "passIncorrect";
      //  boolean b = HttpHelper.login(userName,passIncorrect);
       // assertFalse(b);
    }
}
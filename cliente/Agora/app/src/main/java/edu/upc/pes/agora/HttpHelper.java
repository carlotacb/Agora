package edu.upc.pes.agora;


import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerar on 20/10/2017.
 */

public class HttpHelper {

    /**
     * Verifies if entered data is correct.
     *
     * @param i  entered ID
     * @param u  entered username
     * @param p1 entered password 1
     * @param p2 entered password 2
     * @return true if server verifies data successfully, false otherwise
     * @throws IOException
     */
    public boolean verifyData(String i, String u, String p1, String p2) {

        try {

            URL server = new URL("http://sandshrew.fib.upc.es:3000/api/signup");
            HttpURLConnection client = null;

            client = (HttpURLConnection) server.openConnection();
            client.setReadTimeout(15000);
            client.setConnectTimeout(15000);
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            client.setRequestProperty("Accept", "application/json");
            client.setDoInput(true);
            client.setDoOutput(true);

            JSONObject data = new JSONObject();
            data.put("signupCode", i);
            data.put("username", u);
            data.put("password", p1);
            data.put("confirmPassword", p2);

            Log.i("JSON", data.toString());

            DataOutputStream os = new DataOutputStream(client.getOutputStream());
            os.writeBytes(data.toString());
            os.flush();
            os.close();

            int responseCode = client.getResponseCode();
            Log.i("STATUS", String.valueOf(responseCode));
            Log.i("MSG", client.getResponseMessage());
       /*     String line = "";
            StringBuilder responseOutput = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while((line = br.readLine()) != null ) {
                responseOutput.append(line);
            }
            String result = responseOutput.toString();
            br.close();
*/
            client.disconnect();

            return (responseCode == 200);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
/*
*  // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
                        params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    // save user data
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(Login.this);
                    Editor edit = sp.edit();
                    edit.putString("username", username);
                    edit.commit();

                    Intent i = new Intent(Login.this, ReadComments.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
*
*
* */

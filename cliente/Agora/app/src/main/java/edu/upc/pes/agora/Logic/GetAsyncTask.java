package edu.upc.pes.agora.Logic;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import edu.upc.pes.agora.R;

public class GetAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {
    private URL url;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public GetAsyncTask(String url2, Context coming_context) {
        try {
            url = new URL(url2);
            context = coming_context;
        } catch (MalformedURLException e) {
            Log.v("TagMatchGetAsyncTask", "", e);
        }
    }

    protected JSONObject doInBackground(final JSONObject... params) {

        try {
            /*final String user = params[0].getString("username");
            final String password = params[0].getString("password");

            String userPass = user + ":" + password;

            String basicAuth;
            if (url.getHost().contains("heroku")) {
                basicAuth = "Basic " + new String(Base64.encode(userPass.getBytes(), Base64.NO_WRAP));*/

            HttpsURLConnection client = (HttpsURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setInstanceFollowRedirects(true);
            client.connect();

            JSONObject aux;
            String response = Helpers.iStreamToString(client.getInputStream());

            if (client.getResponseCode() >= 400){
                aux = new JSONObject(Helpers.iStreamToString(client.getErrorStream()));

            }

            else if (client.getResponseCode() == 302) {
                aux = new JSONObject();
                Log.i("asdDEBUG",client.getURL().toString());
                aux.put("302",client.getURL().toString());
            }

            else if (response.equals("[]")) {
                aux = new JSONObject();
                aux.put("arrayResponse",new JSONArray());
            }

            else {
                if(response.startsWith("[")){
                    aux = new JSONObject();
                    aux.put("arrayResponse", new JSONArray(response));
                }
                else {
                    aux = new JSONObject(response);
                }
            }

            client.disconnect();

            return aux;

        } catch (IOException | JSONException e) {
            Log.e("error", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

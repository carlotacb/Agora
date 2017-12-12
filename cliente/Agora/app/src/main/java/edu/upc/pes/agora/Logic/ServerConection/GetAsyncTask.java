package edu.upc.pes.agora.Logic.ServerConection;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import edu.upc.pes.agora.Logic.Utils.Helpers;

public class GetAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {
    private URL url;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public GetAsyncTask(String url2, Context coming_context) {
        try {
            url = new URL(url2);
            context = coming_context;
        } catch (MalformedURLException e) {
            Log.i("asdGetAsyncTask", "", e);
        }
    }

    protected JSONObject doInBackground(final JSONObject... params) {

        Log.i("asdGetAsyncTask", "123");

        try {
            HttpsURLConnection client = (HttpsURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            //client.setInstanceFollowRedirects(true);
            client.connect();

            JSONObject aux;
            String response = Helpers.iStreamToString(client.getInputStream());

            if (client.getResponseCode() >= 400){
                Log.i("asdGetAsyncTask", "entra al 400");
                aux = new JSONObject(Helpers.iStreamToString(client.getErrorStream()));

            }

            else if (client.getResponseCode() == 302) {
                aux = new JSONObject();
                Log.i("asdGetAsyncTask", "entra al 302");
                aux.put("302",client.getURL().toString());
            }

            else if (response.equals("[]")) {
                aux = new JSONObject();
                aux.put("arrayResponse",new JSONArray());
                Log.i("asdGetAsyncTask", "entra al []");
            }

            else {
                if(response.startsWith("[")){
                    aux = new JSONObject();
                    aux.put("arrayResponse", new JSONArray(response));
                    Log.i("asdGetAsyncTask", "entra al else");
                }
                else {
                    aux = new JSONObject(response);
                    Log.i("asdGetAsyncTask", "entra al else2");
                }
            }

            client.disconnect();

            Log.i("asdGetAsyncTask", "desconectat");
            Log.i("asdGetAsyncTask", aux.toString());
            return aux;


        } catch (IOException | JSONException e) {
            Log.e("error", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

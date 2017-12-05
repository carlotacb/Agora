package edu.upc.pes.agora.Logic;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostSesionAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {
    private URL url;
    private Context context;

    public PostSesionAsyncTask(String url2, Context coming_context) {
        try {
            url = new URL(url2);
            context = coming_context;
        } catch (MalformedURLException e) {
            Log.v("TagMatchGetAsyncTask", "", e);
        }
    }

    protected JSONObject doInBackground(final JSONObject... params) {
        try {
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setReadTimeout(15000);
            client.setConnectTimeout(15000);
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            client.setRequestProperty("Accept", "application/json");
            client.setDoInput(true);
            client.setDoOutput(true);

            Log.i("asdPostAsyncTask", "hola");

            OutputStreamWriter wr = new OutputStreamWriter(client.getOutputStream());
            wr.write(params[0].toString());
            wr.flush();
            wr.close();

            client.getOutputStream().close();
            client.connect();
            Log.i("asdPostAsyncTask", "hola2");
            JSONObject response = new JSONObject();

            //Get JSON Object containing the token
            InputStream is = null;
            StringBuffer sb = new StringBuffer();
            is = new BufferedInputStream(client.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";

            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            JSONObject jo = new JSONObject(sb.toString());
            String token = jo.getString("token");
            int zone = jo.getInt("zone");

            try {
                Log.i("asdPostAsyncTask", Integer.toString(client.getResponseCode()));
                if (client.getResponseCode() == 200) {

                    response.put("success", true);
                    response.put("token", token);
                    response.put("zone", zone);
                } else {
                    Log.i("asdTAG", "response code: " + client.getResponseCode());
                    String error = client.getResponseMessage();
                    response.put("success", false);
                    if (client.getResponseCode() != 403)
                        response.put("errorMessage", error);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            client.disconnect();
            Log.i("asdGetAsyncTask", response.toString());

            return response;


        } catch (IOException e) {
            e.printStackTrace();
            Log.e("asdTAG", e.getMessage());

            JSONObject response = new JSONObject();
            try {
                response.put("success", false);
                response.put("errorMessage", "Android Internal error");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return response;
        } catch (JSONException e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            try {
                response.put("success",false);
                response.put("errorMessage","Android Internal error");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return response;
        }

    }
}

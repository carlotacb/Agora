package edu.upc.pes.agora.Logic.ServerConection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.upc.pes.agora.Logic.Utils.Helpers;
import edu.upc.pes.agora.Presentation.MainActivity;

import static edu.upc.pes.agora.Logic.Utils.Constants.SH_PREF_NAME;

public class PostAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {
    private URL url;
    private Context context;


    SharedPreferences prefs;

    public PostAsyncTask(String url2, Context coming_context) {
        try {
            url = new URL(url2);
            context = coming_context;
        } catch (MalformedURLException e) {
            Log.v("asd123", "entra1", e);
        }
    }

    protected JSONObject doInBackground(final JSONObject... params) {
        prefs = MainActivity.getContextOfApplication().getSharedPreferences(SH_PREF_NAME, Context.MODE_PRIVATE);
        String tokenToSend = "";
        if (prefs.contains("token")){
            tokenToSend = prefs.getString("token","");
        }

        try {
            //Open connection to server
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setReadTimeout(15000);
            client.setConnectTimeout(15000);
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            client.setRequestProperty("Accept","application/json");
            client.setRequestProperty("Authorization",tokenToSend);
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

            //return success=true and token if connection is successful
            try {
                Log.i("asdPostAsyncTask", Integer.toString(client.getResponseCode()));
                String reponsie = Helpers.iStreamToString(client.getInputStream());
                Log.i("resposta", reponsie);

                if (client.getResponseCode() == 200) {
                    response.put("success",true);
                    response.put("ArrayResponse", new JSONObject(reponsie));
                }
                else if(client.getResponseCode() == 400){
                    response.put("success", false);
                    response.put("errorMessage","Selected location outside of allowed zone.");
                }
                else  {
                    Log.i("asdTAG","response code: "+client.getResponseCode());
                    String error = client.getResponseMessage();
                    response.put("success",false);
                    if(client.getResponseCode()!=403)
                        response.put("errorMessage",error);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            client.disconnect();
            Log.i("asdPostAsyncTask", response.toString());
            return response;


        } catch (IOException e) {
            e.printStackTrace();
            Log.e("asdTAG", e.getMessage());

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

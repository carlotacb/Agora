package edu.upc.pes.agora.Logic;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import edu.upc.pes.agora.R;

public class GetAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {
    private URL url;
    private Context context;

    public GetAsyncTask(String url2, Context coming_context) {
        try {
            url = new URL(url2);
            context = coming_context;
        } catch (MalformedURLException e) {
            Log.v("asd", "", e);
        }
    }

    protected JSONObject doInBackground(final JSONObject... params) {
        try {


            String user = params[0].getString("username");
            String password = params[0].getString("password");
            String userPass = user + ":" + password;

            String basicAuth;
            basicAuth = "Basic " + new String(Base64.encode(userPass.getBytes(), Base64.NO_WRAP));
            if (url.getHost().contains("heroku")) {
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setConnectTimeout(35000);
                con.setReadTimeout(35000);
                con.setRequestMethod("GET");
                con.setInstanceFollowRedirects(true);

                con.setRequestProperty("Authorization", basicAuth);

                JSONObject aux;

                String response = Helpers.iStreamToString(con.getInputStream());
                if (con.getResponseCode() >= 400){
                    aux = new JSONObject(Helpers.iStreamToString(con.getErrorStream()));
                }
                else if(con.getResponseCode() == 302) {
                    aux = new JSONObject();
                    Log.i(Constants.DebugTAG,con.getURL().toString());
                    aux.put("302",con.getURL().toString());
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

                con.disconnect();

                return aux;
            }
            else {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setInstanceFollowRedirects(true);

                con.setRequestProperty("Authorization", basicAuth);

                JSONObject aux;

                if (con.getResponseCode() >= 400){
                    Log.i(Constants.DebugTAG,"response code: "+con.getResponseCode());
                    aux = new JSONObject(Helpers.iStreamToString(con.getErrorStream()));
                }
                else if(con.getResponseCode() == 302) {
                    aux = new JSONObject();
                    Log.i(Constants.DebugTAG,"302 redirect: "+con.getURL().toString());
                    aux.put("302",con.getURL().toString());
                }
                else {
                    String response = Helpers.iStreamToString(con.getInputStream());
                    if(response.startsWith("[{")){
                        aux = new JSONObject();
                        Log.i(Constants.DebugTAG,"Array JSON REBUT");
                        aux.put("arrayResponse", new JSONArray(response));
                    }
                    else if (response.equals("[]")) {
                        aux = new JSONObject();
                        aux.put("arrayResponse",new JSONArray());
                    }
                    else {
                        Log.i(Constants.DebugTAG,"Objecte JSON REBUT");
                        aux = new JSONObject(response);
                    }
                }

                con.disconnect();

                return aux;
            }

        } catch (IOException | JSONException e) {
            Log.e(Constants.DebugTAG, e.getMessage());
            Map<String, String> map = new HashMap<>();
            if(e.getMessage().contains("failed to connect to")){
                if(e.getMessage().contains("Network is unreachable")){
                    map.put("error", context.getString(R.string.no_network_connection));
                } else {
                    map.put("error", context.getString(R.string.connection_timeout));
                }
            } else {
                map.put("error", e.getMessage());
            }
            return new JSONObject(map);
        }
    }

}

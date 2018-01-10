package edu.upc.pes.agora.Logic.ServerConection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.Presentation.MainActivity;

import static edu.upc.pes.agora.Logic.Utils.Constants.SH_PREF_NAME;

public class PutAsyncTask extends AsyncTask<JSONObject, Void, JSONObject>{

    private URL url;
    private Context context;
    SharedPreferences prefs;
    String newAchievement="";

    public PutAsyncTask(String url2, Context coming_context) {
        try {
            url = new URL(url2);
            context = coming_context;
        } catch (MalformedURLException e) {
            Log.v("asd123", "entra1", e);
        }
    }
    public String getNewAchievement(){
        return newAchievement;
    }

    protected JSONObject doInBackground(JSONObject... params) {
        prefs = MainActivity.getContextOfApplication().getSharedPreferences(SH_PREF_NAME, Context.MODE_PRIVATE);
        String tokenToSend = "";
        if (prefs.contains("token")){
            tokenToSend = prefs.getString("token","");
        }

        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setRequestProperty("Accept","application/json");
            con.setRequestProperty("Authorization", Constants.SH_PREF_NAME);
            con.setDoInput(true);
            con.setDoOutput(true);


            OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
            wr.write(params[0].toString());
            wr.flush();
            wr.close();

            con.getOutputStream().close();
            con.connect();
            newAchievement = con.getHeaderField("X-New-Achievements");


            JSONObject response = new JSONObject();

            try {
                if (con.getResponseCode() == 200) {
                    response.put("success",true);
                }
                else if(con.getResponseCode() == 400){
                    response.put("success", false);
                    response.put("errorMessage","Selected location outside of allowed zone.");
                }
                else  {
                    Log.i("asdTAG","response code: "+con.getResponseCode());
                    String error = con.getResponseMessage();
                    response.put("success",false);
                    if(con.getResponseCode()!=403)
                        response.put("errorMessage",error);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            con.disconnect();

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

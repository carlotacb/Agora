package edu.upc.pes.agora;

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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by gerar on 24/10/2017.
 */

public class GetAsyncTask extends AsyncTask<JSONObject, Void, Boolean> {
    private URL url;
    private Context context;

    public GetAsyncTask(String url2, Context coming_context) {
        try {
            url = new URL(url2);
            context = coming_context;
        } catch (MalformedURLException e) {
            Log.v("TagMatchGetAsyncTask", "", e);
        }
    }

    protected Boolean doInBackground(final JSONObject... params) {
        try {
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setReadTimeout(15000);
            client.setConnectTimeout(15000);
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            client.setRequestProperty("Accept","application/json");
            client.setDoInput(true);
            client.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(client.getOutputStream());
            wr.write(params[0].toString());
            wr.flush();
            wr.close();

            client.getOutputStream().close();
            client.connect();

            if (client.getResponseCode()==200) {
                String response = iStreamToString(client.getInputStream());
                client.disconnect();

                return true;
            }
            else  {
                Log.i("asdTAG","response code: "+client.getResponseCode());
                client.disconnect();

                return  false;
               // aux = new JSONObject(iStreamToString(client.getErrorStream()));
            }




        } catch (IOException e) {
            e.printStackTrace();
            Log.e("asdTAG", e.getMessage());

            return false;
        }
    }

    public static String iStreamToString(InputStream is1) {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is1), 4096);
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String contentOfMyInputStream = sb.toString();
        return contentOfMyInputStream;
    }
}

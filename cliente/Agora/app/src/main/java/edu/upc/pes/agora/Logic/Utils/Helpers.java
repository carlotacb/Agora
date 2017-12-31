package edu.upc.pes.agora.Logic.Utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Helpers {

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
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String showDate(JSONObject d){
        Log.i("date","");
        return "";
    }

    public static String showDate(String d) {
        String[] res = d.split("T")[0].split("-");
        if (res.length == 3) {
            return "" + res[2] + "/" + res[1] + "/" + res[0];
        }else{
            return "dd/MM/yyyy";
        }
    }

}

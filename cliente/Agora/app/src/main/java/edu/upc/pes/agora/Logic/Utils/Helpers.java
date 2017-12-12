package edu.upc.pes.agora.Logic.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Helpers {

    //private static final String SH_PREF_NAME = "SavedToken";

    /*public static ArrayList<String> getPersonalData(Context context){
        ArrayList<String> data = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(SH_PREF_NAME, Context.MODE_PRIVATE);
        data.add(prefs.getString("name", null));
        data.add(prefs.getString("password", null));
        return data;
    }*/

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
        return sb.toString();
    }

    public static void logout(Context context) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SH_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.remove("token");
        editor.apply();
    }

}

package edu.upc.pes.agora.Logic.Utils;

import android.util.Log;

import org.json.JSONObject;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.upc.pes.agora.R;

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

    public static void changeFlag(ImageView canviidioma) {
        switch (Constants.Idioma) {
            case "ca":
                canviidioma.setImageResource(R.drawable.rep);
                break;
            case "es":
                canviidioma.setImageResource(R.drawable.spa);
                break;
            case "en":
                canviidioma.setImageResource(R.drawable.ing);
                break;
        }
    }

    public static String getBarrio(Integer zona) {

        String barrio = "";

        switch (zona) {
            case 0:
                barrio = "Ciutat Vella";
                break;
            case 1:
                barrio = "Eixample";
                break;
            case 2:
                barrio = "Sants-Montjuic";
                break;
            case 3:
                barrio = "Les Corts";
                break;
            case 4:
                barrio = "Sarrià-Sant Gervasi";
                break;
            case 5:
                barrio = "Gràcia";
                break;
            case 6:
                barrio = "Horta-Guinardó";
                break;
            case 7:
                barrio = "Nou Barris";
                break;
            case 8:
                barrio = "San Andreu";
                break;
            case 9:
                barrio = "San Martí";
                break;
        }

        return barrio;
    }

}

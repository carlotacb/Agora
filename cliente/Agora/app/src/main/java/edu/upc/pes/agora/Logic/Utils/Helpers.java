package edu.upc.pes.agora.Logic.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;

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
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void logout(Context context) {
        /*SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SH_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.remove("token");
        editor.apply();*/

        Constants.SH_PREF_NAME = "";

    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static String showDate(JSONObject d){
        Log.i("date","");
        return "";
    }

}

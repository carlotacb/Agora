package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.upc.pes.agora.Logic.Adapters.ArrayAdapterPersonalizado;
import edu.upc.pes.agora.Logic.Listeners.DrawerToggleAdvanced;
import edu.upc.pes.agora.Logic.Listeners.NavMenuListener;
import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.R;

public class LogrosActivity extends AppCompatActivity {

    private List<String> logros = new ArrayList<>();
    private JSONObject Jason = new JSONObject();

    private String[] logros2;
    private String  itemLogro = "logro";

    private Integer size = 0;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navMenu);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        TextView headerUserName = (TextView) navigationView.findViewById(R.id.head_username);
        headerUserName.setText(Constants.Username);
        final ImageView foto = (ImageView) navigationView.findViewById(R.id.navigationPic);

        final Resources res = this.getResources();

        if (Constants.fotoperfil == null) {
            JSONObject Jason = new JSONObject();
            new GetTokenAsyncTask("https://agora-pes.herokuapp.com/api/profile", this) {

                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    try {
                        if (jsonObject.has("error")) {
                            String error = jsonObject.get("error").toString();
                            Log.i("asdProfile", "Error");

                            Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        else {

                            Log.i("asdProfile", (jsonObject.toString()));

                            if (jsonObject.has("image")) {
                                String imageJ = jsonObject.getString("image");

                                if (!imageJ.equals("null")) {
                                    byte[] imageAsBytes = Base64.decode(imageJ.getBytes(), Base64.DEFAULT);
                                    Constants.fotoperfil = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                                    foto.setImageBitmap(Constants.fotoperfil);
                                }

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(Jason);
        }

        else {
            foto.setImageBitmap(Constants.fotoperfil);
        }

        navigationView.getMenu().getItem(NavMenuListener.logros).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavMenuListener(this, drawer));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.logros);
        toolbar.setLogo(R.drawable.trophyw_24);
        setSupportActionBar(toolbar);

        DrawerToggleAdvanced toggle = new DrawerToggleAdvanced(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();



        final ListView listView = (ListView) findViewById(R.id.lista);

        new GetTokenAsyncTask("https://agora-pes.herokuapp.com/api/profile/achievements", this) {

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    if (jsonObject.has("error")) {
                        String error = jsonObject.get("error").toString();
                        Log.i("asdProfile", "Error");
                        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    else {
                        if(jsonObject.has("achievements")){

                            JSONArray arrJson = jsonObject.getJSONArray("achievements");
                            for(int i = 0; i < arrJson.length(); i++) {
                               JSONObject j = arrJson.getJSONObject(i);
                               String item = j.getString("code");
                                itemLogro = codificaLogro(item);
                                if(itemLogro!="Something went wrong" && itemLogro!=""){
                                    logros.add(itemLogro);
                                }
                            }
                            size = arrJson.length();
                        }


                        if(jsonObject.has("missingAchievements")){
                            JSONArray arrJson = jsonObject.getJSONArray("missingAchievements");
                            for(int i = 0; i < arrJson.length(); i++) {
                                String item = arrJson.getString(i);
                                itemLogro = codificaLogro(item);

                                if(itemLogro!="Something went wrong" && itemLogro!=""){
                                    logros.add(itemLogro);
                                }
                            }
                        }
                    }

                    ArrayAdapterPersonalizado cv = new ArrayAdapterPersonalizado(getApplicationContext(), logros,size);
                    listView.setAdapter(cv);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(Jason);

        //listView.setBackgroundColor(Color.WHITE);

        final List<Boolean> list = new ArrayList<Boolean>(Arrays.asList(new Boolean[10]));
        Collections.fill(list, Boolean.FALSE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /*Toast toast = Toast.makeText(getApplicationContext(), "posicion " +i +"   size " +size, Toast.LENGTH_SHORT);
                toast.show();*/

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LogrosActivity.this);
                int vale = 0;
                String s = listView.getItemAtPosition(i).toString();
                if(s.equals(getString(R.string.TWIT1))){
                    vale = 1;
                }
                else if (s.equals(getString(R.string.PROP5))){
                    vale = 2;
                }
                else if (s.equals(getString(R.string.COM5))){
                    vale = 3;
                }
                else {
                    vale = 0;
                }
                View mView = getLayoutInflater().inflate(R.layout.dialog_trophy, null);

                String estado = "";

                if (i >= size){
                    estado = getApplicationContext().getString(R.string.pendiente);
                    mView.setBackgroundColor(Color.LTGRAY);
                }
                else{
                    estado = getApplicationContext().getString(R.string.conseguido);
                    mView.setBackgroundColor(Color.WHITE);
                }


                TextView textView = (TextView)mView.findViewById(R.id.textView);
                textView.setText(listView.getItemAtPosition(i).toString()+estado);
                Button mAccept = (Button) mView.findViewById(R.id.etAccept);

                ImageView imageView = (ImageView) mView.findViewById(R.id.image);
                if (vale == 1 & i < size){
                    imageView.setImageResource(R.drawable.imagecafe);
                }
                else if ( vale == 2 & i < size){
                    imageView.setImageResource(R.drawable.imageropa);
                }
                else if ( vale == 3 & i < size){
                    imageView.setImageResource(R.drawable.imagedonut);
                }
                else {
                    imageView.setImageResource(R.drawable.ic_trofeo_logro2);
                }

                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                mAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private String codificaLogro(String codigoLogro) {

        String Logro = "";
        switch(codigoLogro) {
            case "PROP1": Logro = getApplicationContext().getString(R.string.PROP1);
                break;
            case "PROP5": Logro = getApplicationContext().getString(R.string.PROP5);
                break;
            case "PROP10": Logro = getApplicationContext().getString(R.string.PROP10);
                break;
            case "PROP50": Logro = getApplicationContext().getString(R.string.PROP50);
                break;
            case "PROP100": Logro = getApplicationContext().getString(R.string.PROP100);
                break;
            case "FAV1": Logro = getApplicationContext().getString(R.string.FAV1);
                break;
            case "FAV10": Logro = getApplicationContext().getString(R.string.FAV10);
                break;
            case "UBI1": Logro = getApplicationContext().getString(R.string.UBI1);
                break;
            case "UBI10": Logro = getApplicationContext().getString(R.string.UBI10);
                break;
            case "PROPC": Logro = getApplicationContext().getString(R.string.PROPC);
                break;
            case "PROPD": Logro = getApplicationContext().getString(R.string.PROPD);
                break;
            case "PROPO": Logro = getApplicationContext().getString(R.string.PROPO);
                break;
            case "PROPM": Logro = getApplicationContext().getString(R.string.PROPM);
                break;
            case "PROPE": Logro = getApplicationContext().getString(R.string.PROPE);
                break;
            case "PROPT": Logro = getApplicationContext().getString(R.string.PROPT);
                break;
            case "PROPQ": Logro = getApplicationContext().getString(R.string.PROPQ);
                break;
            case "PROPS": Logro = getApplicationContext().getString(R.string.PROPS);
                break;
            case "TWIT1": Logro = getApplicationContext().getString(R.string.TWIT1);
                break;
            case "TWIT100": Logro = getApplicationContext().getString(R.string.TWIT100);
                break;
            case "GLIKE1": Logro = getApplicationContext().getString(R.string.GLIKE1);
                break;
            case "GLIKE10": Logro = getApplicationContext().getString(R.string.GLIKE10);
                break;
            case "GLIKE100": Logro = getApplicationContext().getString(R.string.GLIKE100);
                break;
            case "PLIKE1": Logro = getApplicationContext().getString(R.string.PLIKE1);
                break;
            case "PLIKE10": Logro = getApplicationContext().getString(R.string.PLIKE10);
                break;
            case "PLIKE100": Logro = getApplicationContext().getString(R.string.PLIKE100);
                break;
            case "COM1": Logro = getApplicationContext().getString(R.string.COM1);
                break;
            case "COM5": Logro = getApplicationContext().getString(R.string.COM5);
                break;
            case "COM25": Logro = getApplicationContext().getString(R.string.COM25);
                break;
            case "COM100": Logro = getApplicationContext().getString(R.string.COM100);
                break;
            case "GCOM1": Logro = getApplicationContext().getString(R.string.GCOM1);
                break;
            case "GCOM10": Logro = getApplicationContext().getString(R.string.GCOM10);
                break;
            case "GCOM100": Logro = getApplicationContext().getString(R.string.GCOM100);
                break;
            default: Logro = "Something went wrong";
                break;
        }
        return Logro;
    }

}

package edu.upc.pes.agora.Presentation;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navMenu);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        TextView headerUserName = (TextView) navigationView.findViewById(R.id.head_username);
        headerUserName.setText(Constants.Username);

       // TextView conseguidos = (TextView) findViewById(R.id.conseguidos);
      //  conseguidos.setText(R.string.conseguidos); //R.string.conseguidos

     //   TextView pendientes = (TextView) findViewById(R.id.pendientes);
     //   pendientes.setText(R.string.pendientes); //R.string.conseguidos


        navigationView.getMenu().getItem(NavMenuListener.logros).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavMenuListener(this, drawer));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.logros);
        setSupportActionBar(toolbar);

        DrawerToggleAdvanced toggle = new DrawerToggleAdvanced(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
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
                         //   logros2 = new String[arrJson.length()];
                            for(int i = 0; i < arrJson.length(); i++) {
                               JSONObject j = arrJson.getJSONObject(i);
                               String item = j.getString("code");
                                itemLogro = codificaLogro(item);
                     //           logros.add(itemLogro);
                                if(itemLogro!="Something went wrong" && itemLogro!=""){
                                 /*   if (i ==0)logros.set(i,itemLogro);
                                    else*/ logros.add(itemLogro);

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
                                    // la lista no tiene q ser logros. tiene q ser una lista diferente
                                }
                            }

                        }


                    }


               //     ArrayAdapter<String> adaptador = new ArrayAdapter<String>(LogrosActivity.this, android.R.layout.simple_list_item_1, logros);
                    //   ArrayAdapterPersonalizado adaptador = new ArrayAdapter<String>(LogrosActivity.this, android.R.layout.simple_list_item_1, logros);
                    ArrayAdapterPersonalizado cv = new ArrayAdapterPersonalizado(getApplicationContext(), logros,size);
                    listView.setAdapter(cv);

                 /*  int max = listView.getAdapter().getCount();
                   for ( int i = 0; i < max ; i++){
                       listView.get
                   }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(Jason);

       /* try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/

       /* logros.add("Crear 1 propuestaaaaaaaaaaa");
        logros.add("Crear 5 propuestasssssssssssssssss");
        logros.add("Crear 10 propuestasssssssssssssssssss");
        logros.add("Compartir 1 propuesta en Twitterrrrrrrrrrrrrrrrrrrrrrrrrrr");
        logros.add("Compartir 5 propuestas en Twitterrrrrrrrrrrrrrrrrrrr");
        logros.add("Compartir 10 propuestas en Twitterrrrrrrrrrrrrrrrrrrrrrrr");*/
        //logros.add(itemLogro);
      //  logros.add("Logros conseguidos por el usuario");
      //  logros.set(0,"cambiado");
        listView.setBackgroundColor(Color.WHITE);

        final List<Boolean> list = new ArrayList<Boolean>(Arrays.asList(new Boolean[10]));
        Collections.fill(list, Boolean.FALSE);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             //   view.setBackgroundColor(Color.CYAN);
            //    Toast.makeText(getApplicationContext(), "Ha pulsado el item en posicion " + i, Toast.LENGTH_LONG).show();
             //   Toast.makeText(getApplicationContext(), "Estado del boolean de la lista " + list.get(i), Toast.LENGTH_LONG).show();
           //     list.set(i,true);
           //     Toast.makeText(getApplicationContext(), "Estado del boolean de la lista version 2" + list.get(i), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LogrosActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_trophy, null);
                String estado = "";

                if (i >= size) estado = getApplicationContext().getString(R.string.pendiente);
                else estado = getApplicationContext().getString(R.string.conseguido);

                TextView textView = (TextView)mView.findViewById(R.id.textView);
                textView.setText(listView.getItemAtPosition(i).toString()+estado);
                Button mAccept = (Button) mView.findViewById(R.id.etAccept);

                ImageView imageView = (ImageView) mView.findViewById(R.id.image);
                imageView.setImageResource(R.drawable.ic_trofeo_logro2);



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

        String Logro ="";
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




            case "COM10": Logro = "Comenta 10 veces en una propuesta";
                    break;
            default: Logro = "Something went wrong";
                    break;
        }
        return Logro;
    }

}

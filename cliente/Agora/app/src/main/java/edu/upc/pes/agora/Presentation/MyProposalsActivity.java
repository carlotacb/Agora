package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.Logic.Listeners.DrawerToggleAdvanced;
import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.Listeners.NavMenuListener;
import edu.upc.pes.agora.Logic.Models.Proposal;
import edu.upc.pes.agora.Logic.Adapters.RecyclerAdapter;
import edu.upc.pes.agora.Logic.Utils.Helpers;
import edu.upc.pes.agora.R;

public class MyProposalsActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private Locale locale;

    private RecyclerView myrecycler;
    private RecyclerView.Adapter adapter;
    private RecyclerAdapter radapter;
    private LinearLayout cargando;

    private List<Proposal> listProposals;
    private JSONObject Jason = new JSONObject();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_proposals);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navMenu);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        TextView headerUserName = (TextView) navigationView.findViewById(R.id.head_username);
        cargando = (LinearLayout) findViewById(R.id.pantallacargandomp);
        headerUserName.setText(Constants.Username);
        final ImageView foto = (ImageView) navigationView.findViewById(R.id.navigationPic);

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

        navigationView.getMenu().getItem(NavMenuListener.myproposals).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavMenuListener(this, drawer));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_my_propuestas);
        toolbar.setLogo(R.mipmap.ic_accountw);
        setSupportActionBar(toolbar);

        DrawerToggleAdvanced toggle = new DrawerToggleAdvanced(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        myrecycler = (RecyclerView) findViewById(R.id.recyclerView);
        myrecycler.setHasFixedSize(true); // cada item del RecyclerView te un Size en concret.
        myrecycler.setLayoutManager(new LinearLayoutManager(this));

        listProposals = new ArrayList<>();

        new GetTokenAsyncTask("https://agora-pes.herokuapp.com/api/proposal?username=" + Constants.Username.toLowerCase(), this) {

            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                myrecycler.setVisibility(View.GONE);
                cargando.setVisibility(View.VISIBLE);
                try {
                    if (jsonObject.has("error")) {
                        String error = jsonObject.get("error").toString();
                        Log.i("asd123", "Error");

                        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    else if (jsonObject != null){
                        JSONArray ArrayProp = jsonObject.getJSONArray("arrayResponse");
                        ArrayList<Proposal> propostes = new ArrayList<>();

                        if (ArrayProp != null) {
                            for (int i=0; i < ArrayProp.length(); i++){
                                Proposal aux;
                                Log.i("asd123", (ArrayProp.get(i).toString()));

                                JSONObject jas = ArrayProp.getJSONObject(i);
                                JSONArray comentaris = jas.getJSONArray("comments");
                                String title = jas.getString("title");
                                String owner = jas.getString("owner");
                                String description = jas.getString("content");
                                Integer id = jas.getInt("id");
                                String creada = jas.getString("createdDateTime");
                                String ca = jas.getString("categoria");
                                String createDate = Helpers.showDate(jas.getString("createdDateTime"));
                                String updateDate = Helpers.showDate(jas.getString("updatedDateTime"));
                                Integer nvotes = jas.getInt("numberUpvotes");
                                Integer nunvotes = jas.getInt("numberDownvotes");
                                Integer vote = jas.getInt("userVoted");
                                Boolean fav = jas.getBoolean("favorited");
                                Integer numcoments = comentaris.length();

                                if(jas.has("location") && jas.getJSONObject("location").has("lat") && jas.getJSONObject("location").get("lat") != JSONObject.NULL ) {
                                    Double lat = jas.getJSONObject("location").getDouble("lat");
                                    Double lng = jas.getJSONObject("location").getDouble("long");
                                    aux = new Proposal(id, title, description, owner, ca, lat, lng, createDate, updateDate);
                                }else {
                                    aux = new Proposal(id, title, description, owner, ca, createDate, updateDate);
                                }

                                Log.i("asdCreate", creada);

                                aux.setNumerocomentarios(numcoments);
                                aux.setFavorite(fav);
                                aux.setNumerounvotes(nunvotes);
                                aux.setNumerovotes(nvotes);
                                aux.setVotacion(vote);

                                propostes.add(aux);
                            }
                        }

                        radapter = new RecyclerAdapter(propostes, getApplicationContext());
                        myrecycler.setAdapter(radapter);
                    }

                    String achievement = this.getNewAchievement();


                    if (achievement != null && !achievement.equals("")) {
                        sendNot(achievement);
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
                myrecycler.setVisibility(View.VISIBLE);
                cargando.setVisibility(View.GONE);
            }
        }.execute(Jason);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem bandera = menu.findItem(R.id.bandera);
        switch (Constants.Idioma) {
            case "es":
                bandera.setIcon(R.drawable.spa);
                break;
            case "en":
                bandera.setIcon(R.drawable.ing);
                break;
            case "ca":
                bandera.setIcon(R.drawable.rep);
                break;
        }
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        Intent refresh = new Intent(this, MyProposalsActivity.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Boolean change = false;

        if (id == R.id.men_castella){
            Constants.Idioma = "es";
            change = true;
        }

        else if (id == R.id.men_catala){
            Constants.Idioma = "ca";
            change = true;
        }

        else if (id == R.id.men_angles) {
            Constants.Idioma = "en";
            change = true;
        }

        if (change) {
            config.locale = new Locale(Constants.Idioma);
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }



    public void sendNot(String achievement){

        Intent i=new Intent(MyProposalsActivity.this, LogrosActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyProposalsActivity.this, 0, i, 0);

        String[] parts = achievement.split(",");
        int count = parts.length;
        for ( int j = 0; j < count; j++ ){
            String decoded = codificaLogro(parts[j]);
            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.logo);

            NotificationCompat.Builder mBuilder;
            NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_trofeo_logro)
                    .setContentTitle(getString(R.string.nuevo))
                    .setLargeIcon(icon)
                    .setContentText(decoded)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(decoded))
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(true);

            mNotifyMgr.notify(j+1, mBuilder.build());
        }



    }

/*
    public void crear(String achievement) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateProposalActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_trophy, null);
        TextView textView = (TextView)mView.findViewById(R.id.textView);
        textView.setText(codificaLogro(achievement));
        Button mAccept = (Button) mView.findViewById(R.id.etAccept);
        ImageView imageView = (ImageView) mView.findViewById(R.id.image);
        imageView.setImageResource(R.drawable.ic_trofeo_logro2);
        mBuilder.setView(mView);
        //  mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                startActivity(new Intent(CreateProposalActivity.this, MainActivity.class));
            }
        });
        dialog.show();

        mAccept.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //    startActivity(new Intent(CreateProposalActivity.this, MainActivity.class));

            }
        });
    }*/

    private String codificaLogro(String codigoLogro) {

     /*   String[] parts = codigoLogro.split(",");
        int count = parts.length;
        String[] Logros = new String[count];*/
        // for (int i = 0; i < count; i++){
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
        //   Logros[i]=Logro;
        //  }
        return Logro;
    }

}

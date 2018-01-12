package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
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
import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.Listeners.NavMenuListener;
import edu.upc.pes.agora.Logic.Adapters.ProposalAdapter;
import edu.upc.pes.agora.Logic.Models.Proposal;
import edu.upc.pes.agora.Logic.Utils.Helpers;
import edu.upc.pes.agora.R;

public class MainActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private JSONObject Jason = new JSONObject();
    private ListView llista_propostes;
    private ArrayList<Proposal> propostes;
    @SuppressLint("StaticFieldLeak")
    public static Context mainContext;
    private List<String> opcions = new ArrayList<>();
    private List<String> usuaris = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private Spinner filterSpinner, searchSpinner;
    private LinearLayout cargando;
    private TextView buscartext;
    private AutoCompleteTextView searchUsers;
    private  ArrayAdapter<String> adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_main);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setLogo(R.mipmap.ic_homew);
        setSupportActionBar(toolbar);

        mainContext = getApplicationContext();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        TextView headerUserName = (TextView) navigationView.findViewById(R.id.head_username);
        headerUserName.setText(Constants.Username);
        final ImageView foto = (ImageView) navigationView.findViewById(R.id.navigationPic);

        final Resources res = this.getResources();

        if (Constants.fotoperfil == null) {
            JSONObject Jason = new JSONObject();
            new GetTokenAsyncTask("https://agora-pes.herokuapp.com/api/profile", MainActivity.this) {

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

        navigationView.getMenu().getItem(NavMenuListener.homneButton).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavMenuListener(this, drawer));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        llista_propostes = (ListView) findViewById(R.id.list);
        filterSpinner = (Spinner) findViewById(R.id.filterSpinnerView);
        searchSpinner = (Spinner) findViewById(R.id.searchSpinnerView);
        buscartext = (TextView) findViewById(R.id.buscar);
        searchUsers = (AutoCompleteTextView) findViewById(R.id.searchUser);
        cargando = (LinearLayout) findViewById(R.id.pantallacargando);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateProposalActivity.class));

            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.refresh,R.color.refresh1,R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        ferGetAsyncTask("https://agora-pes.herokuapp.com/api/proposal");
                    }
                },3000);
            }
        });

        ferGetAsyncTask("https://agora-pes.herokuapp.com/api/proposal");

        opcions.add(res.getString(R.string.tot));
        opcions.add(res.getString(R.string.categ));
        opcions.add(res.getString(R.string.user));

        categories.add(res.getString(R.string.todo));
        categories.add(res.getString(R.string.cultura));
        categories.add(res.getString(R.string.ocio));
        categories.add(res.getString(R.string.mantenimiento));
        categories.add(res.getString(R.string.eventos));
        categories.add(res.getString(R.string.turismo));
        categories.add(res.getString(R.string.deportes));
        categories.add(res.getString(R.string.quejas));
        categories.add(res.getString(R.string.soporte));

        ArrayAdapter<String> filterSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_filter_style, opcions);
        filterSpinnerAdapter.setDropDownViewResource(R.layout.spinner_filter_style);

        new GetTokenAsyncTask("https://agora-pes.herokuapp.com/api/profile/comunity", mainContext) {

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    if (jsonObject.has("error")) {
                        String error = jsonObject.get("error").toString();
                        Log.i("asd123", "Error");

                        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    else if (jsonObject != null){
                        JSONArray ArrayProp = jsonObject.getJSONArray("arrayResponse");
                        usuaris = new ArrayList<>();

                        if (ArrayProp != null) {
                            for (int i=0; i < ArrayProp.length(); i++){

                                Log.i("asd123", (ArrayProp.get(i).toString()));

                                JSONObject jas = ArrayProp.getJSONObject(i);


                                String username = jas.getString("username");
                                usuaris.add(username);

                            }
                        }
                        adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_dropdown_item_1line,usuaris);
                        adapter.setDropDownViewResource(R.layout.spinner_filter_style);
                        searchUsers.setAdapter(adapter);
                        searchUsers.setThreshold(1);
                        searchUsers.setAdapter(adapter);

                        ArrayAdapter<String> opcionsSpinnerAdapter2 = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_filter_style, usuaris);
                        opcionsSpinnerAdapter2.setDropDownViewResource(R.layout.spinner_filter_style);
                        searchSpinner.setAdapter(opcionsSpinnerAdapter2);
                        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedItem = searchSpinner.getSelectedItem().toString().toLowerCase();
                                Log.i("asdse", selectedItem);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                } catch (JSONException e ) {
                    e.printStackTrace();
                }
            }
        }.execute(Jason);

        filterSpinner.setAdapter(filterSpinnerAdapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = filterSpinner.getSelectedItem().toString().toLowerCase();
                switch (position) {
                    case 0: // TOT
                        ferGetAsyncTask("https://agora-pes.herokuapp.com/api/proposal");
                        buscartext.setVisibility(View.GONE);
                        searchSpinner.setVisibility(View.GONE);
                        searchUsers.setVisibility(View.GONE);
                        break;

                    case 1: // CATEGORIAS
                        Log.i("asdse", selectedItem);
                        buscartext.setVisibility(View.VISIBLE);
                        searchSpinner.setVisibility(View.VISIBLE);
                        searchUsers.setVisibility(View.GONE);
                        ArrayAdapter<String> opcionsSpinnerAdapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_filter_style, categories);
                        opcionsSpinnerAdapter.setDropDownViewResource(R.layout.spinner_filter_style);
                        searchSpinner.setAdapter(opcionsSpinnerAdapter);
                        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedItem = searchSpinner.getSelectedItem().toString().toLowerCase();
                                String categoriaS = "";
                                String url = "https://agora-pes.herokuapp.com/api/proposal";
                                switch (position) {
                                    case 0: //todas las categorias
                                        categoriaS = "A";
                                        break; 
                                    case 1: //cultura
                                        categoriaS = "C";
                                        break;
                                    case 2: //ocio
                                        categoriaS = "O";
                                        break;
                                    case 3: // mantenimiento
                                        categoriaS = "M";
                                        break;
                                    case 4: // eventos
                                        categoriaS = "E";
                                        break;
                                    case 5: // turismo
                                        categoriaS = "T";
                                        break;
                                    case 6: // deportes
                                        categoriaS = "D";
                                        break;
                                    case 7: // quejas
                                        categoriaS = "Q";
                                        break;
                                    case 8: // soporte
                                        categoriaS = "S";
                                        break;
                                }

                                if (!categoriaS.equals("A")) {
                                    Log.i("asdse", selectedItem);
                                    url += "?category="+ categoriaS;
                                    ferGetAsyncTask(url);
                                }
                                else {
                                    ferGetAsyncTask("https://agora-pes.herokuapp.com/api/proposal");
                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        break;

                    case 2: // USUARIOS
                        Log.i("asdse", selectedItem);
                        buscartext.setVisibility(View.VISIBLE);
                        searchSpinner.setVisibility(View.GONE);
                        searchUsers.setVisibility(View.VISIBLE);
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String user = searchUsers.getText().toString();
                Toast toast = Toast.makeText(getApplicationContext(), user, Toast.LENGTH_SHORT);
                toast.show();
                ferGetAsyncTask("https://agora-pes.herokuapp.com/api/proposal?username=" + user);
            }
        });

        searchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String user = searchUsers.getText().toString();
                Toast toast = Toast.makeText(getApplicationContext(), user, Toast.LENGTH_SHORT);
                toast.show();
                ferGetAsyncTask("https://agora-pes.herokuapp.com/api/proposal?username=" + user);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_search_menu, menu);
        inflater.inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.search_action_menu).getActionView();
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() != 0) {
                    ArrayList<Proposal> propostesAux = new ArrayList<>();
                    // handle search here
                    for(Proposal p : propostes){
                        if(p.getTitle() != null && p.getTitle().toLowerCase().contains(query.toLowerCase()))
                            propostesAux.add(p);
                    }
                    llista_propostes.setAdapter(new ProposalAdapter(propostesAux, getApplicationContext()));
                    return true;
                }
                else llista_propostes.setAdapter(new ProposalAdapter(propostes, getApplicationContext()));
                return false;
            }


            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() != 0) {
                    ArrayList<Proposal> propostesAux = new ArrayList<>();
                    // handle search here
                    for(Proposal p : propostes){
                        if(p.getTitle() != null && p.getTitle().toLowerCase().contains(query.toLowerCase()))
                            propostesAux.add(p);
                    }
                    llista_propostes.setAdapter(new ProposalAdapter(propostesAux, getApplicationContext()));
                    return true;
                }
                return false;
            }
        });

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

        Intent refresh = new Intent(this, MainActivity.class);
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

    public static Context getContextOfApplication(){
        return mainContext;
    }

    @SuppressLint("StaticFieldLeak")
    public void ferGetAsyncTask(String url) {
        new GetTokenAsyncTask(url, this) {

            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                cargando.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);

                try {
                    if (jsonObject.has("error")) {
                        String error = jsonObject.get("error").toString();
                        Log.i("asd123", "Error");

                        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    else if (jsonObject != null) {
                        JSONArray ArrayProp = jsonObject.getJSONArray("arrayResponse");
                        propostes = new ArrayList<>();

                        if (ArrayProp != null) {

                            for (int i = 0; i < ArrayProp.length(); i++){
                                Proposal aux;

                                Log.i("asd123", (ArrayProp.get(i).toString()));

                                JSONObject jas = ArrayProp.getJSONObject(i);
                                JSONArray comentaris = jas.getJSONArray("comments");
                                String title = jas.getString("title");
                                String owner = jas.getString("owner");
                                String description = jas.getString("content");
                                Integer id = jas.getInt("id");
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
                                } else {
                                    aux = new Proposal(id, title, description, owner, ca, createDate, updateDate);
                                }

                                aux.setNumerocomentarios(numcoments);
                                aux.setFavorite(fav);
                                aux.setNumerounvotes(nunvotes);
                                aux.setNumerovotes(nvotes);
                                aux.setVotacion(vote);

                                propostes.add(aux);
                            }
                        }
                        llista_propostes.setAdapter(new ProposalAdapter(propostes, getApplicationContext()));
                    }


                    String achievement = this.getNewAchievement();


                    if (achievement != null && !achievement.equals("")) {
                        sendNot(achievement);
                    }

                } catch (JSONException | ParseException e ) {
                    e.printStackTrace();
                }
                cargando.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
            }
        }.execute(Jason);
    }

    public void sendNot(String achievement){

        Intent i=new Intent(MainActivity.this, LogrosActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, i, 0);

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
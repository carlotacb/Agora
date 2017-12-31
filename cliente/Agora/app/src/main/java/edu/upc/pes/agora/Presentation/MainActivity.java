package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private FloatingActionButton fab;
    private Configuration config = new Configuration();
    private Locale locale;
    private JSONObject Jason = new JSONObject();
    private ListView llista_propostes;
    private ArrayList<Proposal> propostes;
    public static Context mainContext;
    private List<String> opcions = new ArrayList<>();
    private List<String> usuaris = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private Spinner filterSpinner, searchSpinner;
    private TextView buscartext;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_main);
        toolbar.setLogo(R.mipmap.ic_homew);
        setSupportActionBar(toolbar);

        mainContext = getApplicationContext();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        TextView headerUserName = (TextView) navigationView.findViewById(R.id.head_username);
        headerUserName.setText(Constants.Username);
        ImageView foto = (ImageView) navigationView.findViewById(R.id.navigationPic);

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

                                byte[] imageAsBytes = Base64.decode(imageJ.getBytes(), Base64.DEFAULT);

                                Constants.fotoperfil = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(Jason);
        }

        foto.setImageBitmap(Constants.fotoperfil);

        navigationView.getMenu().getItem(NavMenuListener.homneButton).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavMenuListener(this, drawer));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateProposalActivity.class));

            }
        });

        llista_propostes = (ListView) findViewById(R.id.list);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh,R.color.refresh1,R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        ferGetAsyncTask();
                    }
                },3000);
            }
        });

        ferGetAsyncTask();

        filterSpinner = (Spinner) findViewById(R.id.filterSpinnerView);
        searchSpinner = (Spinner) findViewById(R.id.searchSpinnerView);
        buscartext = (TextView) findViewById(R.id.buscar);

        final Resources res = this.getResources();

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
        filterSpinner.setAdapter(filterSpinnerAdapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = filterSpinner.getSelectedItem().toString().toLowerCase();
                final View V = view;
                switch (position) {
                    case 0: // TOT
                        ferGetAsyncTask();

                        buscartext.setVisibility(View.GONE);
                        searchSpinner.setVisibility(View.GONE);

                        break;

                    case 1: // CATEGORIAS
                        Log.i("asdse", selectedItem);

                        buscartext.setVisibility(View.VISIBLE);
                        searchSpinner.setVisibility(View.VISIBLE);

                        ArrayAdapter<String> opcionsSpinnerAdapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_filter_style, categories);
                        opcionsSpinnerAdapter.setDropDownViewResource(R.layout.spinner_filter_style);
                        searchSpinner.setAdapter(opcionsSpinnerAdapter);
                        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedItem = searchSpinner.getSelectedItem().toString().toLowerCase();
                                String categoriaS = "";
                                Toast toast;
                                String url = "https://agora-pes.herokuapp.com/api/proposal";
                                switch (position) {
                                    case 0: //todas las categorias
                                        Log.i("asdse", selectedItem);
                                        categoriaS = "A";
                                         toast = Toast.makeText(getApplicationContext(), categoriaS, Toast.LENGTH_SHORT);
                                        toast.show();

                                        break;
                                    case 1: //cultura
                                        Log.i("asdse", selectedItem);
                                        categoriaS = "C";
                                        toast = Toast.makeText(getApplicationContext(), categoriaS, Toast.LENGTH_SHORT);
                                        toast.show();
                                        url += "?category="+categoriaS;
                                        break;
                                    case 2: //ocio
                                        Log.i("asdse", selectedItem);
                                        categoriaS = "O";
                                        toast = Toast.makeText(getApplicationContext(), categoriaS, Toast.LENGTH_SHORT);
                                        toast.show();
                                        url += "?category="+categoriaS;

                                        break;
                                    case 3: // mantenimiento
                                        Log.i("asdse", selectedItem);
                                        categoriaS = "M";
                                        toast = Toast.makeText(getApplicationContext(), categoriaS, Toast.LENGTH_SHORT);
                                        toast.show();
                                        url += "?category="+categoriaS;

                                        break;
                                    case 4: // eventos
                                        Log.i("asdse", selectedItem);
                                        categoriaS = "E";
                                        toast = Toast.makeText(getApplicationContext(), categoriaS, Toast.LENGTH_SHORT);
                                        toast.show();
                                        url += "?category="+categoriaS;

                                        break;
                                    case 5: // turismo
                                        Log.i("asdse", selectedItem);
                                        categoriaS = "T";
                                        toast = Toast.makeText(getApplicationContext(), categoriaS, Toast.LENGTH_SHORT);
                                        toast.show();
                                        url += "?category="+categoriaS;

                                        break;
                                    case 6: // deportes
                                        Log.i("asdse", selectedItem);
                                        categoriaS = "D";
                                        toast = Toast.makeText(getApplicationContext(), categoriaS, Toast.LENGTH_SHORT);
                                        toast.show();
                                        url += "?category="+categoriaS;

                                        break;
                                    case 7: // quejas
                                        Log.i("asdse", selectedItem);
                                        categoriaS = "Q";
                                        toast = Toast.makeText(getApplicationContext(), categoriaS, Toast.LENGTH_SHORT);
                                        toast.show();
                                        url += "?category="+categoriaS;

                                        break;
                                    case 8: // soporte
                                        Log.i("asdse", selectedItem);
                                        categoriaS = "S";
                                        toast = Toast.makeText(getApplicationContext(), categoriaS, Toast.LENGTH_SHORT);
                                        toast.show();
                                        url += "?category="+categoriaS;

                                        break;
                                }

                                new GetTokenAsyncTask(url, mainContext) {

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
                                                propostes = new ArrayList<>();

                                                if (ArrayProp != null) {
                                                    for (int i=0; i < ArrayProp.length(); i++){

                                                        Log.i("asd123", (ArrayProp.get(i).toString()));

                                                        JSONObject jas = ArrayProp.getJSONObject(i);
                                                        String title = jas.getString("title");
                                                        String owner = jas.getString("owner");
                                                        String description = jas.getString("content");
                                                        Integer id = jas.getInt("id");
                                                        String ca = jas.getString("categoria");
                                                        Double lat = jas.getJSONObject("location").getDouble("lat");
                                                        Double lng = jas.getJSONObject("location").getDouble("long");

                                                        Proposal aux = new Proposal(id, title, description, owner, ca, lat, lng);

                                                        propostes.add(aux);
                                                    }
                                                }
                                                llista_propostes.setAdapter(new ProposalAdapter(propostes, getApplicationContext()));
                                            }
                                        } catch (JSONException e ) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.execute(Jason);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        break;


                    case 2: // USUARIOS
                        Log.i("asdse", selectedItem);

                        buscartext.setVisibility(View.VISIBLE);
                        searchSpinner.setVisibility(View.VISIBLE);


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
                                                Toast toast = Toast.makeText(getApplicationContext(), username, Toast.LENGTH_SHORT);
                                                toast.show();

                                            }
                                        }

                                        ArrayAdapter<String> opcionsSpinnerAdapter2 = new ArrayAdapter<>(V.getContext(), R.layout.spinner_filter_style, usuaris);
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

                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.men_castella) {
            locale = new Locale("es");
            config.locale = locale;
            Constants.Idioma = "es";
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();
        }

        else if (id == R.id.men_catala){
            locale = new Locale("ca");
            config.locale = locale;
            Constants.Idioma = "ca";
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();

        }

        else if (id == R.id.men_angles){
            locale = new Locale("en");
            config.locale = locale;
            Constants.Idioma = "en";
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
    public void ferGetAsyncTask() {
        new GetTokenAsyncTask("https://agora-pes.herokuapp.com/api/proposal", this) {

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
                        propostes = new ArrayList<>();

                        if (ArrayProp != null) {
                            for (int i=0; i < ArrayProp.length(); i++){
                                Proposal aux;

                                Log.i("asd123", (ArrayProp.get(i).toString()));

                                JSONObject jas = ArrayProp.getJSONObject(i);
                                String title = jas.getString("title");
                                String owner = jas.getString("owner");
                                String description = jas.getString("content");
                                Integer id = jas.getInt("id");
                                String ca = jas.getString("categoria");
                                if(jas.has("location") && jas.getJSONObject("location").has("lat") && jas.getJSONObject("location").get("lat") != JSONObject.NULL ) {
                                    Double lat = jas.getJSONObject("location").getDouble("lat");
                                    Double lng = jas.getJSONObject("location").getDouble("long");
                                    aux = new Proposal(id, title, description, owner, ca, lat, lng);
                                }else {
                                     aux = new Proposal(id, title, description, owner, ca);
                                }

                                propostes.add(aux);
                            }
                        }
                        llista_propostes.setAdapter(new ProposalAdapter(propostes, getApplicationContext()));
                    }
                } catch (JSONException e ) {
                    e.printStackTrace();
                }
            }
        }.execute(Jason);
    }
}

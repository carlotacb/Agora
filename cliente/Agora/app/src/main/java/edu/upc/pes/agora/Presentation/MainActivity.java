package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
import java.util.Objects;
import java.util.Random;

import edu.upc.pes.agora.Logic.Constants;
import edu.upc.pes.agora.Logic.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.NavMenuListener;
import edu.upc.pes.agora.Logic.ProposalAdapter;
import edu.upc.pes.agora.Logic.Proposals;
import edu.upc.pes.agora.R;

import static edu.upc.pes.agora.Logic.Constants.SH_PREF_NAME;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private Configuration config = new Configuration();
    private Locale locale;
    private JSONObject Jason = new JSONObject();
    private ListView llista_propostes;
    private ArrayList<Proposals> propostes;
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

        /*Toast toast = Toast.makeText(getApplicationContext(),"estoy en el create" , Toast.LENGTH_SHORT);
        toast.show();*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_main);
        toolbar.setLogo(R.mipmap.ic_homew);
        setSupportActionBar(toolbar);

        mainContext = getApplicationContext();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        TextView headerUserName = (TextView) navigationView.findViewById(R.id.head_username);
        headerUserName.setText(Constants.Username);

        navigationView.getMenu().getItem(NavMenuListener.homneButton).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavMenuListener(this, drawer));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProposalsActivity.class));

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
                        /* Toast toast = Toast.makeText(getApplicationContext(),"estoy en el run" , Toast.LENGTH_SHORT);
                        toast.show();*/
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

        usuaris.add("Usuario1");
        usuaris.add("Usuario2");
        usuaris.add("Usuario3");
        usuaris.add("Usuario4");
        usuaris.add("Usuario5");
        usuaris.add("Usuario6");
        usuaris.add("Usuario7");


        categories.add(res.getString(R.string.cultura));
        categories.add(res.getString(R.string.deportes));
        categories.add(res.getString(R.string.ocio));
        categories.add(res.getString(R.string.mantenimiento));
        categories.add(res.getString(R.string.eventos));
        categories.add(res.getString(R.string.turismo));
        categories.add(res.getString(R.string.quejas));
        categories.add(res.getString(R.string.soporte));

        ArrayAdapter<String> filterSpinnerAdapter = new ArrayAdapter<>(this, R.layout.filter_spinner_style, opcions);
        filterSpinnerAdapter.setDropDownViewResource(R.layout.filter_spinner_style);
        filterSpinner.setAdapter(filterSpinnerAdapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = filterSpinner.getSelectedItem().toString().toLowerCase();

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

                        ArrayAdapter<String> opcionsSpinnerAdapter = new ArrayAdapter<>(view.getContext(), R.layout.filter_spinner_style, categories);
                        opcionsSpinnerAdapter.setDropDownViewResource(R.layout.filter_spinner_style);
                        searchSpinner.setAdapter(opcionsSpinnerAdapter);
                        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedItem = searchSpinner.getSelectedItem().toString().toLowerCase();
                                switch (position) {
                                    case 0: //cultura
                                        Log.i("asdse", selectedItem);
                                        break;
                                    case 1: //deportes
                                        Log.i("asdse", selectedItem);
                                        break;
                                    case 2: //ocio
                                        Log.i("asdse", selectedItem);
                                        break;
                                    case 3: // mantenimiento
                                        Log.i("asdse", selectedItem);
                                        break;
                                    case 4: // eventos
                                        Log.i("asdse", selectedItem);
                                        break;
                                    case 5: // turismo
                                        Log.i("asdse", selectedItem);
                                        break;
                                    case 6: // quejas
                                        Log.i("asdse", selectedItem);
                                        break;
                                    case 7: // soporte
                                        Log.i("asdse", selectedItem);
                                        break;
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
                        searchSpinner.setVisibility(View.VISIBLE);

                        ArrayAdapter<String> opcionsSpinnerAdapter2 = new ArrayAdapter<>(view.getContext(), R.layout.filter_spinner_style, usuaris);
                        opcionsSpinnerAdapter2.setDropDownViewResource(R.layout.filter_spinner_style);
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
        if(Constants.Idioma.equals("es")){
            bandera.setIcon(R.drawable.spa);
        }
        else if(Constants.Idioma.equals("en")){
            bandera.setIcon(R.drawable.ing);
        }
        else if(Constants.Idioma.equals("ca")){
            bandera.setIcon(R.drawable.rep);
        }

        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

                                Log.i("asd123", (ArrayProp.get(i).toString()));

                                JSONObject jas = ArrayProp.getJSONObject(i);
                                String title = jas.getString("title");
                                String owner = jas.getString("owner");
                                String description = jas.getString("content");
                                Integer id = jas.getInt("id");

                                Proposals aux = new Proposals(id, title, description, owner);

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

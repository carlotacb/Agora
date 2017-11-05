package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import edu.upc.pes.agora.Logic.GetAsyncTask;
import edu.upc.pes.agora.Logic.NavMenuListener;
import edu.upc.pes.agora.Logic.Proposals;
import edu.upc.pes.agora.Logic.ProposalsAdapter;
import edu.upc.pes.agora.R;

public class MainActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private Locale locale;
    private JSONObject Jason = new JSONObject();
    private ListView llista_propostes;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_main);
        toolbar.setLogo(R.mipmap.ic_homew);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView.getMenu().getItem(NavMenuListener.homneButton).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavMenuListener(this, drawer));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        llista_propostes = (ListView) findViewById(R.id.list);

        Log.i("asd123", "abans del getasync");

        // TODO: añadir asyncTask con el GET correspondiente para sacar las propuestas de la DB

        new GetAsyncTask("https://agora-pes.herokuapp.com/api/proposal", this) {

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                Log.i("asd123", "abans del try");
                try {
                    Log.i("asd123", "entra al try");
                    if (jsonObject.has("error")) {
                        String error = jsonObject.get("error").toString();
                        Log.i("asd123", "Error");

                        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    else if (jsonObject != null){
                        Log.i("asd123", "entra al elseif1");
                        JSONArray ArrayProp = jsonObject.getJSONArray("arrayResponse");
                        //JSONArray ArrayPropOwner = jsonObject.getJSONArray("owner");
                        //JSONArray ArrayPropDescription = jsonObject.getJSONArray("description");
                        Log.i("asd123", "entra al elseif");
                        ArrayList<Proposals> propostes = new ArrayList<>();


                        if (ArrayProp != null) {
                            for (int i=0; i < ArrayProp.length(); i++){

                                Log.i("asd123", (ArrayProp.get(i).toString()));

                                JSONObject jas = ArrayProp.getJSONObject(i);
                                Log.i("asd123", "1");
                                String title = jas.getString("title");
                                Log.i("asd123", title);
                                String owner = jas.getString("owner");
                                Log.i("asd123", owner);
                                String description = jas.getString("content");
                                Log.i("asd123", description);

                                Proposals aux = new Proposals(title, description, owner);
                                Log.i("asd123", "creada proposta");

                                /*aux.setDescription(description);
                                Log.i("asd123", "posa description");
                                aux.setOwner(owner);
                                Log.i("asd123", "posa owner");
                                aux.setTitle(title);
                                Log.i("asd123", "posa title");*/

                                propostes.add(aux);

                            }
                        }

                        llista_propostes.setAdapter(new ProposalsAdapter(getApplicationContext(), propostes));
                        Log.i("asd123", "nothing");
                    }
                } catch (JSONException e) {
                    Log.i("asd123", "entra al catch");
                    e.printStackTrace();
                }
            }
        }.execute(Jason);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // TODO: el BACK ha d'obrir el drawer
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_search_menu, menu);

        // TODO: fer el search funcional (Sprint 3...)

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //TODO: posar-ho al MenuListener

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
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();
        }

        else if (id == R.id.men_catala){
            locale = new Locale("ca");
            config.locale = locale;
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();

        }

        else if (id == R.id.men_angles){
            locale = new Locale("en");
            config.locale = locale;
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }


}

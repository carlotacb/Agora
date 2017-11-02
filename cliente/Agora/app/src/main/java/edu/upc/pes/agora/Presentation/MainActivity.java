package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import edu.upc.pes.agora.Logic.NavMenuListener;
import edu.upc.pes.agora.Logic.PostAsyncTask;
import edu.upc.pes.agora.R;

public class MainActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private Locale locale;

    final Resources res = this.getResources();

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

        /*new PostAsyncTask("http://sandshrew.fib.upc.es:3000/api/proposal", MainActivity.this) {
            @Override
            protected void onPostExecute(JSONObject resObject) {
                Boolean result = false;
                String error = res.getString(R.string.errorCreacion);

                try {
                    if (resObject.has("success"))
                        result = resObject.getBoolean("success");
                    if (!result && resObject.has("errorMessage"))
                        error = res.getString(R.string.errorCreacion);

                    Toast.makeText(getApplicationContext(), "Result : " + result , Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("asdBool", result.toString());

                if (result) {
                    Toast.makeText(getApplicationContext(), "Titulo : " + Titulo + " Descripcion : " + Descripcion, Toast.LENGTH_LONG).show();

                    startActivity(new Intent(propuestaActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "FUCKED", Toast.LENGTH_LONG).show();

                    Log.i("asd", "gfgffgfgf");
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                    Titulo.setText("");
                    Descripcion.setText("");
                }

            }
        }.execute(values);*/


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

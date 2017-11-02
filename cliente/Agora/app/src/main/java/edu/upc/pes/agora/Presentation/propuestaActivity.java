package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import edu.upc.pes.agora.Logic.DrawerToggleAdvanced;
import edu.upc.pes.agora.Logic.NavMenuListener;
import edu.upc.pes.agora.Logic.PostAsyncTask;
import edu.upc.pes.agora.R;

public class propuestaActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private Locale locale;

    private Button Reset;
    private Button Create;

    private TextView Titulo;
    private TextView Descripcion;

/*    private Configuration config = new Configuration();
    private Locale locale;
  */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propuesta);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navMenu);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        navigationView.getMenu().getItem(NavMenuListener.addproposalbutton).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavMenuListener(this, drawer));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nuevapropuesta);
        toolbar.setLogo(R.mipmap.ic_addw);
        setSupportActionBar(toolbar);

        DrawerToggleAdvanced toggle = new DrawerToggleAdvanced(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Reset = (Button) findViewById(R.id.resetButton);
        Create = (Button) findViewById(R.id.createButton);

        Titulo = (TextView) findViewById(R.id.titulo);
        Descripcion = (TextView) findViewById(R.id.descripcion);

        final Resources res = this.getResources();

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Titulo.setText("");
                Descripcion.setText("");

            }
        });

        Create.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                if (Titulo.getText()=="" ){
                    String error = res.getString(R.string.errorTitulo);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
                else if (Descripcion.getText()==""){
                    String error = res.getString(R.string.errorDescripcion);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
                else {
                    JSONObject values = new JSONObject();
                    try {
                        values.put("title", Titulo);
                        values.put("content", Descripcion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new PostAsyncTask("http://sandshrew.fib.upc.es:3000/api/proposal", propuestaActivity.this) {
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
                    }.execute(values);

                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

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

        Intent refresh = new Intent(this, propuestaActivity.class);
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

package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.text.SimpleDateFormat;


import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.Logic.Listeners.DrawerToggleAdvanced;
import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.Listeners.NavMenuListener;
import edu.upc.pes.agora.Logic.Models.Profile;
import edu.upc.pes.agora.Logic.Utils.Helpers;
import edu.upc.pes.agora.R;

public class MyProfileActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private Locale locale;
    private JSONObject Jason = new JSONObject();
    private ImageButton editar;

    private TextView username, name, CP, Born, neigh, sex;
    private Profile p = new Profile();

    private String usernameJ, neighJ, nameJ, BornJ, sexJ;
    private Integer CPJ;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navMenu);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        navigationView.getMenu().getItem(NavMenuListener.profile).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavMenuListener(this, drawer));

        TextView headerUserName = (TextView) navigationView.findViewById(R.id.head_username);
        headerUserName.setText(Constants.Username);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_profile);
        toolbar.setLogo(R.mipmap.ic_personw);
        setSupportActionBar(toolbar);

        DrawerToggleAdvanced toggle = new DrawerToggleAdvanced(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        editar = (ImageButton) findViewById(R.id.editarperfil);

        username = (TextView) findViewById(R.id.user);
        name = (TextView) findViewById(R.id.nameprofile);
        neigh = (TextView) findViewById(R.id.barrio);
        CP = (TextView) findViewById(R.id.codipostal);
        Born = (TextView) findViewById(R.id.born);
        sex = (TextView) findViewById(R.id.sexo);

        @SuppressLint("SimpleDateFormat") final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final String dateInString = "07/06/2013";

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

                        usernameJ = jsonObject.getString("username");
                        username.setText(usernameJ);
//                        p.setUsername(usernameJ);

                        if(jsonObject.has("realname")) {
                            nameJ = jsonObject.getString("realname");
                            name.setText(nameJ);
                            p.setName(nameJ);
                        }
                        else {
                            name.setText("");
                        }

                        if(jsonObject.has("neighborhood")) {
                            neighJ = jsonObject.getString("neighborhood");
                            neigh.setText(neighJ);
                            p.setNeighborhood(neighJ);
                        }
                        else {
                            neigh.setText("");
                        }

                        if(jsonObject.has("cpCode")) {
                            CPJ = jsonObject.getInt("cpCode");
                            CP.setText(CPJ.toString());
                            p.setCP(CPJ);
                        }
                        else {
                            CP.setText("");
                        }

                        if(jsonObject.has("bdate")) {
                            BornJ = jsonObject.getString("bdate");
                            Born.setText(Helpers.showDate(BornJ));
                            p.setBorn(Helpers.showDate(BornJ));
                        }
                        else {
                            Born.setText("");
                        }

                        if(jsonObject.has("sex")) {
                            sexJ = jsonObject.getString("sex");
                            if (sexJ.equals("I")) sex.setText(R.string.I);
                            else if (sexJ.equals("M")) sex.setText(R.string.M);
                            else if (sexJ.equals("F")) sex.setText(R.string.F);

                            p.setSex(sexJ);
                        }
                        else {
                            sex.setText("");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(Jason);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
                myIntent.putExtra("cp", p.getCP()); 
                myIntent.putExtra("barrio", p.getNeighborhood());
                myIntent.putExtra("nombre", p.getName());
                myIntent.putExtra("fecha", p.getBorn());
                myIntent.putExtra("sex", p.getSex());

                startActivity(myIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //TODO: posar-ho al MenuListener

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent refresh = new Intent(this, MyProfileActivity.class);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // TODO: el boto Back ha d'obrir el navigation drawer.
            drawer.openDrawer(GravityCompat.START);
        }
    }

}

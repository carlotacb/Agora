package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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
        headerUserName.setText(Constants.Username);
        ImageView foto = (ImageView) navigationView.findViewById(R.id.navigationPic);
        foto.setImageBitmap(Constants.fotoperfil);

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
                                String title = jas.getString("title");
                                String owner = jas.getString("owner");
                                String description = jas.getString("content");
                                Integer id = jas.getInt("id");
                                String creada = jas.getString("createdDateTime");
                                String ca = jas.getString("categoria");
                                String createDate = Helpers.showDate(jas.getString("createdDateTime"));
                                String updateDate = Helpers.showDate(jas.getString("updatedDateTime"));


                                if(jas.has("location") && jas.getJSONObject("location").has("lat") && jas.getJSONObject("location").get("lat") != JSONObject.NULL ) {
                                    Double lat = jas.getJSONObject("location").getDouble("lat");
                                    Double lng = jas.getJSONObject("location").getDouble("long");
                                    aux = new Proposal(id, title, description, owner, ca, lat, lng, createDate, updateDate);
                                }else {
                                    aux = new Proposal(id, title, description, owner, ca, createDate, updateDate);
                                }

                                Log.i("asdCreate", creada);
                                propostes.add(aux);
                            }
                        }

                        radapter = new RecyclerAdapter(propostes, getApplicationContext());
                        myrecycler.setAdapter(radapter);
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
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
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent refresh = new Intent(this, MyProposalsActivity.class);
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

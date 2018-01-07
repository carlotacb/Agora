package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private JSONObject Jason = new JSONObject();
    private Button editar;

    private TextView username, name, CP, Born, neigh, sex, descripcion;
    private ImageView image;
    private Profile p = new Profile();

    private String neighJ, nameJ, BornJ, sexJ, descriptionJ;
    private String imageJ;
    private Integer CPJ;

    private LinearLayout loading, pagina;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_profile);
        toolbar.setLogo(R.mipmap.ic_personw);
        setSupportActionBar(toolbar);

        DrawerToggleAdvanced toggle = new DrawerToggleAdvanced(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        username = (TextView) findViewById(R.id.user);
        name = (TextView) findViewById(R.id.nameprofile);
        neigh = (TextView) findViewById(R.id.barrio);
        CP = (TextView) findViewById(R.id.codipostal);
        Born = (TextView) findViewById(R.id.born);
        sex = (TextView) findViewById(R.id.sexo);
        descripcion = (TextView) findViewById(R.id.description);
        image = (ImageView) findViewById(R.id.setImage);
        editar = (Button) findViewById(R.id.editarperfil);
        pagina = (LinearLayout) findViewById(R.id.pantallaperfil);
        loading = (LinearLayout) findViewById(R.id.pantallacargando);


        new GetTokenAsyncTask("https://agora-pes.herokuapp.com/api/profile", this) {

            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                pagina.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);

                try {
                    if (jsonObject.has("error")) {
                        String error = jsonObject.get("error").toString();
                        Log.i("asdProfile", "Error");

                        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    else {

                        Log.i("asdProfile", (jsonObject.toString()));

                        username.setText(Constants.Username);

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
                            CP.setText(String.valueOf(CPJ));
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
                            switch (sexJ) {
                                case "I":
                                    sex.setText(R.string.I);
                                    break;
                                case "M":
                                    sex.setText(R.string.M);
                                    break;
                                case "F":
                                    sex.setText(R.string.F);
                                    break;
                            }

                            p.setSex(sexJ);
                            Log.i("asd123", sexJ);

                        }
                        else {
                            sex.setText("");
                        }

                        if (jsonObject.has("image")) {
                            imageJ = jsonObject.getString("image");

                            if (!imageJ.equals("null")) {
                                byte[] imageAsBytes = Base64.decode(imageJ.getBytes(), Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

                                image.setImageBitmap(bitmap);
                                p.setImatge(bitmap);
                            }
                        }

                        if (jsonObject.has("description")) {
                            descriptionJ = jsonObject.getString("description");
                            if (!descriptionJ.equals("null")) {
                                descripcion.setText(descriptionJ);
                                p.setDescription(descriptionJ);
                            }
                            else {
                                descripcion.setText("");
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pagina.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

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
                myIntent.putExtra("sexof", p.getSex());
                myIntent.putExtra("descripcion", p.getDescription());
                myIntent.putExtra("image", p.getImatge());

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

        Intent refresh = new Intent(this, MyProfileActivity.class);
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

}

package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import edu.upc.pes.agora.Logic.Constants;
import edu.upc.pes.agora.Logic.DrawerToggleAdvanced;
import edu.upc.pes.agora.Logic.NavMenuListener;
import edu.upc.pes.agora.Logic.PostAsyncTask;
import edu.upc.pes.agora.R;

public class ProposalsActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private Locale locale;

    private Button Reset;
    private Button Create;

    private TextView Titulo;
    private TextView Descripcion;

    private TextInputLayout errortitulo, errordescripcion;

    private ProgressBar prog;
    private ImageView canviidioma, enrerre;

    String strTitulo;
    String strDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal);

        Reset = (Button) findViewById(R.id.resetButton);
        Create = (Button) findViewById(R.id.createButton);

        Titulo = (TextView) findViewById(R.id.titulo);
        Descripcion = (TextView) findViewById(R.id.descripcion);
        errortitulo = (TextInputLayout) findViewById(R.id.titulo_up);
        errordescripcion = (TextInputLayout) findViewById(R.id.descripcion_up);

        prog = (ProgressBar) findViewById(R.id.crproposalprogressbar);

        final Resources res = this.getResources();

        canviidioma = (ImageView) findViewById(R.id.multiidiomareg);
        enrerre = (ImageView) findViewById(R.id.backbutton);

        if (Constants.Idioma.equals("ca")) {
            canviidioma.setImageResource(R.drawable.rep);
        }

        else if (Constants.Idioma.equals("es")) {
            canviidioma.setImageResource(R.drawable.spa);
        }

        else if (Constants.Idioma.equals("en")) {
            canviidioma.setImageResource(R.drawable.ing);
        }

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Titulo.setText("");
                Descripcion.setText("");
                strDescripcion = "";
                strTitulo = "";

            }
        });

        Create.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                strTitulo = Titulo.getText().toString();
                strDescripcion = Descripcion.getText().toString();

                if (strTitulo.equals("") ){
                    errortitulo.setErrorEnabled(true);
                    errortitulo.setError("Campo necesario");
                    errordescripcion.setErrorEnabled(false);
                    String error = res.getString(R.string.errorTitulo);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
                else if (strDescripcion.equals("")){
                    errordescripcion.setErrorEnabled(true);
                    errordescripcion.setError("Campo necesario");
                    errortitulo.setErrorEnabled(false);
                    String error = res.getString(R.string.errorDescripcion);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }

                else {

                    Create.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);

                    JSONObject values = new JSONObject();

                    try {
                        values.put("title", strTitulo);
                        values.put("content", strDescripcion);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new PostAsyncTask("https://agora-pes.herokuapp.com/api/proposal", ProposalsActivity.this) {
                        @Override
                        protected void onPostExecute(JSONObject resObject) {
                            Boolean result = false;
                            String error = res.getString(R.string.errorCreacion);

                            try {

                                if (resObject.has("success")) {
                                    result = resObject.getBoolean("success");
                                }

                                if (!result && resObject.has("errorMessage")) {
                                    error = res.getString(R.string.errorCreacion);
                                    Log.i("asdCreacion", error);
                                    Toast.makeText(getApplicationContext(), error , Toast.LENGTH_LONG).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String creacionok = String.format(res.getString(R.string.done), strTitulo);

                            if (result) {
                                Toast.makeText(getApplicationContext(), creacionok, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ProposalsActivity.this, MainActivity.class));
                            }

                            else {
                                Log.i("asdCreacion", "reset");
                                Titulo.setText("");
                                Descripcion.setText("");
                                Create.setVisibility(View.VISIBLE);
                                prog.setVisibility(View.GONE);
                            }
                        }
                    }.execute(values);
                }
            }
        });

        canviidioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent refresh = new Intent(ProposalsActivity.this, ProposalsActivity.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PopupMenu popupMenu = new PopupMenu(v.getRootView().getContext(), canviidioma);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.men_castella:
                                Constants.Idioma = "es";
                                break;

                            case R.id.men_catala:
                                Constants.Idioma = "ca";
                                break;

                            case R.id.men_angles:
                                Constants.Idioma = "en";
                                break;
                        }

                        locale = new Locale(Constants.Idioma);
                        config.locale = locale;
                        getResources().updateConfiguration(config, null);
                        startActivity(refresh);
                        finish();

                        return false;
                    }
                });
                popupMenu.inflate(R.menu.idioma);
                popupMenu.show();

            }
        });

        enrerre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log = new Intent(ProposalsActivity.this, MainActivity.class);
                log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(log);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent log = new Intent(ProposalsActivity.this, MainActivity.class);
        log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(log);
    }
}

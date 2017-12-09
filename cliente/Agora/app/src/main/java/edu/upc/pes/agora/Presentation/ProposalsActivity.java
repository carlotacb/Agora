package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import edu.upc.pes.agora.Logic.Constants;
import edu.upc.pes.agora.Logic.PostAsyncTask;
import edu.upc.pes.agora.R;

public class ProposalsActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private Locale locale;

    private Button Reset;
    private Button Create;
    private Button addPos;
    private Button deletePos;

    private TextView Titulo;
    private TextView Descripcion;
    private TextView txtPosAttached;
    private TextInputLayout errortitulo, errordescripcion;
    private Spinner spinC;

    private ProgressBar prog;
    private ImageView canviidioma, enrerre;

    String strTitulo;
    String strDescripcion;
    private double lat;
    private double lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal);

        Reset = (Button) findViewById(R.id.resetButton);
        Create = (Button) findViewById(R.id.createButton);
        addPos = (Button) findViewById(R.id.btnAddPosition);
        deletePos = (Button) findViewById(R.id.btnDeletePosition);


        Titulo = (TextView) findViewById(R.id.titulo);
        Descripcion = (TextView) findViewById(R.id.descripcion);
        errortitulo = (TextInputLayout) findViewById(R.id.titulo_up);
        errordescripcion = (TextInputLayout) findViewById(R.id.descripcion_up);

        txtPosAttached = (TextView) findViewById(R.id.txtPosAttached);

        if (getIntent().hasExtra("Title")){
            Titulo.setText(getIntent().getStringExtra("Title"));
        }
        if (getIntent().hasExtra("Description")){
            Descripcion.setText(getIntent().getStringExtra("Description"));
        }
        if(getIntent().hasExtra("lat") && getIntent().hasExtra("lng")){
            lat = getIntent().getDoubleExtra("lat",0);
            lng = getIntent().getDoubleExtra("lng",0);
            txtPosAttached.setText(R.string.posAttached);
            deletePos.setVisibility(View.VISIBLE);
        }else{
            lat = 0;
            lng = 0;
            deletePos.setVisibility(View.INVISIBLE);
        }
        spinC = (Spinner) findViewById(R.id.CategorySpinner);

        /*cultura
        deportes
        ocio
        mantenimiento
        eventos
        turismo
        quejas
        soporte*/

        String[] diferentesCategoriasGenerico = {"C", "D", "O","M", "E", "T","Q", "S"};

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter<String> aa = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,diferentesCategoriasGenerico);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinC.setAdapter(aa);

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
                    JSONObject location = new JSONObject();

                    try {
                        values.put("title", strTitulo);
                        values.put("content", strDescripcion);
                        location.put("lat", getIntent().getDoubleExtra("lat",0));
                        location.put("long", getIntent().getDoubleExtra("lng",0));
                        values.put("location", location);
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
                                    if(resObject.getString("errorMessage") == ""){ //TODO: Add errorMessage received if position is out of neighbourhood
                                        Toast.makeText(getApplicationContext(), res.getString(R.string.errorPosition), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                                    }
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

        addPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddLocationActivity.class);
                i.putExtra("Title", Titulo.getText().toString());
                i.putExtra("Description", Descripcion.getText().toString());
                startActivity(i);
            }
        });

        deletePos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lat = 0;
                lng = 0;
                txtPosAttached.setText("");
                deletePos.setVisibility(View.INVISIBLE);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent log = new Intent(ProposalsActivity.this, MainActivity.class);
        log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(log);
    }
}

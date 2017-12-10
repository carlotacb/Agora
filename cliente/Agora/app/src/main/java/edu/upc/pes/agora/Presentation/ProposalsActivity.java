package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import edu.upc.pes.agora.Logic.BackOnClickListener;
import edu.upc.pes.agora.Logic.Constants;
import edu.upc.pes.agora.Logic.LanguageOnClickListener;
import edu.upc.pes.agora.Logic.PostAsyncTask;
import edu.upc.pes.agora.R;

public class ProposalsActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private Locale locale;

    private Button Create, deletePos;

    private TextView Titulo, Descripcion;/*txtPosAttached*/

    private TextInputLayout errortitulo, errordescripcion;

    private ProgressBar prog;

    private Spinner spin;

    private String strTitulo = "";
    private String strDescripcion = "";
    private String[] categoriasGenericas = {"C", "D", "O","M", "E", "T","Q", "S","A"};

    private double lat;
    private double lng;

    private LayoutInflater mInflator;
    private boolean selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal);

        Button reset = (Button) findViewById(R.id.resetButton);
        Create = (Button) findViewById(R.id.createButton);
        final Button addPos = (Button) findViewById(R.id.btnAddPosition);
        deletePos = (Button) findViewById(R.id.btnDeletePosition);

        ImageView canviidioma = (ImageView) findViewById(R.id.multiidiomareg);
        ImageView enrerre = (ImageView) findViewById(R.id.backbutton);

        spin = (Spinner) findViewById(R.id.cate);

        Titulo = (TextView) findViewById(R.id.titulo);
        Descripcion = (TextView) findViewById(R.id.descripcion);
        //txtPosAttached = (TextView) findViewById(R.id.txtPosAttached);

        errortitulo = (TextInputLayout) findViewById(R.id.titulo_up);
        errordescripcion = (TextInputLayout) findViewById(R.id.descripcion_up);


        prog = (ProgressBar) findViewById(R.id.crproposalprogressbar);

        final Resources res = this.getResources();

        // Lista de Categorias con los nombres buenos (cambian con el idioma).
        String[] categorias = new String[]{
                getString(R.string.cultura),
                getString(R.string.deportes),
                getString(R.string.ocio),
                getString(R.string.mantenimiento),
                getString(R.string.eventos),
                getString(R.string.turismo),
                getString(R.string.quejas),
                getString(R.string.soporte)};

        // Adapter para el Spinner:
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, categorias);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(categoriesAdapter);

        switch (Constants.Idioma) {
            case "ca":
                canviidioma.setImageResource(R.drawable.rep);
                break;
            case "es":
                canviidioma.setImageResource(R.drawable.spa);
                break;
            case "en":
                canviidioma.setImageResource(R.drawable.ing);
                break;
        }

        Intent idioma = new Intent(ProposalsActivity.this, ProposalsActivity.class);
        Intent back = new Intent(ProposalsActivity.this, MainActivity.class);
        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        canviidioma.setOnClickListener(new LanguageOnClickListener(idioma, canviidioma, res, getApplicationContext()));

        enrerre.setOnClickListener(new BackOnClickListener(back, getApplicationContext()));

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Titulo.setText("");
                Descripcion.setText("");
                //txtPosAttached.setText("");
            }
        });

        Create.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                strTitulo = Titulo.getText().toString();
                strDescripcion = Descripcion.getText().toString();

                errortitulo.getBackground().clearColorFilter();
                errordescripcion.getBackground().clearColorFilter();

                if (strTitulo.equals("") ){
                    errortitulo.setErrorEnabled(true);
                    errortitulo.setError(res.getString(R.string.fieldnecesary));
                    Titulo.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                    errordescripcion.setErrorEnabled(false);
                    String error = res.getString(R.string.errorTitulo);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
                else if (strDescripcion.equals("")){
                    errordescripcion.setErrorEnabled(true);
                    errordescripcion.setError(res.getString(R.string.fieldnecesary));
                    Descripcion.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
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
                        String ca = categoriasGenericas[spin.getSelectedItemPosition()];

                        values.put("title", strTitulo);
                        values.put("content", strDescripcion);
                        values.put("categoria", ca);
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
                            try {

                                if (resObject.has("success")) {
                                    result = resObject.getBoolean("success");
                                }

                                if (!result && resObject.has("errorMessage")) {
                                    String error = res.getString(R.string.errorCreacion);
                                    Log.i("asdCreacion", error);
                                    if(resObject.getString("errorMessage").equals("")){ //TODO: Add errorMessage received if position is out of neighbourhood
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
                deletePos.setVisibility(View.VISIBLE);
                addPos.setVisibility(View.GONE);
            }
        });

        deletePos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lat = 0;
                lng = 0;
                //txtPosAttached.setText("");
                deletePos.setVisibility(View.GONE);
                addPos.setVisibility(View.VISIBLE);
            }
        });

        if (getIntent().hasExtra("cat")){
            spin.setSelection(getIntent().getIntExtra("cat",0));
        }

        if (getIntent().hasExtra("Title")){
            Titulo.setText(getIntent().getStringExtra("Title"));
        }

        if (getIntent().hasExtra("Description")){
            Descripcion.setText(getIntent().getStringExtra("Description"));
        }

        if (getIntent().hasExtra("lat") && getIntent().hasExtra("lng")){
            lat = getIntent().getDoubleExtra("lat",0);
            lng = getIntent().getDoubleExtra("lng",0);
            //txtPosAttached.setText(R.string.posAttached);
            deletePos.setVisibility(View.VISIBLE);
        }

        else {
            lat = 0;
            lng = 0;
            deletePos.setVisibility(View.INVISIBLE);
        }
    }
}

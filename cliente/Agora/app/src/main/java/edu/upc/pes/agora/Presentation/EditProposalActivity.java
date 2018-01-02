package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.upc.pes.agora.Logic.Adapters.CommentAdapter;
import edu.upc.pes.agora.Logic.Adapters.ImatgesAdapter;
import edu.upc.pes.agora.Logic.Adapters.ImatgesEditAdapter;
import edu.upc.pes.agora.Logic.Adapters.RecyclerAdapter;
import edu.upc.pes.agora.Logic.Models.Comment;
import edu.upc.pes.agora.Logic.Models.ImatgeItem;
import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.ServerConection.PostAsyncTask;
import edu.upc.pes.agora.Logic.ServerConection.PutAsyncTask;
import edu.upc.pes.agora.R;

import static edu.upc.pes.agora.Logic.Utils.Constants.SH_PREF_NAME;

public class EditProposalActivity extends AppCompatActivity {

    EditText editTitle;
    EditText editDescription;
    Button editButton;
    Button saveButton;
    Button cancelButton;
    Button editPosButton;
    Spinner categories;
    LinearLayout locallayout;
    TextView esborrarlocalitzacio;
    TextView veurelocalitzacio;

    String newTitle;
    String newDescription;

    Double latitud, longitud;
    Integer proposalID;

    private ListView limatges;
    private String encoded;
    private ProgressBar prog;
    private final int SELECT_PICTURE=200;

    private JSONArray ArrayImages = new JSONArray();
    private ArrayList<ImatgeItem> mImatgeItems = new ArrayList<> ();
    private String[] categoriasGenericas = {"X", "C", "D", "O","M", "E", "T","Q", "S", "A"};

    private Integer idprop;
    private int numimatges = 0;
    private FloatingActionButton addImage, addLocation, menuFloating;
    private LinearLayout linearImage, linearLocation;
    private JSONObject Jason = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_proposal);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);
        categories = (Spinner) findViewById(R.id.editcategoria);
        locallayout = (LinearLayout) findViewById(R.id.layoutlocalization);
        esborrarlocalitzacio = (TextView) findViewById(R.id.deleteposition);
        veurelocalitzacio = (TextView) findViewById(R.id.seeposition);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        prog = (ProgressBar) findViewById(R.id.saveprogressbar);

        addImage = (FloatingActionButton) findViewById(R.id.fabimages);
        addLocation = (FloatingActionButton) findViewById(R.id.fablocalization);
        menuFloating = (FloatingActionButton) findViewById(R.id.faboptions);

        linearLocation = (LinearLayout) findViewById(R.id.localizationLayout);
        linearImage = (LinearLayout) findViewById(R.id.imagesLayout);

        //editButton = (Button) findViewById(R.id.editButton);
        //editPosButton = (Button) findViewById(R.id.editPosButton);

        final Animation showButton = AnimationUtils.loadAnimation(EditProposalActivity.this, R.anim.show_button);
        final Animation hideButton = AnimationUtils.loadAnimation(EditProposalActivity.this, R.anim.hide_button);
        final Animation showLayout = AnimationUtils.loadAnimation(EditProposalActivity.this, R.anim.show_layout);
        final Animation hideLayout = AnimationUtils.loadAnimation(EditProposalActivity.this, R.anim.hide_layout);

        limatges = (ListView) findViewById(R.id.llistaimatges);

        Intent i = getIntent();

        idprop = i.getIntExtra("id", 0);

        if(i.hasExtra("Title")) {
            editTitle.setText(i.getStringExtra("Title"));
        }
        if(i.hasExtra("Description")) {
            editDescription.setText(i.getStringExtra("Description"));
        }
        Integer selection = 0;
        if(i.hasExtra("Categoria")) {
            String categoriaProposta = i.getStringExtra("Categoria");

            if (categoriaProposta.equals("X")) selection = 1;
            else if (categoriaProposta.equals("C")) selection = 2;
            else if (categoriaProposta.equals("D")) selection = 3;
            else if (categoriaProposta.equals("O")) selection = 4;
            else if (categoriaProposta.equals("M")) selection = 5;
            else if (categoriaProposta.equals("E")) selection = 6;
            else if (categoriaProposta.equals("T")) selection = 7;
            else if (categoriaProposta.equals("Q")) selection = 8;
            else if (categoriaProposta.equals("S")) selection = 9;
            else if (categoriaProposta.equals("A")) selection = 10;

        }

        if (i.hasExtra("lat") && i.hasExtra("lng")) {

            latitud = i.getDoubleExtra("lat",0.0);
            longitud = i.getDoubleExtra("lng", 0.0);

            if (latitud != 0.0 && longitud != 0) {
                locallayout.setVisibility(View.VISIBLE);
            }
            else {
                locallayout.setVisibility(View.INVISIBLE);
            }

        }

        final int id = i.getIntExtra("id",0);
        proposalID = id;

        final Resources res = this.getResources();

        // Lista de Categorias con los nombres buenos (cambia con el idioma).
        final String[] categorias = new String[]{
                getString(R.string.spinnerhint),
                getString(R.string.cultura),
                getString(R.string.deportes),
                getString(R.string.ocio),
                getString(R.string.mantenimiento),
                getString(R.string.eventos),
                getString(R.string.turismo),
                getString(R.string.quejas),
                getString(R.string.soporte)};

        final List<String> categoriesList = new ArrayList<>(Arrays.asList(categorias));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_categories_layout, categoriesList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    // Disable the first item from Spinner. The first item will be use for hint
                    return false;
                }
                else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (categorias[position].equals(getString(R.string.spinnerhint))){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_categories_layout);
        categories.setAdapter(spinnerArrayAdapter);
        categories.setSelection(selection);

        llistarimatges();

        veurelocalitzacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ShowLocationActivity.class);
                if (getIntent().hasExtra("lat") && getIntent().hasExtra("lng")) {
                    i.putExtra("lat", getIntent().getDoubleExtra("lat",0));
                    i.putExtra("lng", getIntent().getDoubleExtra("lng",0));
                    startActivity(i);
                }
            }
        });

        esborrarlocalitzacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitud = 0.0;
                longitud = 0.0;

            }
        });

        menuFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (linearLocation.getVisibility() == View.VISIBLE && linearImage.getVisibility() == View.VISIBLE) {
                    linearLocation.setVisibility(View.GONE);
                    linearImage.setVisibility(View.GONE);
                    linearLocation.startAnimation(hideLayout);
                    linearImage.startAnimation(hideLayout);
                    menuFloating.startAnimation(hideButton);

                }
                else {
                    linearLocation.setVisibility(View.VISIBLE);
                    linearImage.setVisibility(View.VISIBLE);
                    linearLocation.startAnimation(showLayout);
                    linearImage.startAnimation(showLayout);
                    menuFloating.startAnimation(showButton);
                }
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLocation.setVisibility(View.GONE);
                linearImage.setVisibility(View.GONE);
                linearLocation.startAnimation(hideLayout);
                linearImage.startAnimation(hideLayout);
                menuFloating.startAnimation(hideButton);
                final CharSequence[] options = {"Galería", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditProposalActivity.this);
                builder.setTitle("Escoge una opción");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selection) {
                        if(options[selection]=="Galería") {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                        }
                        else if(options[selection]=="Cancelar"){
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLocation.setVisibility(View.GONE);
                linearImage.setVisibility(View.GONE);
                linearLocation.startAnimation(hideLayout);
                linearImage.startAnimation(hideLayout);
                menuFloating.startAnimation(hideButton);
                if (numimatges > 0) {
                    Toast.makeText(v.getContext(), "Perdras les imatges afegides", Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(getApplicationContext(), AddLocationActivity.class);
                i.putExtra("Title", editTitle.getText().toString());
                i.putExtra("Description", editDescription.getText().toString());
                i.putExtra("CallingActivity", "Edit");
                i.putExtra("Category",categories.getSelectedItemPosition());
                i.putExtra("id",id);
                if (getIntent().hasExtra("lat") && getIntent().getDoubleExtra("lat",0) != 0){
                    i.putExtra("lat", getIntent().getDoubleExtra("lat",0));
                    i.putExtra("lng", getIntent().getDoubleExtra("lng",0));
                }
                startActivityForResult(i,1);;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                if (editTitle.getText().toString().equals("") ){
                    String error = res.getString(R.string.errorTitulo);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
                else if (editDescription.getText().toString().equals("")){
                    String error = res.getString(R.string.errorDescripcion);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
                else {

                    saveButton.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);

                    JSONObject values = new JSONObject();
                    JSONObject location = new JSONObject();

                    try {
                        newTitle = editTitle.getText().toString();
                        newDescription = editDescription.getText().toString();
                        values.put("id",id);
                        values.put("title", newTitle);
                        values.put("content", newDescription);
                        location.put("lat", getIntent().getDoubleExtra("lat",0));
                        location.put("long", getIntent().getDoubleExtra("lng",0));
                        values.put("location", location);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String editUrl = "https://agora-pes.herokuapp.com/api/proposal/"+id;
                    Log.i("Link",editUrl);

                    // nou server : agora-pes.herokuapp.com/api/proposal
                    new PutAsyncTask(editUrl, EditProposalActivity.this){
                        protected void onPostExecute(JSONObject resObject) {
                            Boolean result = false;
                            String error = res.getString(R.string.errorEdit);

                            try {

                                if (resObject.has("success")) {
                                    result = resObject.getBoolean("success");
                                }
                                if (!result && resObject.has("errorMessage")) {
                                    error = resObject.getString("errorMessage");
                                    Log.i("asdCreacion", error);
                                    if(resObject.getString("errorMessage").equals("Selected location outside of allowed zone.")){
                                        Toast.makeText(getApplicationContext(), res.getString(R.string.errorPosition), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (result) {

                                if (numimatges > 0) {
                                    afegirimatges();
                                }
                            }

                            else {
                                Log.i("asdCreacion", "reset");
                                saveButton.setVisibility(View.VISIBLE);
                                prog.setVisibility(View.GONE);
                            }

                        }
                    }.execute(values);

                    Intent myIntent = new Intent(getApplicationContext(), MyProposalsActivity.class);
                    startActivity(myIntent);

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyProposalsActivity.class);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private void afegirimatges(){

        final Resources res = this.getResources();

        JSONObject imatgesperpropostes = new JSONObject();

        try {
            imatgesperpropostes.put("images", ArrayImages);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new PostAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + idprop + "/image", EditProposalActivity.this) {
            @Override
            protected void onPostExecute(JSONObject resObject) {
                Boolean result = false;

                Log.i("asdimatge", "entra aqui");

                try {
                    if (resObject.has("success")) {
                        result = resObject.getBoolean("success");
                    }

                    if (!result && resObject.has("errorMessage")) {
                        String error = res.getString(R.string.errorCreacion);
                        Log.i("asdCreacion", error);
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*String creacionok = String.format(res.getString(R.string.done), strTitulo);

                if (result) {
                    Toast.makeText(getApplicationContext(), creacionok, Toast.LENGTH_LONG).show();

                    startActivity(new Intent(EditProposalActivity.this, MainActivity.class));
                }

                else {
                    Create.setVisibility(View.VISIBLE);
                    prog.setVisibility(View.GONE);
                }*/
            }
        }.execute(imatgesperpropostes);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PICTURE:
                if(resultCode == RESULT_OK) {
                    Bitmap bitmap =null;
                    if (data != null) {
                        try {
                            bitmap  = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    ImatgeItem i = new ImatgeItem();

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    ArrayImages.put(encoded);

                    ++numimatges;
                    i.setImatge(encoded);
                    i.setNumero(mImatgeItems.size()+1);

                    afegirimatgellista(i);
                }
                break;
        }
    }

    private void afegirimatgellista(ImatgeItem im) {

        mImatgeItems.add(im);

        limatges.setAdapter(new ImatgesAdapter(getApplicationContext(), mImatgeItems));
    }

    @SuppressLint("StaticFieldLeak")
    private void llistarimatges() {
        new GetTokenAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + idprop, this) {

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    if (jsonObject.has("error")) {
                        String error = jsonObject.get("error").toString();
                        Log.i("asd123", "Error");

                        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    else if (jsonObject != null) {

                        JSONArray ArrayImages = jsonObject.getJSONArray("images");
                        ArrayList<ImatgeItem> imatges = new ArrayList<>();

                        if (ArrayImages != null) {
                            for (int i=0; i < ArrayImages.length(); i++){

                                Log.i("asd123", (ArrayImages.get(i).toString()));

                                JSONObject jas = ArrayImages.getJSONObject(i);
                                String id = jas.getString("id");
                                String contentimage = jas.getString("image");

                                ImatgeItem aux = new ImatgeItem();
                                aux.setNumero(Integer.parseInt(id));
                                aux.setImatge(contentimage);
                                aux.setIdproposta(proposalID);

                                mImatgeItems.add(aux);
                            }
                        }
                        limatges.setAdapter(new ImatgesEditAdapter(getApplicationContext(), mImatgeItems));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(Jason);
    }

}

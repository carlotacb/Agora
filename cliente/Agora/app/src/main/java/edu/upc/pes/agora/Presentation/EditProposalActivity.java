package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.InputFilter;
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

import edu.upc.pes.agora.Logic.Adapters.ImatgesAdapter;
import edu.upc.pes.agora.Logic.Adapters.ImatgesEditAdapter;
import edu.upc.pes.agora.Logic.Listeners.BackOnClickListener;
import edu.upc.pes.agora.Logic.Listeners.LanguageOnClickListener;
import edu.upc.pes.agora.Logic.Models.ImatgeItem;
import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.ServerConection.PostAsyncTask;
import edu.upc.pes.agora.Logic.ServerConection.PutAsyncTask;
import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.R;


public class EditProposalActivity extends AppCompatActivity {

    EditText editTitle;
    EditText editDescription;
    Button saveButton;
    Button cancelButton;
    Spinner categories;
    LinearLayout locallayout;
    TextView esborrarlocalitzacio;
    TextView veurelocalitzacio;

    String newTitle;
    String newDescription;
    String newCategories;

    Double latitud, longitud;
    Integer proposalID;

    private ListView limatges;
    private ProgressBar prog;
    private final int SELECT_PICTURE=200;

    private JSONArray ArrayImages = new JSONArray();
    private ArrayList<ImatgeItem> mImatgeItems = new ArrayList<> ();

    private Integer idprop;
    private int numimatges = 0;
    private FloatingActionButton menuFloating;
    private LinearLayout linearImage, linearLocation;
    private JSONObject Jason = new JSONObject();
    private String[] categoriasGenericas = {"X", "C", "D", "O","M", "E", "T","Q", "S", "A"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_proposal);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);
        editDescription.getBackground().clearColorFilter();
        editTitle.getBackground().clearColorFilter();
        editDescription.setFilters(new InputFilter[]{new InputFilter.LengthFilter(140)});
        categories = (Spinner) findViewById(R.id.editcategoria);
        locallayout = (LinearLayout) findViewById(R.id.layoutlocalization);
        esborrarlocalitzacio = (TextView) findViewById(R.id.deleteposition);
        veurelocalitzacio = (TextView) findViewById(R.id.seeposition);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        prog = (ProgressBar) findViewById(R.id.saveprogressbar);

        FloatingActionButton addImage = (FloatingActionButton) findViewById(R.id.fabimages);
        FloatingActionButton addLocation = (FloatingActionButton) findViewById(R.id.fablocalization);
        menuFloating = (FloatingActionButton) findViewById(R.id.faboptions);

        linearLocation = (LinearLayout) findViewById(R.id.localizationLayout);
        linearImage = (LinearLayout) findViewById(R.id.imagesLayout);

        limatges = (ListView) findViewById(R.id.llistaimatges);
        ImageView canviidioma = (ImageView) findViewById(R.id.multiidiomareg);
        ImageView enrerre = (ImageView) findViewById(R.id.backbutton);

        final Resources res = this.getResources();

        final Animation showButton = AnimationUtils.loadAnimation(EditProposalActivity.this, R.anim.show_button);
        final Animation hideButton = AnimationUtils.loadAnimation(EditProposalActivity.this, R.anim.hide_button);
        final Animation showLayout = AnimationUtils.loadAnimation(EditProposalActivity.this, R.anim.show_layout);
        final Animation hideLayout = AnimationUtils.loadAnimation(EditProposalActivity.this, R.anim.hide_layout);


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

        Intent i = getIntent();

        Intent idioma = new Intent(EditProposalActivity.this, EditProposalActivity.class);
        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        idioma.putExtra("Title", i.getStringExtra("Title"));
        idioma.putExtra("Description", i.getStringExtra("Description"));
        idioma.putExtra("id", i.getIntExtra("id",0));
        if (i.hasExtra("Categoria")) idioma.putExtra("Categoria", i.getStringExtra("Categoria"));
        if (i.hasExtra("Category")) idioma.putExtra("Category", i.getIntExtra("Category", 0));
        canviidioma.setOnClickListener(new LanguageOnClickListener(idioma, canviidioma, res, getApplicationContext()));

        Intent back = new Intent(EditProposalActivity.this, MyProposalsActivity.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        enrerre.setOnClickListener(new BackOnClickListener(back, getApplicationContext()));


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

            Log.i("asdaasd", categoriaProposta);

            switch (categoriaProposta) {
                case "X":
                    selection = 0;
                    break;
                case "C":
                    selection = 1;
                    break;
                case "D":
                    selection = 2;
                    break;
                case "O":
                    selection = 3;
                    break;
                case "M":
                    selection = 4;
                    break;
                case "E":
                    selection = 5;
                    break;
                case "T":
                    selection = 6;
                    break;
                case "Q":
                    selection = 7;
                    break;
                case "S":
                    selection = 8;
                    break;
            }

            Log.i("asdaasd", selection.toString());
        }

        if (i.hasExtra("Category")) {
            Log.i("asdaasd", "entraaqui");
            selection = i.getIntExtra("Category", 0);
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
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
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
                            startActivityForResult(Intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
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
                i.putExtra("Category",categories.getSelectedItemPosition());
                i.putExtra("CallingActivity", "Edit");
                i.putExtra("id",id);
                if (getIntent().hasExtra("lat") && getIntent().getDoubleExtra("lat",0) != 0){
                    i.putExtra("lat", getIntent().getDoubleExtra("lat",0));
                    i.putExtra("lng", getIntent().getDoubleExtra("lng",0));
                }
                startActivityForResult(i,1);
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
                        newCategories = categoriasGenericas[categories.getSelectedItemPosition()];
                        values.put("id", id);
                        values.put("title", newTitle);
                        values.put("content", newDescription);
                        values.put("categoria", newCategories);
                        location.put("lat", getIntent().getDoubleExtra("lat",0));
                        location.put("long", getIntent().getDoubleExtra("lng",0));
                        values.put("location", location);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String editUrl = "https://agora-pes.herokuapp.com/api/proposal/" + id;
                    Log.i("Link", editUrl);

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

                            String achievement = this.getNewAchievement();


                            if (result && achievement != null && !achievement.equals("")) {
                                sendNot(achievement);
                            }


                            if (result) {

                                if (numimatges > 0) {
                                    afegirimatges();
                                }

                                Intent myIntent;

                                if (getIntent().hasExtra("ChangeActivity")) {
                                    myIntent = new Intent(getApplicationContext(), DetailsProposalActivity.class);
                                    myIntent.putExtra("Title", newTitle);
                                    myIntent.putExtra("Description", newDescription);
                                    myIntent.putExtra("id", id);
                                    myIntent.putExtra("Owner", getIntent().getStringExtra("Owner"));
                                    myIntent.putExtra("Categoria", newCategories);
                                    myIntent.putExtra("lat", getIntent().getDoubleExtra("lat",0));
                                    myIntent.putExtra("lng", getIntent().getDoubleExtra("lng",0));
                                    myIntent.putExtra("Creation", getIntent().getStringExtra("Creation"));
                                    myIntent.putExtra("Update", getIntent().getStringExtra("Update"));
                                    myIntent.putExtra("ncomentarios", getIntent().getIntExtra("ncomentarios", 0));
                                    myIntent.putExtra("nvotes", getIntent().getIntExtra("nvotes", 0));
                                    myIntent.putExtra("nunvotes", getIntent().getIntExtra("nunvotes", 0));
                                    myIntent.putExtra("favorit", getIntent().getBooleanExtra("favorit", false));
                                    myIntent.putExtra("votacion", getIntent().getIntExtra("votacion", 0));
                                    if (getIntent().hasExtra("otherUser")) myIntent.putExtra("otherUser", "ve d'altre usuari");
                                    if (getIntent().hasExtra("deFavorites")) myIntent.putExtra("deFavorites", "ve d'altre usuari");
                                    if (getIntent().hasExtra("deMyProposals")) myIntent.putExtra("deMyProposals", "ve d'altre usuari");
                                }
                                else {
                                    myIntent = new Intent(getApplicationContext(), MyProposalsActivity.class);
                                }
                                startActivity(myIntent);
                            }

                            else {
                                Log.i("asdCreacion", "reset");
                                saveButton.setVisibility(View.VISIBLE);
                                prog.setVisibility(View.GONE);
                            }
                        }
                    }.execute(values);
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


                    String achievement = this.getNewAchievement();

                    if (result && achievement != null && !achievement.equals("")) {
                        sendNot(achievement);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                    if (bitmap != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    }
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

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


    public void sendNot(String achievement){

        Intent i=new Intent(EditProposalActivity.this, LogrosActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(EditProposalActivity.this, 0, i, 0);

        String[] parts = achievement.split(",");
        int count = parts.length;
        for ( int j = 0; j < count; j++ ){
            String decoded = codificaLogro(parts[j]);
            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.logo);

            NotificationCompat.Builder mBuilder;
            NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_trofeo_logro)
                    .setContentTitle(getString(R.string.nuevo))
                    .setLargeIcon(icon)
                    .setContentText(decoded)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(decoded))
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(true);

            mNotifyMgr.notify(j+1, mBuilder.build());
        }



    }

/*
    public void crear(String achievement) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateProposalActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_trophy, null);
        TextView textView = (TextView)mView.findViewById(R.id.textView);
        textView.setText(codificaLogro(achievement));
        Button mAccept = (Button) mView.findViewById(R.id.etAccept);
        ImageView imageView = (ImageView) mView.findViewById(R.id.image);
        imageView.setImageResource(R.drawable.ic_trofeo_logro2);
        mBuilder.setView(mView);
        //  mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                startActivity(new Intent(CreateProposalActivity.this, MainActivity.class));
            }
        });
        dialog.show();

        mAccept.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //    startActivity(new Intent(CreateProposalActivity.this, MainActivity.class));

            }
        });
    }*/
private String codificaLogro(String codigoLogro) {

     /*   String[] parts = codigoLogro.split(",");
        int count = parts.length;
        String[] Logros = new String[count];*/
    // for (int i = 0; i < count; i++){
    String Logro = "";
    switch(codigoLogro) {
        case "PROP1": Logro = getApplicationContext().getString(R.string.PROP1);
            break;
        case "PROP5": Logro = getApplicationContext().getString(R.string.PROP5);
            break;
        case "PROP10": Logro = getApplicationContext().getString(R.string.PROP10);
            break;
        case "PROP50": Logro = getApplicationContext().getString(R.string.PROP50);
            break;
        case "PROP100": Logro = getApplicationContext().getString(R.string.PROP100);
            break;
        case "FAV1": Logro = getApplicationContext().getString(R.string.FAV1);
            break;
        case "FAV10": Logro = getApplicationContext().getString(R.string.FAV10);
            break;
        case "UBI1": Logro = getApplicationContext().getString(R.string.UBI1);
            break;
        case "UBI10": Logro = getApplicationContext().getString(R.string.UBI10);
            break;
        case "PROPC": Logro = getApplicationContext().getString(R.string.PROPC);
            break;
        case "PROPD": Logro = getApplicationContext().getString(R.string.PROPD);
            break;
        case "PROPO": Logro = getApplicationContext().getString(R.string.PROPO);
            break;
        case "PROPM": Logro = getApplicationContext().getString(R.string.PROPM);
            break;
        case "PROPE": Logro = getApplicationContext().getString(R.string.PROPE);
            break;
        case "PROPT": Logro = getApplicationContext().getString(R.string.PROPT);
            break;
        case "PROPQ": Logro = getApplicationContext().getString(R.string.PROPQ);
            break;
        case "PROPS": Logro = getApplicationContext().getString(R.string.PROPS);
            break;
        case "TWIT1": Logro = getApplicationContext().getString(R.string.TWIT1);
            break;
        case "TWIT100": Logro = getApplicationContext().getString(R.string.TWIT100);
            break;
        case "GLIKE1": Logro = getApplicationContext().getString(R.string.GLIKE1);
            break;
        case "GLIKE10": Logro = getApplicationContext().getString(R.string.GLIKE10);
            break;
        case "GLIKE100": Logro = getApplicationContext().getString(R.string.GLIKE100);
            break;
        case "PLIKE1": Logro = getApplicationContext().getString(R.string.PLIKE1);
            break;
        case "PLIKE10": Logro = getApplicationContext().getString(R.string.PLIKE10);
            break;
        case "PLIKE100": Logro = getApplicationContext().getString(R.string.PLIKE100);
            break;
        case "COM1": Logro = getApplicationContext().getString(R.string.COM1);
            break;
        case "COM5": Logro = getApplicationContext().getString(R.string.COM5);
            break;
        case "COM25": Logro = getApplicationContext().getString(R.string.COM25);
            break;
        case "COM100": Logro = getApplicationContext().getString(R.string.COM100);
            break;
        case "GCOM1": Logro = getApplicationContext().getString(R.string.GCOM1);
            break;
        case "GCOM10": Logro = getApplicationContext().getString(R.string.GCOM10);
            break;
        case "GCOM100": Logro = getApplicationContext().getString(R.string.GCOM100);
            break;
        default: Logro = "Something went wrong";
            break;
    }
    //   Logros[i]=Logro;
    //  }
    return Logro;
}
}

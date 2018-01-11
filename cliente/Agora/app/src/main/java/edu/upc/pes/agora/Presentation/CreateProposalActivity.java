package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import edu.upc.pes.agora.Logic.Listeners.BackOnClickListener;
import edu.upc.pes.agora.Logic.Models.ImatgeItem;
import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.Logic.Listeners.LanguageOnClickListener;
import edu.upc.pes.agora.Logic.ServerConection.PostAsyncTask;
import edu.upc.pes.agora.R;

public class CreateProposalActivity extends AppCompatActivity {

    private Button Create;
    private TextView Titulo, Descripcion, txtPosAttached, deletePosition;
    private TextInputLayout errortitulo, errordescripcion;
    private ProgressBar prog;
    private Spinner spin;
    private ListView limatges;
    private FloatingActionButton addImage, addLocation, menuFloating;
    private LinearLayout linearImage, linearLocation;


    private String strTitulo = "";
    private String strDescripcion = "";
    private String strCategoria = "";
    private String encoded;
    private String[] categoriasGenericas = {"X", "C", "D", "O","M", "E", "T","Q", "S", "A"};
    private double lat;
    private double lng;

    private JSONArray ArrayImages = new JSONArray();
    private ArrayList<ImatgeItem> mImatgeItems = new ArrayList<> ();

    private final int SELECT_PICTURE=200;
    private int idProposta;
    private int numimatges = 0;

    private LayoutInflater mInflator;
    private boolean selected;
    private boolean crearDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_proposal);

        Button reset = (Button) findViewById(R.id.resetButton);
        Create = (Button) findViewById(R.id.createButton);

        Titulo = (TextView) findViewById(R.id.titulo);
        Descripcion = (TextView) findViewById(R.id.descripcion);
        Descripcion.setFilters(new InputFilter[]{new InputFilter.LengthFilter(140)});
        txtPosAttached = (TextView) findViewById(R.id.positionatached);
        deletePosition = (TextView) findViewById(R.id.deleteposition);
        //showPosition = (TextView) findViewById(R.id.seeposition);

        errortitulo = (TextInputLayout) findViewById(R.id.titulo_up);
        errordescripcion = (TextInputLayout) findViewById(R.id.descripcion_up);

        prog = (ProgressBar) findViewById(R.id.crproposalprogressbar);

        spin = (Spinner) findViewById(R.id.cate);

        limatges = (ListView) findViewById(R.id.llistaimatges);

        addImage = (FloatingActionButton) findViewById(R.id.fabimages);
        addLocation = (FloatingActionButton) findViewById(R.id.fablocalization);
        menuFloating = (FloatingActionButton) findViewById(R.id.faboptions);

        linearLocation = (LinearLayout) findViewById(R.id.localizationLayout);
        linearImage = (LinearLayout) findViewById(R.id.imagesLayout);

        ImageView canviidioma = (ImageView) findViewById(R.id.multiidiomareg);
        ImageView enrerre = (ImageView) findViewById(R.id.backbutton);

        crearDialog = false;

        final Resources res = this.getResources();

        Titulo.getBackground().clearColorFilter();
        Descripcion.getBackground().clearColorFilter();

        final Animation showButton = AnimationUtils.loadAnimation(CreateProposalActivity.this, R.anim.show_button);
        final Animation hideButton = AnimationUtils.loadAnimation(CreateProposalActivity.this, R.anim.hide_button);
        final Animation showLayout = AnimationUtils.loadAnimation(CreateProposalActivity.this, R.anim.show_layout);
        final Animation hideLayout = AnimationUtils.loadAnimation(CreateProposalActivity.this, R.anim.hide_layout);

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
        spin.setAdapter(spinnerArrayAdapter);

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

        Intent idioma = new Intent(CreateProposalActivity.this, CreateProposalActivity.class);
        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        canviidioma.setOnClickListener(new LanguageOnClickListener(idioma, canviidioma, res, getApplicationContext()));

        Intent back = new Intent(CreateProposalActivity.this, MainActivity.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        enrerre.setOnClickListener(new BackOnClickListener(back, getApplicationContext()));

        Log.i("asdProva", "psas por aqui");
        if (getIntent().hasExtra("Category")){
            spin.setSelection(getIntent().getIntExtra("Category",0));
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
            txtPosAttached.setVisibility(View.VISIBLE);
            deletePosition.setVisibility(View.VISIBLE);
            //showPosition.setVisibility(View.VISIBLE);
        }
        else {
            lat = 0;
            lng = 0;
            txtPosAttached.setVisibility(View.INVISIBLE);
            deletePosition.setVisibility(View.INVISIBLE);
            //showPosition.setVisibility(View.INVISIBLE);
        }


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Titulo.setText("");
                Descripcion.setText("");
                errortitulo.setErrorEnabled(false);
                Titulo.getBackground().clearColorFilter();
                errordescripcion.setErrorEnabled(false);
                Descripcion.getBackground().clearColorFilter();
                spin.setSelection(0);
                mImatgeItems.clear();
                limatges.setAdapter(new ImatgesAdapter(getApplicationContext(), mImatgeItems));
                lat = 0;
                lng = 0;
                txtPosAttached.setVisibility(View.INVISIBLE);
                deletePosition.setVisibility(View.INVISIBLE);
                //showPosition.setVisibility(View.INVISIBLE);
            }
        });

        Create.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                strTitulo = Titulo.getText().toString();
                strDescripcion = Descripcion.getText().toString();
                strCategoria = categoriasGenericas[spin.getSelectedItemPosition()];

                if (Titulo.length() == 0){
                    errortitulo.setErrorEnabled(true);
                    errortitulo.setError(res.getString(R.string.fieldnecesary));
                    Titulo.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                    if (Descripcion.length() != 0) {
                        errordescripcion.setErrorEnabled(false);
                        Descripcion.getBackground().clearColorFilter();
                    }
                }

                if (Descripcion.length() == 0){
                    errordescripcion.setErrorEnabled(true);
                    errordescripcion.setError(res.getString(R.string.fieldnecesary));
                    Descripcion.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                    if (Titulo.length() != 0) {
                        errortitulo.setErrorEnabled(false);
                        Titulo.getBackground().clearColorFilter();
                    }
                }

                if (strCategoria.equals("X")) {
                    TextView errorText = (TextView)spin.getSelectedView();
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    if (Titulo.length() != 0) {
                        errortitulo.setErrorEnabled(false);
                        Titulo.getBackground().clearColorFilter();
                    }
                    if (Descripcion.length() != 0) {
                        errordescripcion.setErrorEnabled(false);
                        Descripcion.getBackground().clearColorFilter();
                    }
                }

                else if (Titulo.length() != 0 && Descripcion.length() != 0 && !strCategoria.equals("X")){
                    Create.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);
                    Descripcion.getBackground().clearColorFilter();
                    Titulo.getBackground().clearColorFilter();

                    final JSONObject values = new JSONObject();
                    JSONObject location = new JSONObject();

                    try {

                        values.put("title", strTitulo);
                        values.put("content", strDescripcion);
                        values.put("categoria", strCategoria);
                        location.put("lat", getIntent().getDoubleExtra("lat",0));
                        location.put("long", getIntent().getDoubleExtra("lng",0));
                        values.put("location", location);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.i("asdTORNAR", values.toString());

                    new PostAsyncTask("https://agora-pes.herokuapp.com/api/proposal", CreateProposalActivity.this) {
                        @Override
                        protected void onPostExecute(JSONObject resObject) {
                            Boolean result = false;
                            try {

                                Log.i("resposta", resObject.toString());

                                if (resObject.has("success")) {
                                    result = resObject.getBoolean("success");
                                }

                                if (resObject.has("ArrayResponse")) {
                                    JSONObject ArrayProp = resObject.getJSONObject("ArrayResponse");
                                    Log.i("resposta2", ArrayProp.toString());

                                    idProposta = ArrayProp.getInt("id");

                                    Log.i("respostaid", String.valueOf(idProposta));
                                }

                                if (!result && resObject.has("errorMessage")) {
                                    String error = res.getString(R.string.errorCreacion);
                                    Log.i("asdCreacion", error);
                                    if(resObject.getString("errorMessage").equals("Selected location outside of allowed zone.")){
                                        Toast.makeText(getApplicationContext(), res.getString(R.string.errorPosition), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String creacionok = String.format(res.getString(R.string.done), strTitulo);
                            String achievement = this.getNewAchievement();






                            if (result && achievement != null && !achievement.equals("")) {
                                sendNot(achievement);
                          //      crear(achievement);
                            }
                            if (result) {
                                if (numimatges > 0) {
                                    afegirimatges();
                                } else {
                                    Toast.makeText(getApplicationContext(), creacionok, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(CreateProposalActivity.this, MainActivity.class));
                                }
                            }

                            else {
                                Log.i("asdCreacion", "reset");
                                Create.setVisibility(View.VISIBLE);
                                prog.setVisibility(View.GONE);
                            }
                        }
                    }.execute(values);

                }
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
/*
        if (crearDialog) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateProposalActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_trophy, null);
            TextView textView = (TextView)mView.findViewById(R.id.textView);
            Button mAccept = (Button) mView.findViewById(R.id.etAccept);
            ImageView imageView = (ImageView) mView.findViewById(R.id.image);
            imageView.setImageResource(R.drawable.ic_twitter);
            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            mAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }*/

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLocation.setVisibility(View.GONE);
                linearImage.setVisibility(View.GONE);
                linearLocation.startAnimation(hideLayout);
                linearImage.startAnimation(hideLayout);
                menuFloating.startAnimation(hideButton);
                final CharSequence[] options = {"Galería", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(CreateProposalActivity.this);
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
                i.putExtra("Title", Titulo.getText().toString());
                i.putExtra("Description", Descripcion.getText().toString());
                i.putExtra("Category",spin.getSelectedItemPosition());
                i.putExtra("CallingActivity", "Create");
                if (getIntent().hasExtra("lat") && getIntent().hasExtra("lng") && lat != 0 && lng != 0){
                    Log.i("Putting Position","check");
                    i.putExtra("lat", getIntent().getDoubleExtra("lat",0));
                    i.putExtra("lng", getIntent().getDoubleExtra("lng",0));
                }
                startActivityForResult(i,1);
            }
        });

        deletePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat = 0;
                lng = 0;
                txtPosAttached.setVisibility(View.INVISIBLE);
                deletePosition.setVisibility(View.INVISIBLE);
                //showPosition.setVisibility(View.INVISIBLE);
                lat = 0;
                lng = 0;
            }
        });

        /*showPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ShowLocationActivity.class);
                if (getIntent().hasExtra("lat") && getIntent().hasExtra("lng")) {
                    i.putExtra("lat", getIntent().getDoubleExtra("lat",0));
                    i.putExtra("lng", getIntent().getDoubleExtra("lng",0));
                    startActivity(i);
                }
            }
        });*/
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

        new PostAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + idProposta + "/image", CreateProposalActivity.this) {
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

                String creacionok = String.format(res.getString(R.string.done), strTitulo);

                String achievement = this.getNewAchievement();

                if (result && achievement != null && !achievement.equals("")) {
                    sendNot(achievement);
                  //  crear(achievement);
                }

                if (result) {
                    Toast.makeText(getApplicationContext(), creacionok, Toast.LENGTH_LONG).show();

                    startActivity(new Intent(CreateProposalActivity.this, MainActivity.class));
                }

                else {
                    Create.setVisibility(View.VISIBLE);
                    prog.setVisibility(View.GONE);
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

    public void sendNot(String achievement){

        Intent i=new Intent(CreateProposalActivity.this, LogrosActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(CreateProposalActivity.this, 0, i, 0);

        String[] parts = achievement.split(",");
        int count = parts.length;
        for ( int j = 0; j < count; j++ ){
            String decoded = codificaLogro(parts[j]);
            if(!decoded.equals("Something went wrong")) {
                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.logo);

                NotificationCompat.Builder mBuilder;
                NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.trophy)
                        .setContentTitle(getString(R.string.nuevo))
                        .setLargeIcon(icon)
                        .setContentText(decoded)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(decoded))
                        .setVibrate(new long[]{100, 250, 100, 500})
                        .setAutoCancel(true);

                mNotifyMgr.notify(j + 1, mBuilder.build());
            }
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

package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.upc.pes.agora.Logic.Adapters.ImatgesAdapter;
import edu.upc.pes.agora.Logic.Listeners.BackOnClickListener;
import edu.upc.pes.agora.Logic.Models.Comment;
import edu.upc.pes.agora.Logic.Adapters.CommentAdapter;
import edu.upc.pes.agora.Logic.Models.ImatgeItem;
import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.Listeners.LanguageOnClickListener;
import edu.upc.pes.agora.Logic.ServerConection.PostAsyncTask;
import edu.upc.pes.agora.R;

public class DetailsProposalActivity extends AppCompatActivity {

    private TextView titol;
    private TextView descripcio;
    private TextView owner;
    private TextView categoria;
    private Button showPos;

    private TextView date;


    private ListView llista_comentaris;
    private ListView llista_imatges;
    private String newComent;
    private ImageView canviidioma, enrerre, compartir;

    private String mtit, mdesc, mowner, mcategorias, c;

    private FloatingActionButton addcoment;

    private Integer idprop;

    private JSONObject Jason = new JSONObject();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_proposal);

        titol = (TextView) findViewById(R.id.titolproposal);
        descripcio = (TextView) findViewById(R.id.descripcioproposta);
        categoria = (TextView) findViewById(R.id.categoriaproposta);
        owner = (TextView) findViewById(R.id.ownerproposal);
        date = (TextView) findViewById(R.id.date);

        date.setText(getIntent().getStringExtra("Creation"));

        showPos = (Button) findViewById(R.id.showPositionButton);
        if(getIntent().getDoubleExtra("lat",0) != 0){
            showPos.setVisibility(View.VISIBLE);
        }else{
            showPos.setVisibility(View.INVISIBLE);
        }

        llista_comentaris = (ListView) findViewById(R.id.listcommentaris);

        llista_imatges = (ListView) findViewById(R.id.listimatges);

        addcoment = (FloatingActionButton) findViewById(R.id.fabcoment);

        canviidioma = (ImageView) findViewById(R.id.multiidiomareg);
        enrerre = (ImageView) findViewById(R.id.backbutton);
        compartir = (ImageView) findViewById(R.id.compartir);

        final Resources res = this.getResources();

        Intent i = getIntent();

        if (i.hasExtra("Title")) {
            titol.setText(i.getStringExtra("Title"));
            mtit = i.getStringExtra("Title");
        }
        if (i.hasExtra("Description")) {
            descripcio.setText(i.getStringExtra("Description"));
            mdesc = i.getStringExtra("Description");
        }
        if (i.hasExtra("Owner")) {
            owner.setText(i.getStringExtra("Owner"));
            mowner = i.getStringExtra("Owner");
        }
        if (i.hasExtra("Categoria")) {
            c = i.getStringExtra("Categoria");
        }

        idprop = i.getIntExtra("id", 0);

        switch (c) {
            case "C":
                mcategorias = res.getString(R.string.cultura);
                break;
            case "D":
                mcategorias = res.getString(R.string.deportes);
                break;
            case "O":
                mcategorias = res.getString(R.string.ocio);
                break;
            case "M":
                mcategorias = res.getString(R.string.mantenimiento);
                break;
            case "E":
                mcategorias = res.getString(R.string.eventos);
                break;
            case "T":
                mcategorias = res.getString(R.string.turismo);
                break;
            case "Q":
                mcategorias = res.getString(R.string.quejas);
                break;
            case "S":
                mcategorias = res.getString(R.string.soporte);
                break;
        }

        categoria.setText(mcategorias);

        llistarcomentaris();

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

        final Intent idioma = new Intent(DetailsProposalActivity.this, DetailsProposalActivity.class);
        Intent back = new Intent();
        if (getIntent().hasExtra("otherUser") && getIntent().getBooleanExtra("otherUser", false)) {
            back = new Intent(this, OtherUserProposalsActivity.class);
            back.putExtra("username", getIntent().getStringExtra("Owner"));
        } else {
            back = new Intent(DetailsProposalActivity.this, MainActivity.class);
        }

        idioma.putExtra("Title", mtit);
        idioma.putExtra("Description", mdesc);
        idioma.putExtra("id", idprop);
        idioma.putExtra("Owner", mowner);
        idioma.putExtra("Categoria", c);
        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), OtherUserActivity.class);
                i.putExtra("username", owner.getText());
                startActivity(i);
            }
        });

        canviidioma.setOnClickListener(new LanguageOnClickListener(idioma, canviidioma, res, getApplicationContext()));

        enrerre.setOnClickListener(new BackOnClickListener(back, getApplicationContext()));

        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("asdCompartir", "true");
                String intro = getString(R.string.mensajecompartir);
                String tweetUrl = "https://twitter.com/intent/tweet?text=" + intro + "<br>" + "<br>" + mtit + "<br>"+ mdesc + "&url=";
                tweetUrl = Html.fromHtml(tweetUrl).toString();
                Uri uri = Uri.parse(tweetUrl);
                v.getRootView().getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        addcoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogoaddcoment = new AlertDialog.Builder(v.getRootView().getContext());

                final EditText input = new EditText(v.getRootView().getContext());
                //input.setSingleLine();
                FrameLayout container = new FrameLayout(DetailsProposalActivity.this);
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                input.setLayoutParams(params);
                input.getBackground().clearColorFilter();
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
                container.addView(input);
                dialogoaddcoment.setTitle(getString(R.string.nou));
                String mensajeparaañadir = String.format(res.getString(R.string.mensajenc), mtit);
                dialogoaddcoment.setMessage(mensajeparaañadir);
                dialogoaddcoment.setIcon(R.drawable.logo);
                dialogoaddcoment.setCancelable(false);
                dialogoaddcoment.setView(container);

                dialogoaddcoment.setPositiveButton(getString(R.string.añadir), new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        newComent = input.getText().toString();

                        JSONObject values = new JSONObject();
                        try {
                            values.put("comment", newComent);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new PostAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + idprop + "/comment", DetailsProposalActivity.this) {
                            @Override
                            protected void onPostExecute(JSONObject resObject) {
                                Boolean result = false;
                                String error = res.getString(R.string.errorCreacion);

                                try {
                                    if (resObject.has("success")) {
                                        result = resObject.getBoolean("success");
                                    }

                                    if (!result && resObject.has("errorMessage")) {
                                        Log.i("asdCreacion", error);
                                        //Toast.makeText(getApplicationContext(), error , Toast.LENGTH_LONG).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (result) {
                                    input.getBackground().clearColorFilter();
                                    Intent myIntent = new Intent(getApplicationContext(), DetailsProposalActivity.class);
                                    myIntent.putExtra("Title", mtit);
                                    myIntent.putExtra("Description", mdesc);
                                    myIntent.putExtra("id", idprop);
                                    myIntent.putExtra("Owner", mowner);
                                    myIntent.putExtra("Categoria", c);
                                    startActivity(myIntent);
                                }

                                else {
                                    Log.i("asdCreacion", "reset");
                                    input.setText("");
                                    input.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                                }
                            }
                        }.execute(values);
                    }
                });

                dialogoaddcoment.setNegativeButton(getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                final AlertDialog dialog = dialogoaddcoment.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {
                        if(input.getText().toString().equals("")) ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                });

                dialog.show();

                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (input.getText().toString().equals("")) {
                            // Disable ok button
                            (dialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            // Something into edit text. Enable the button.
                            (dialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                });

            }
        });

        showPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ShowLocationActivity.class);
                if (getIntent().hasExtra("lat") && getIntent().hasExtra("lng")) {
                    i.putExtra("lat", getIntent().getDoubleExtra("lat",0));
                    i.putExtra("lng", getIntent().getDoubleExtra("lng",0));
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent refresh;
        if (getIntent().hasExtra("otherUser") && getIntent().getBooleanExtra("otherUser", false)){
            refresh = new Intent(this, OtherUserProposalsActivity.class);
            refresh.putExtra("username", getIntent().getStringExtra("Owner"));
        }else {
            refresh = new Intent(this, MainActivity.class);
        }
        startActivity(refresh);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @SuppressLint("StaticFieldLeak")
    private void llistarcomentaris() {
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

                        JSONArray ArrayComments = jsonObject.getJSONArray("comments");
                        ArrayList<Comment> comentarios = new ArrayList<>();

                        if (ArrayComments != null) {
                            for (int i=0; i < ArrayComments.length(); i++){

                                Log.i("asd123", (ArrayComments.get(i).toString()));

                                JSONObject jas = ArrayComments.getJSONObject(i);
                                String id = jas.getString("id");
                                String date = jas.getString("createdDateTime");
                                String contentcoment = jas.getString("comment");

                                JSONObject Usuario = jas.getJSONObject("author");
                                Log.i("asd123", (Usuario.toString()));
                                String owner = Usuario.getString("username");


                                Comment aux = new Comment(owner, id, contentcoment);
                                aux.setCreated(date);
                                aux.setIdentificadorProp(idprop);

                                comentarios.add(aux);
                            }
                        }
                        llista_comentaris.setAdapter(new CommentAdapter(getApplicationContext(), comentarios));

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

                                imatges.add(aux);
                            }
                        }
                        llista_imatges.setAdapter(new ImatgesAdapter(getApplicationContext(), imatges));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(Jason);
    }

}
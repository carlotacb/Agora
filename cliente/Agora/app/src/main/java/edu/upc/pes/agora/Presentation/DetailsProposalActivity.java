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
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
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
import android.widget.LinearLayout;
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

    private ImageView moreoptions;
    private ImageView favorite;
    private ImageView loca;
    private LinearLayout Limatges;

    private TextView date;

    private ListView llista_comentaris;
    //private ListView llista_imatges;
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

        moreoptions = (ImageView) findViewById(R.id.more);
        //favorite = (ImageView) findViewById(R.id.jilkhjkf);

        TextView numerocomentarios = (TextView) findViewById(R.id.numerocomentaris);
        ImageView likeuser = (ImageView) findViewById(R.id.likeuser);
        ImageView dislikeuser = (ImageView) findViewById(R.id.dislikeuser);
        final TextView numerolikes = (TextView) findViewById(R.id.numerovote);
        final TextView numerodislikes = (TextView) findViewById(R.id.numerounvote);
        final ImageView likeimagen = (ImageView) findViewById(R.id.like);
        final ImageView dislikeimagen = (ImageView) findViewById(R.id.dislike);

        canviidioma = (ImageView) findViewById(R.id.multiidiomareg);
        enrerre = (ImageView) findViewById(R.id.backbutton);
        compartir = (ImageView) findViewById(R.id.compartir);
        Limatges = (LinearLayout) findViewById(R.id.imagesLayout);
        //loca = (ImageView) findViewById(R.id.);

        if(getIntent().getDoubleExtra("lat",0) != 0){
            Limatges.setVisibility(View.VISIBLE);
        }else{
            Limatges.setVisibility(View.INVISIBLE);
        }

        //llista_imatges = (ListView) findViewById(R.id.listimatges);

        llista_comentaris = (ListView) findViewById(R.id.listcommentaris);

        addcoment = (FloatingActionButton) findViewById(R.id.fabcoment);

        final Resources res = this.getResources();

        Intent i = getIntent();
        date.setText(getIntent().getStringExtra("Creation"));

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

        if (Constants.Username.equals(mowner)) {
            likeuser.setVisibility(View.VISIBLE);
            dislikeuser.setVisibility(View.VISIBLE);
            likeimagen.setVisibility(View.GONE);
            dislikeimagen.setVisibility(View.GONE);
            //favorite.setVisibility(View.GONE);
            moreoptions.setVisibility(View.VISIBLE);

            owner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsProposalActivity.this, MyProfileActivity.class);
                    v.getContext().startActivity(i);
                }
            });
        }
        else {
            likeuser.setVisibility(View.GONE);
            dislikeuser.setVisibility(View.GONE);
            likeimagen.setVisibility(View.VISIBLE);
            dislikeimagen.setVisibility(View.VISIBLE);
            //favorite.setVisibility(View.VISIBLE);
            moreoptions.setVisibility(View.GONE);

            owner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailsProposalActivity.this, OtherUserActivity.class);
                    i.putExtra("username", mowner);
                    v.getContext().startActivity(i);
                }
            });
        }

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

                JSONObject values = new JSONObject();
                try {
                    values.put("proposalId",idprop);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new PostAsyncTask("https://agora-pes.herokuapp.com/api/webhook/shared/twitter",getApplicationContext()){
                    @Override
                    protected void onPostExecute(JSONObject resObject) {
                        Boolean result = false;
                        try {

                            Log.i("resposta", resObject.toString());

                            if (resObject.has("success")) {
                                result = resObject.getBoolean("success");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String achievement = this.getNewAchievement();


                        if (result && achievement != null && !achievement.equals("")) {
                            sendNot(achievement);
                        }

                    }


                }.execute(values);

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
                input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                //input.setLines(3);
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

                                String achievement = this.getNewAchievement();

                                if (result && achievement != null && !achievement.equals("")) {
                                    sendNot(achievement);
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

        /*showPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ShowLocationActivity.class);
                if (getIntent().hasExtra("lat") && getIntent().hasExtra("lng")) {
                    i.putExtra("lat", getIntent().getDoubleExtra("lat",0));
                    i.putExtra("lng", getIntent().getDoubleExtra("lng",0));
                    startActivity(i);
                }
            }
        });*/
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
                        //llista_imatges.setAdapter(new ImatgesAdapter(getApplicationContext(), imatges));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(Jason);
    }


    public void sendNot(String achievement){

        Intent i=new Intent(DetailsProposalActivity.this, LogrosActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(DetailsProposalActivity.this, 0, i, 0);

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


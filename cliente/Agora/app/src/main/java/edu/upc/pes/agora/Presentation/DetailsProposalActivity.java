package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.upc.pes.agora.Logic.Adapters.ImageAdapter;
import edu.upc.pes.agora.Logic.Adapters.ImatgesAdapter;
import edu.upc.pes.agora.Logic.Adapters.RecyclerAdapter;
import edu.upc.pes.agora.Logic.Listeners.BackOnClickListener;
import edu.upc.pes.agora.Logic.Models.Comment;
import edu.upc.pes.agora.Logic.Adapters.CommentAdapter;
import edu.upc.pes.agora.Logic.Models.ImatgeItem;
import edu.upc.pes.agora.Logic.Models.Imatgev2;
import edu.upc.pes.agora.Logic.Models.Proposal;
import edu.upc.pes.agora.Logic.ServerConection.DeleteAsyncTask;
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
    private LinearLayout loca;
    private LinearLayout Limatges;
    private LinearLayout carregant;

    private TextView date;

    private ListView llista_comentaris;
    private ListView llista_imatges;
    private String newComent;
    private ImageView canviidioma, enrerre, compartir;

    private String mtit, mdesc, mowner, mcategorias, c, mcreated;

    private FloatingActionButton addcoment;

    private Integer idprop;
    private Proposal proposal;

    private JSONObject Jason = new JSONObject();
    private ArrayList<Imatgev2> imatges = new ArrayList<>();

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
        favorite = (ImageView) findViewById(R.id.prefee);

        final TextView numerocomentarios = (TextView) findViewById(R.id.numerocomentaris);
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
        loca = (LinearLayout) findViewById(R.id.locali);

        if(getIntent().getDoubleExtra("lat",0) != 0){
            loca.setVisibility(View.VISIBLE);
        }else{
            loca.setVisibility(View.INVISIBLE);
        }

        //llista_imatges = (ListView) findViewById(R.id.listimatges);

        llista_comentaris = (ListView) findViewById(R.id.listcommentaris);
        carregant = (LinearLayout) findViewById(R.id.pantallacargando);

        addcoment = (FloatingActionButton) findViewById(R.id.fabcoment);

        final Resources res = this.getResources();

        Intent i = getIntent();

        idprop = i.getIntExtra("id", 0);

        if (i.hasExtra("Creation")) {
            date.setText(i.getStringExtra("Creation"));
            mcreated = i.getStringExtra("Creation");
        }
        if (i.hasExtra("Title")) {
            titol.setText(i.getStringExtra("Title"));
            mtit = i.getStringExtra("Title");
        }
        if (i.hasExtra("Description")) {
            descripcio.setText(i.getStringExtra("Description"));
            mdesc = i.getStringExtra("Description");
        }
        if (i.hasExtra("Owner")) {
            owner.setText(i.getStringExtra("Owner").toUpperCase());
            mowner = i.getStringExtra("Owner");
        }
        if (i.hasExtra("Categoria")) {
            c = i.getStringExtra("Categoria");

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
        }

        proposal = new Proposal(idprop, mtit, mdesc, mowner, mcategorias, mcreated);


        if (i.hasExtra("votacion")) {
            proposal.setVotacion(i.getIntExtra("votacion", 0));
        }
        if (i.hasExtra("ncomentarios")) {
            proposal.setNumerocomentarios(i.getIntExtra("ncomentarios",0));
            numerocomentarios.setText(String.valueOf(proposal.getNumerocomentarios()));
        }
        if (i.hasExtra("nvotes")) {
            proposal.setNumerovotes(i.getIntExtra("nvotes",0));
            numerolikes.setText(String.valueOf(proposal.getNumerovotes()));
        }
        if (i.hasExtra("nunvotes")) {
            proposal.setNumerounvotes(i.getIntExtra("nunvotes", 0));
            numerodislikes.setText(String.valueOf(proposal.getNumerounvotes()));
        }
        if (i.hasExtra("favorit")) {
            proposal.setFavorite(i.getBooleanExtra("favorit", false));
        }
        if (i.hasExtra("lat")) {
            proposal.setLat(i.getDoubleExtra("lat", 0.0));
        }
        if (i.hasExtra("lng")) {
            proposal.setLng(i.getDoubleExtra("lng", 0.0));
        }


        if (Constants.Username.equals(mowner.toLowerCase())) {
            likeuser.setVisibility(View.VISIBLE);
            dislikeuser.setVisibility(View.VISIBLE);
            likeimagen.setVisibility(View.GONE);
            dislikeimagen.setVisibility(View.GONE);
            favorite.setVisibility(View.GONE);
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
            favorite.setVisibility(View.VISIBLE);
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

        llistarcomentaris();

        if (proposal.getFavorite()){
            favorite.setImageResource(R.drawable.ic_favorite_red);
        } else {
            favorite.setImageResource(R.drawable.ic_favorite);
        }

        if (proposal.getVotacion() == -1) {
            likeimagen.setImageResource(R.drawable.ic_like_24);
            dislikeimagen.setImageResource(R.drawable.ic_dislike_blue_24);
        }
        else if (proposal.getVotacion() == 0) {
            likeimagen.setImageResource(R.drawable.ic_like_24);
            dislikeimagen.setImageResource(R.drawable.ic_dislike_24);
        }
        else if (proposal.getVotacion() == 1) {
            dislikeimagen.setImageResource(R.drawable.ic_dislike_24);
            likeimagen.setImageResource(R.drawable.ic_like_blue_24);
        }

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
        Intent back;
        if (getIntent().hasExtra("otherUser")) {
            back = new Intent(DetailsProposalActivity.this, OtherUserProposalsActivity.class);
            back.putExtra("username", getIntent().getStringExtra("Owner"));
        }

        else if (getIntent().hasExtra("deFavorites")){
            back = new Intent(DetailsProposalActivity.this, MyFavoritesActivity.class);
        }

        else if (getIntent().hasExtra("deMyProposals")){
            back = new Intent(DetailsProposalActivity.this, MyProposalsActivity.class);
        }

        else {
            back = new Intent(DetailsProposalActivity.this, MainActivity.class);
        }

        idioma.putExtra("Title", mtit);
        idioma.putExtra("Description", mdesc);
        idioma.putExtra("id", idprop);
        idioma.putExtra("lat", proposal.getLat());
        idioma.putExtra("lng", proposal.getLng());
        idioma.putExtra("Owner", mowner);
        idioma.putExtra("Categoria", c);
        idioma.putExtra("Creation", proposal.getCreation());
        idioma.putExtra("ncomentarios", proposal.getNumerocomentarios());
        idioma.putExtra("nvotes", proposal.getNumerovotes());
        idioma.putExtra("nunvotes", proposal.getNumerounvotes());
        idioma.putExtra("favorit", proposal.getFavorite());
        idioma.putExtra("votacion", proposal.getVotacion());

        if (getIntent().hasExtra("otherUser")) idioma.putExtra("otherUser", "ve d'altre usuari");
        if (getIntent().hasExtra("deFavorites")) idioma.putExtra("deFavorites", "ve d'altre usuari");
        if (getIntent().hasExtra("deMyProposals")) idioma.putExtra("deMyProposals", "ve d'altre usuari");

        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

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
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(70)});
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
                                    Integer ncom = proposal.getNumerocomentarios();
                                    proposal.setNumerocomentarios(ncom+1);
                                    numerocomentarios.setText(String.valueOf(ncom+1));
                                    llistarcomentaris();


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

        if (proposal.getLat() == 0.0 && proposal.getLng() == 0.0) {
            loca.setVisibility(View.GONE);
        }
        else {
            loca.setVisibility(View.VISIBLE);
        }

        loca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sl = new Intent(getApplicationContext(), ShowLocationActivity.class);
                if (getIntent().hasExtra("lat") && getIntent().hasExtra("lng")) {
                    sl.putExtra("lat", getIntent().getDoubleExtra("lat",0));
                    sl.putExtra("lng", getIntent().getDoubleExtra("lng",0));
                    startActivity(sl);
                }
            }
        });

        likeimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject values = new JSONObject();
                Integer voting = proposal.getVotacion();

                if (proposal.getVotacion() == 0 || proposal.getVotacion() == -1) {
                    voting = 1;
                } else if (proposal.getVotacion() == 1){
                    voting = 0;
                }

                try {
                    values.put("vote", voting);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Boolean voted = votar_favorite(values, "https://agora-pes.herokuapp.com/api/proposal/" + proposal.getId() + "/vote");

                Integer numvote = proposal.getNumerovotes();
                Integer numunvote = proposal.getNumerounvotes();
                Log.i("asd123", String.valueOf(proposal.getNumerovotes()));

                if (voted) {
                    if (proposal.getVotacion() == 0) {
                        likeimagen.setImageResource(R.drawable.ic_like_blue_24);
                        proposal.setVotacion(1);
                        proposal.setNumerovotes(numvote+1);
                        numerolikes.setText(String.valueOf(numvote+1));

                    }
                    else if (proposal.getVotacion() == 1) {
                        likeimagen.setImageResource(R.drawable.ic_like_24);
                        proposal.setVotacion(0);
                        proposal.setNumerovotes(numvote-1);
                        numerolikes.setText(String.valueOf(numvote-1));
                    }
                    else if (proposal.getVotacion() == -1){
                        likeimagen.setImageResource(R.drawable.ic_like_blue_24);
                        dislikeimagen.setImageResource(R.drawable.ic_dislike_24);
                        proposal.setVotacion(1);
                        proposal.setNumerovotes(numvote+1);
                        proposal.setNumerounvotes(numunvote-1);
                        numerodislikes.setText(String.valueOf(numunvote-1));
                        numerolikes.setText(String.valueOf(numvote+1));
                    }
                }

            }

        });

        dislikeimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject values = new JSONObject();
                Integer voting = proposal.getVotacion();

                if (proposal.getVotacion() == 0 || proposal.getVotacion() == 1) {
                    //dislikeimagen.setImageResource(R.drawable.ic_dislike_blue_24);
                    voting = -1;
                } else if (proposal.getVotacion() == -1){
                    //dislikeimagen.setImageResource(R.drawable.ic_dislike_24);
                    voting = 0;
                }

                try {
                    values.put("vote", voting);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Boolean unvoted = votar_favorite(values, "https://agora-pes.herokuapp.com/api/proposal/" + proposal.getId() + "/vote");

                Integer numvote = proposal.getNumerovotes();
                Integer numunvote = proposal.getNumerounvotes();

                if (unvoted) {
                    if (proposal.getVotacion() == 0) {
                        dislikeimagen.setImageResource(R.drawable.ic_dislike_blue_24);
                        proposal.setVotacion(-1);
                        proposal.setNumerounvotes(numunvote+1);
                        numerodislikes.setText(String.valueOf(numunvote+1));

                    }
                    else if (proposal.getVotacion() == 1) {
                        dislikeimagen.setImageResource(R.drawable.ic_dislike_blue_24);
                        likeimagen.setImageResource(R.drawable.ic_like_24);
                        proposal.setVotacion(-1);
                        proposal.setNumerounvotes(numunvote+1);
                        proposal.setNumerovotes(numvote-1);
                        numerolikes.setText(String.valueOf(numvote-1));
                        numerodislikes.setText(String.valueOf(numunvote+1));
                    }
                    else if (proposal.getVotacion() == -1){
                        dislikeimagen.setImageResource(R.drawable.ic_dislike_24);
                        proposal.setVotacion(0);
                        proposal.setNumerounvotes(numunvote-1);
                        numerodislikes.setText(String.valueOf(numunvote-1));
                    }

                }
            }
        });


        favorite.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                JSONObject values = new JSONObject();
                Boolean propfav = !proposal.getFavorite();

                try {
                    values.put("favorited", propfav);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Boolean favorited = votar_favorite(values, "https://agora-pes.herokuapp.com/api/proposal/" + proposal.getId() + "/favorite");

                if (favorited) {
                    if (proposal.getFavorite()) {
                        favorite.setImageResource(R.drawable.ic_favorite);
                        proposal.setFavorite(false);
                    }
                    else {
                        favorite.setImageResource(R.drawable.ic_favorite_red);
                        proposal.setFavorite(true);
                    }
                }
            }
        });

        if (imatges.size() == 0) {
            Limatges.setVisibility(View.GONE);
        }
        else {
            Limatges.setVisibility(View.VISIBLE);
        }

        final ProgressBar Limatgespro = (ProgressBar) findViewById(R.id.proli);

        Limatges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Limatges.setVisibility(View.GONE);
                Limatgespro.setVisibility(View.VISIBLE);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailsProposalActivity.this);

                View mView = getLayoutInflater().inflate(R.layout.dialog_images, null);

                Button mAccept = (Button) mView.findViewById(R.id.aceptar);
                GridView gridview = (GridView) mView.findViewById(R.id.gv);
                ImageAdapter gridAdapter = new ImageAdapter(DetailsProposalActivity.this, R.layout.grid_item, imatges);
                gridview.setAdapter(gridAdapter);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                mAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Limatges.setVisibility(View.VISIBLE);
                        Limatgespro.setVisibility(View.GONE);
                    }
                });
            }
        });

        moreoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(v.getRootView().getContext(), moreoptions);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.item_editar:
                                Intent myIntent = new Intent(DetailsProposalActivity.this, EditProposalActivity.class);
                                myIntent.putExtra("Title", proposal.getTitle());
                                myIntent.putExtra("Description", proposal.getDescription());
                                myIntent.putExtra("id", proposal.getId());
                                myIntent.putExtra("Categoria", c);
                                Log.i("asd123", proposal.getCategoria());
                                myIntent.putExtra("lat", proposal.getLat());
                                myIntent.putExtra("lng", proposal.getLng());
                                myIntent.putExtra("ChangeActivity", "Detalls");
                                myIntent.putExtra("Owner", proposal.getOwner());
                                myIntent.putExtra("Creation", proposal.getCreation());
                                myIntent.putExtra("Update", proposal.getUpdate());
                                myIntent.putExtra("ncomentarios", proposal.getNumerocomentarios());
                                myIntent.putExtra("nvotes", proposal.getNumerovotes());
                                myIntent.putExtra("nunvotes", proposal.getNumerounvotes());
                                myIntent.putExtra("favorit", proposal.getFavorite());
                                myIntent.putExtra("votacion", proposal.getVotacion());

                                if (getIntent().hasExtra("otherUser")) myIntent.putExtra("otherUser", "ve d'altre usuari");
                                if (getIntent().hasExtra("deFavorites")) myIntent.putExtra("deFavorites", "ve d'altre usuari");
                                if (getIntent().hasExtra("deMyProposals")) myIntent.putExtra("deMyProposals", "ve d'altre usuari");

                                v.getRootView().getContext().startActivity(myIntent);
                                break;

                            case R.id.item_delete:
                                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(v.getRootView().getContext());
                                dialogo1.setTitle(res.getString(R.string.importante));
                                dialogo1.setMessage(res.getString(R.string.seguro));
                                dialogo1.setCancelable(false);
                                dialogo1.setIcon(R.drawable.logo);
                                dialogo1.setCancelable(false);
                                dialogo1.setPositiveButton(res.getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
                                    @SuppressLint("StaticFieldLeak")
                                    public void onClick(DialogInterface dialogo1, int id) {

                                        new DeleteAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + proposal.getId(), v.getRootView().getContext()){
                                            @Override
                                            protected void onPostExecute(JSONObject jsonObject) {
                                                if (!jsonObject.has("error")) {

                                                    Intent dele;
                                                    if (getIntent().hasExtra("otherUser")) {
                                                        dele = new Intent(DetailsProposalActivity.this, OtherUserProposalsActivity.class);
                                                        dele.putExtra("username", getIntent().getStringExtra("Owner"));
                                                    } else if (getIntent().hasExtra("deFavorites")){
                                                        dele = new Intent(DetailsProposalActivity.this, MyFavoritesActivity.class);
                                                    } else {
                                                        dele = new Intent(DetailsProposalActivity.this, MainActivity.class);
                                                    }

                                                    startActivity(dele);

                                                }
                                                else {
                                                    try {
                                                        Toast.makeText(DetailsProposalActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                super.onPostExecute(jsonObject);
                                            }
                                        }.execute();

                                        //String borratok = String.format(res.getString(R.string.Borrado), tit);
                                        //Toast.makeText(context, borratok, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialogo1.setNegativeButton(res.getString(R.string.Cancelar), new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialogo1, int id) {

                                    }
                                }).show();

                                break;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.popupmenu);
                popupMenu.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent refresh;
        if (getIntent().hasExtra("otherUser") && getIntent().getBooleanExtra("otherUser", false)){
            refresh = new Intent(this, OtherUserProposalsActivity.class);
            refresh.putExtra("username", getIntent().getStringExtra("Owner"));
        } else {
            refresh = new Intent(this, MainActivity.class);
        }
        startActivity(refresh);
    }

    @SuppressLint("StaticFieldLeak")
    private void llistarcomentaris() {

        llista_comentaris.setVisibility(View.GONE);
        carregant.setVisibility(View.VISIBLE);

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

                        if (ArrayImages != null) {
                            Limatges.setVisibility(View.VISIBLE);

                            for (int i=0; i < ArrayImages.length(); i++){
                                Log.i("asd123", (ArrayImages.get(i).toString()));

                                JSONObject jas = ArrayImages.getJSONObject(i);
                                String id = jas.getString("id");
                                String contentimage = jas.getString("image");

                                Imatgev2 aux = new Imatgev2(contentimage);

                                imatges.add(aux);
                            }
                        }
                        else {
                            Limatges.setVisibility(View.GONE);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                llista_comentaris.setVisibility(View.VISIBLE);
                carregant.setVisibility(View.GONE);
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


    private String codificaLogro(String codigoLogro) {

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

    @SuppressLint("StaticFieldLeak")
    private Boolean votar_favorite (JSONObject values, String url) {

        final Boolean[] sortida = new Boolean[1];
        sortida[0] = true;

        new PostAsyncTask(url, DetailsProposalActivity.this) {
            @Override
            protected void onPostExecute(JSONObject resObject) {
                Boolean result = false;

                try {
                    if (resObject.has("success")) {
                        result = resObject.getBoolean("success");
                    }

                    if (!result && resObject.has("errorMessage")) {
                        Log.i("asdCreacion", "Error");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (result) {
                    Log.i("asdCreacion", "OKEY");
                    sortida[0] = true;
                }

                else {
                    Log.i("asdCreacion", "reset");
                    sortida[0] = false;
                }
            }
        }.execute(values);

        return sortida[0];

    }
}


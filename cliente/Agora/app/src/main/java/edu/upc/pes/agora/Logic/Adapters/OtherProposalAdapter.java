package edu.upc.pes.agora.Logic.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.upc.pes.agora.Logic.Models.Proposal;
import edu.upc.pes.agora.Logic.ServerConection.PostAsyncTask;
import edu.upc.pes.agora.Presentation.DetailsProposalActivity;
import edu.upc.pes.agora.R;

public class OtherProposalAdapter extends BaseAdapter {

    private List<Proposal> listProposals;
    private Context context;

    public OtherProposalAdapter(List<Proposal> listProposals, Context context) {
        this.listProposals = listProposals;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listProposals.size();
    }

    @Override
    public Object getItem(int position) {
        return listProposals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = View.inflate(context, R.layout.card_othersproposals_item, null);
        }

        TextView titol = (TextView) convertView.findViewById(R.id.titolcard);
        TextView descripcio = (TextView) convertView.findViewById(R.id.descripciocard);
        TextView moreinfo = (TextView) convertView.findViewById(R.id.btnLernMore);
        TextView categoria = (TextView) convertView.findViewById(R.id.categoriaproposal);
        TextView data = (TextView) convertView.findViewById(R.id.data);
        TextView numerocomentarios = (TextView) convertView.findViewById(R.id.numerocomentaris);
        final TextView numerolikes = (TextView) convertView.findViewById(R.id.numerovote);
        final TextView numerodislikes = (TextView) convertView.findViewById(R.id.numerounvote);
        final ImageView likeimagen = (ImageView) convertView.findViewById(R.id.like);
        final ImageView dislikeimagen = (ImageView) convertView.findViewById(R.id.dislike);
        final ImageView favorite = (ImageView) convertView.findViewById(R.id.fav);

        final Proposal proposal = listProposals.get(position);

        Boolean favo = proposal.getFavorite();
        Integer votacion = proposal.getVotacion();

        if (favo){
            favorite.setImageResource(R.drawable.ic_favorite_red);
        } else {
            favorite.setImageResource(R.drawable.ic_favorite);
        }

        if (votacion == -1) {
            likeimagen.setImageResource(R.drawable.ic_like_24);
            dislikeimagen.setImageResource(R.drawable.ic_dislike_blue_24);
        }
        else if (votacion == 0) {
            likeimagen.setImageResource(R.drawable.ic_like_24);
            dislikeimagen.setImageResource(R.drawable.ic_dislike_24);
        }
        else if (votacion == 1) {
            dislikeimagen.setImageResource(R.drawable.ic_dislike_24);
            likeimagen.setImageResource(R.drawable.ic_like_blue_24);
        }

        final Resources res = context.getResources();

        titol.setText(proposal.getTitle());
        descripcio.setText(proposal.getDescription());
        data.setText(String.format(res.getString(R.string.creadoel), proposal.getCreation()));
        numerocomentarios.setText(String.valueOf(proposal.getNumerocomentarios()));
        numerolikes.setText(String.valueOf(proposal.getNumerovotes()));
        numerodislikes.setText(String.valueOf(proposal.getNumerounvotes()));
        String c = proposal.getCategoria();

        if (c.equals("C")) c = context.getString(R.string.cultura);
        else if (c.equals("D")) c = context.getString(R.string.deportes);
        else if (c.equals("O")) c = context.getString(R.string.ocio);
        else if (c.equals("M")) c = context.getString(R.string.mantenimiento);
        else if (c.equals("E")) c = context.getString(R.string.eventos);
        else if (c.equals("T")) c = context.getString(R.string.turismo);
        else if (c.equals("Q")) c = context.getString(R.string.quejas);
        else if (c.equals("S")) c = context.getString(R.string.soporte);

        categoria.setText(c);

        moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(v.getContext(), DetailsProposalActivity.class);
                myIntent.putExtra("Title", proposal.getTitle());
                myIntent.putExtra("Description", proposal.getDescription());
                myIntent.putExtra("id", proposal.getId());
                myIntent.putExtra("Owner", proposal.getOwner());
                myIntent.putExtra("Categoria", proposal.getCategoria());
                myIntent.putExtra("lat", proposal.getLat());
                myIntent.putExtra("lng", proposal.getLng());
                myIntent.putExtra("Creation", proposal.getCreation());
                myIntent.putExtra("Update", proposal.getUpdate());
                myIntent.putExtra("ncomentarios", proposal.getNumerocomentarios());
                myIntent.putExtra("nvotes", proposal.getNumerovotes());
                myIntent.putExtra("nunvotes", proposal.getNumerounvotes());
                myIntent.putExtra("favorit", proposal.getFavorite());
                myIntent.putExtra("votacion", proposal.getVotacion());
                myIntent.putExtra("otherUser", "ve desde favorites");

                v.getContext().startActivity(myIntent);

                Log.i("asd", "clica");
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

        return convertView;
    }

    @SuppressLint("StaticFieldLeak")
    private Boolean votar_favorite (JSONObject values, String url) {

        final Boolean[] sortida = new Boolean[1];
        sortida[0] = true;

        new PostAsyncTask(url, context) {
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
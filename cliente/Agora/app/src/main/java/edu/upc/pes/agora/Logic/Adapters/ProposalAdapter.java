package edu.upc.pes.agora.Logic.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import edu.upc.pes.agora.Presentation.CreateProposalActivity;
import edu.upc.pes.agora.Presentation.DetailsProposalActivity;
import edu.upc.pes.agora.Presentation.MainActivity;
import edu.upc.pes.agora.R;


public class ProposalAdapter extends BaseAdapter {

    private List<Proposal> listProposals;
    private Context context;
    private Boolean votado = false;
    private Boolean unvote = false;
    private Boolean favo;

    public ProposalAdapter(List<Proposal> listProposals, Context context) {
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
            convertView = View.inflate(context, R.layout.card_proposals_item, null);
        }

        TextView titol = (TextView) convertView.findViewById(R.id.titolcard);
        TextView descripcio = (TextView) convertView.findViewById(R.id.descripciocard);
        TextView owner = (TextView) convertView.findViewById(R.id.owner);
        TextView moreinfo = (TextView) convertView.findViewById(R.id.btnLernMore);
        TextView categoria = (TextView) convertView.findViewById(R.id.categoriaproposal);
        TextView numerocomentarios = (TextView) convertView.findViewById(R.id.numerocomentaris);
        final ImageView likeimagen = (ImageView) convertView.findViewById(R.id.like);
        final ImageView dislikeimagen = (ImageView) convertView.findViewById(R.id.dislike);
        final ImageView favorite = (ImageView) convertView.findViewById(R.id.fav);

        final Proposal proposal = listProposals.get(position);

        favo = proposal.getFavorite();

        if (favo){
            favorite.setImageResource(R.drawable.ic_favorite_red);
        } else {
            favorite.setImageResource(R.drawable.ic_favorite);
        }

        titol.setText(proposal.getTitle());
        descripcio.setText(proposal.getDescription());
        owner.setText(proposal.getOwner());
        numerocomentarios.setText(String.valueOf(proposal.getNumerocomentarios()));
        String c = proposal.getCategoria();

        switch (c) {
            case "C":
                c = context.getString(R.string.cultura);
                break;
            case "D":
                c = context.getString(R.string.deportes);
                break;
            case "O":
                c = context.getString(R.string.ocio);
                break;
            case "M":
                c = context.getString(R.string.mantenimiento);
                break;
            case "E":
                c = context.getString(R.string.eventos);
                break;
            case "T":
                c = context.getString(R.string.turismo);
                break;
            case "Q":
                c = context.getString(R.string.quejas);
                break;
            case "S":
                c = context.getString(R.string.soporte);
                break;
        }

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
                myIntent.putExtra("Creation", proposal.getCreation());
                myIntent.putExtra("Update", proposal.getUpdate());
                v.getContext().startActivity(myIntent);

                Log.i("asd", "clica");
            }
        });

        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Anirem al usuari " + proposal.getOwner(), Toast.LENGTH_SHORT).show();
            }
        });

        likeimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(votado) {
                    likeimagen.setImageResource(R.drawable.ic_like_24);
                    votado = false;
                }
                else {
                    likeimagen.setImageResource(R.drawable.ic_like_blue_24);
                    votado = true;
                }
                Log.i("asd", "clica");

            }

        });

        dislikeimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unvote){
                    dislikeimagen.setImageResource(R.drawable.ic_dislike_24);
                    unvote = false;
                } else {
                    dislikeimagen.setImageResource(R.drawable.ic_dislike_blue_24);
                    unvote = true;
                }
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                JSONObject values = new JSONObject();

                if (proposal.getFavorite()){
                    favorite.setImageResource(R.drawable.ic_favorite);
                    try {
                        values.put("favorited", false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    favorite.setImageResource(R.drawable.ic_favorite_red);
                    try {
                        values.put("favorited", true);
                        //favorite.setImageResource(R.drawable.ic_favorite_red);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                new PostAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + proposal.getId() + "/favorite", context) {
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
                            /*if (favo) {
                                favorite.setImageResource(R.drawable.ic_favorite);
                            } else {
                                favorite.setImageResource(R.drawable.ic_favorite_red);
                            }*/
                            Log.i("asdCreacion", "OKEY");
                        }

                        else {
                            Log.i("asdCreacion", "reset");
                        }
                    }
                }.execute(values);
            }
        });

        return convertView;
    }
}

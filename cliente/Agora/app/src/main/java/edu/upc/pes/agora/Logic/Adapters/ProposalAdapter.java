package edu.upc.pes.agora.Logic.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.upc.pes.agora.Logic.Models.Proposal;
import edu.upc.pes.agora.Presentation.DetailsProposalActivity;
import edu.upc.pes.agora.R;


public class ProposalAdapter extends BaseAdapter {

    private List<Proposal> listProposals;
    private Context context;
    private Boolean votado = false;
    private Boolean unvote = false;
    private Boolean favo = false;

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
        final ImageView likeimagen = (ImageView) convertView.findViewById(R.id.like);
        final ImageView dislikeimagen = (ImageView) convertView.findViewById(R.id.dislike);
        final ImageView favorite = (ImageView) convertView.findViewById(R.id.fav);

        final Proposal proposal = listProposals.get(position);

        titol.setText(proposal.getTitle());
        descripcio.setText(proposal.getDescription());
        owner.setText(proposal.getOwner());
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
            @Override
            public void onClick(View v) {
                if (favo){
                    favorite.setImageResource(R.drawable.ic_favorite);
                    favo = false;
                } else {
                    favorite.setImageResource(R.drawable.ic_favorite_red);
                    favo = true;
                }

            }
        });

        return convertView;
    }
}

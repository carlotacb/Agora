package edu.upc.pes.agora.Logic.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.upc.pes.agora.Logic.Models.Proposal;
import edu.upc.pes.agora.Presentation.DetailsProposalActivity;
import edu.upc.pes.agora.R;

/**
 * Created by carlo on 25/11/2017.
 */

public class ProposalAdapter extends BaseAdapter {

    private List<Proposal> listProposals;
    private Context context;

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

        final Proposal proposal = listProposals.get(position);

        titol.setText(proposal.getTitle());
        descripcio.setText(proposal.getDescription());
        owner.setText(proposal.getOwner());
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

        return convertView;
    }
}

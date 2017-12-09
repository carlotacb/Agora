package edu.upc.pes.agora.Logic;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import edu.upc.pes.agora.Presentation.DetailsProposalActivity;
import edu.upc.pes.agora.R;

/**
 * Created by carlo on 25/11/2017.
 */

public class ProposalAdapter extends BaseAdapter {

    private List<Proposals> listProposals;
    private Context context;

    public ProposalAdapter(List<Proposals> listProposals, Context context) {
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
            convertView = View.inflate(context, R.layout.proposals_item, null);
        }

        final Resources res = convertView.getResources();

        TextView titol = (TextView) convertView.findViewById(R.id.titolcard);
        TextView descripcio = (TextView) convertView.findViewById(R.id.descripciocard);
        TextView owner = (TextView) convertView.findViewById(R.id.owner);
        //ImageView button = (ImageView) convertView.findViewById(R.id.compartir);
        TextView moreinfo = (TextView) convertView.findViewById(R.id.btnLernMore);
        TextView categoria = (TextView) convertView.findViewById(R.id.categoria);

        final Proposals proposal = listProposals.get(position);

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
        else if (c.equals("A")) c = context.getString(R.string.todo); // para mostrar todas las categorias



        categoria.setText(c);

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("asdCompartir", "true");
                //     Toast.makeText(v.getContext(),"entro al twitter", Toast.LENGTH_LONG).show();
                String intro = "Mira que propuesta he encontrado en Agora!";
                String title = proposal.getTitle();
                String description = proposal.getDescription();
                //  String s = "titulo";
                //  String s2 = "descripcion";
                //         Toast.makeText(v.getRootView().getContext(),"Twitter is not installed on this device",Toast.LENGTH_LONG).show();
                String tweetUrl = "https://twitter.com/intent/tweet?text=" + intro + "<br>"+ "<br>" +title + "<br>"+ description + "&url=";
                tweetUrl = Html.fromHtml(tweetUrl).toString();
                Uri uri = Uri.parse(tweetUrl);
                v.getRootView().getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                //     v.getContext().getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                //  c.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });*/

        moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(v.getContext(), DetailsProposalActivity.class);
                myIntent.putExtra("Title", proposal.getTitle());
                myIntent.putExtra("Description", proposal.getDescription());
                myIntent.putExtra("id", proposal.getId());
                myIntent.putExtra("Owner", proposal.getOwner());
                myIntent.putExtra("categoria", proposal.getCategoria());
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

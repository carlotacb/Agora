package edu.upc.pes.agora.Logic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

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

        TextView titol = (TextView) convertView.findViewById(R.id.titolcard);
        TextView descripcio = (TextView) convertView.findViewById(R.id.descripciocard);

        Proposals proposal = listProposals.get(position);

        titol.setText(proposal.getTitle());
        descripcio.setText(proposal.getDescription());


        return convertView;
    }
}

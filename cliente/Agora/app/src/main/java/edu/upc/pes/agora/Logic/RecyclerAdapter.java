package edu.upc.pes.agora.Logic;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.upc.pes.agora.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private LayoutInflater inflater;
    private List<Proposals> listProposals;
    private Context context;

    public RecyclerAdapter(List<Proposals> listProposals, Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listProposals = listProposals;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item,parent,false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        Proposals proposal = listProposals.get(position);

        holder.title.setText(proposal.getTitle());
        holder.description.setText(proposal.getDescription());
    }

    @Override
    public int getItemCount() {
        return listProposals.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;


        public RecyclerHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textHead);
            description = (TextView) itemView.findViewById(R.id.textDescription);

        }
    }
}

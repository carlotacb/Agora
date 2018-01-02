package edu.upc.pes.agora.Logic.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upc.pes.agora.Logic.Models.Proposal;
import edu.upc.pes.agora.Logic.ServerConection.DeleteAsyncTask;
import edu.upc.pes.agora.Presentation.EditProposalActivity;
import edu.upc.pes.agora.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private LayoutInflater inflater;
    private List<Proposal> listProposals;
    private Context context;

    public RecyclerAdapter(List<Proposal> listProposals, Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listProposals = listProposals;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_my_proposal_item,parent,false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, final int position) {
        final Proposal proposal = listProposals.get(position);

        final Resources res = context.getResources();

        holder.title.setText(proposal.getTitle());
        final String tit = proposal.getTitle();
        holder.description.setText(proposal.getDescription());
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(v.getRootView().getContext(), holder.more);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.item_editar:
                                Intent myIntent = new Intent(context, EditProposalActivity.class);
                                myIntent.putExtra("Title", proposal.getTitle());
                                myIntent.putExtra("Description", proposal.getDescription());
                                myIntent.putExtra("id", proposal.getId());
                                myIntent.putExtra("Categoria", proposal.getCategoria());
                                myIntent.putExtra("lat", proposal.getLat());

                                myIntent.putExtra("lng", proposal.getLng());
                                v.getRootView().getContext().startActivity(myIntent);

                                Toast.makeText(context, "nose", Toast.LENGTH_SHORT).show();

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
                                                    listProposals.remove(proposal);
                                                    RecyclerAdapter.this.notifyDataSetChanged();
                                                }
                                                else {
                                                    try {
                                                        Toast.makeText(context, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                super.onPostExecute(jsonObject);
                                            }
                                        }.execute();

                                        String borratok = String.format(res.getString(R.string.Borrado), tit);
                                        Toast.makeText(context, borratok, Toast.LENGTH_SHORT).show();
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
    public int getItemCount() {
        return listProposals.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener, PopupMenu.OnMenuItemClickListener*/{

        private TextView title;
        private TextView description;
        private ImageView more;

        public RecyclerHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textHead);
            description = (TextView) itemView.findViewById(R.id.textDescription);
            more = (ImageView) itemView.findViewById(R.id.more);

        }
    }
}
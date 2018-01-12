package edu.upc.pes.agora.Logic.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import edu.upc.pes.agora.Presentation.DetailsProposalActivity;
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

        holder.categoria.setText(c);
        holder.numcomentaris.setText(String.valueOf(proposal.getNumerocomentarios()));
        holder.numdislikes.setText(String.valueOf(proposal.getNumerounvotes()));
        holder.numlikes.setText(String.valueOf(proposal.getNumerovotes()));
        holder.created.setText(String.format(res.getString(R.string.creadoel), proposal.getCreation()));

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

        holder.vermas.setOnClickListener(new View.OnClickListener() {
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
                Log.i("asd123", String.valueOf(proposal.getLat()));
                Log.i("asd123", String.valueOf(proposal.getLng()));
                myIntent.putExtra("Creation", proposal.getCreation());
                myIntent.putExtra("Update", proposal.getUpdate());
                myIntent.putExtra("ncomentarios", proposal.getNumerocomentarios());
                myIntent.putExtra("nvotes", proposal.getNumerovotes());
                myIntent.putExtra("nunvotes", proposal.getNumerounvotes());
                myIntent.putExtra("favorit", proposal.getFavorite());
                myIntent.putExtra("votacion", proposal.getVotacion());
                myIntent.putExtra("deMyProposals", "vedeproposasls");

                v.getContext().startActivity(myIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listProposals.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView categoria;
        private TextView created;
        private TextView numcomentaris;
        private TextView numlikes;
        private TextView numdislikes;
        private TextView vermas;
        private ImageView more;

        public RecyclerHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textHead);
            description = (TextView) itemView.findViewById(R.id.textDescription);
            categoria = (TextView) itemView.findViewById(R.id.categoriaproposal);
            created = (TextView) itemView.findViewById(R.id.data);
            numcomentaris = (TextView) itemView.findViewById(R.id.numerocomentaris);
            numlikes = (TextView) itemView.findViewById(R.id.numerovote);
            numdislikes = (TextView) itemView.findViewById(R.id.numerounvote);
            more = (ImageView) itemView.findViewById(R.id.more);
            vermas = (TextView) itemView.findViewById(R.id.btnLernMore);

        }
    }
}
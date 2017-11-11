package edu.upc.pes.agora.Logic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upc.pes.agora.Presentation.EditProposalActivity;
import edu.upc.pes.agora.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private LayoutInflater inflater;
    private List<Proposals> listProposals;
    private Context context;

    //private Boolean Desplegat = false;

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
    public void onBindViewHolder(final RecyclerHolder holder, final int position) {
        final Proposals proposal = listProposals.get(position);

        final Boolean[] Desplegat = {false};

        holder.title.setText(proposal.getTitle());
        holder.description.setText(proposal.getDescription());
        holder.llayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "You cliked " + proposal.getTitle(), Toast.LENGTH_SHORT).show();

                if (Desplegat[0]) {
                    holder.description.setVisibility(View.GONE);
                    holder.edit.setVisibility(View.GONE);
                    holder.borrar.setVisibility(View.GONE);
                    Desplegat[0] = false;
                }

                else {
                    holder.description.setVisibility(View.VISIBLE);
                    holder.edit.setVisibility(View.VISIBLE);
                    holder.borrar.setVisibility(View.VISIBLE);
                    Desplegat[0] = true;
                }
            }
        });

        holder.borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(context, "BORRAR", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder((Activity) view.getRootView().getContext() );
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿ Esta seguro de eliminar esta propuesta ?");
                dialogo1.setCancelable(false);
                dialogo1.setIcon(R.drawable.logo);
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Toast.makeText(context, "ACEPTADO", Toast.LENGTH_SHORT).show();

                        Toast.makeText(context, "ID" + proposal.getId(), Toast.LENGTH_SHORT).show();

                        new DeleteAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + proposal.getId(), view.getRootView().getContext()){
                            @Override
                            protected void onPostExecute(JSONObject jsonObject) {
                                Toast.makeText(context, "dentro del onpostExecute", Toast.LENGTH_SHORT).show();
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

                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Toast.makeText(context, "CANCELADO", Toast.LENGTH_SHORT).show();
                    }
                }).show();

                // pop up preguntant si esborrar
                // cridar delete async task
                // refresh de la llista



            }
        });

         holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Editar", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(context, EditProposalActivity.class);
                myIntent.putExtra("Title", proposal.getTitle());
                myIntent.putExtra("Description", proposal.getDescription());
                myIntent.putExtra("id", proposal.getId());
                context.startActivity(myIntent);
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
        private LinearLayout llayout;

        private Button edit;
        private Button borrar;


        public RecyclerHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textHead);
            description = (TextView) itemView.findViewById(R.id.textDescription);
            llayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            edit = (Button) itemView.findViewById(R.id.editproposal);
            borrar = (Button) itemView.findViewById(R.id.erraseproposal);

        }
    }
}

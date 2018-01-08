package edu.upc.pes.agora.Logic.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.PopupMenu;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.Logic.Models.Comment;
import edu.upc.pes.agora.Logic.ServerConection.DeleteAsyncTask;
import edu.upc.pes.agora.Logic.ServerConection.PutAsyncTask;
import edu.upc.pes.agora.Logic.Utils.Helpers;
import edu.upc.pes.agora.R;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private ArrayList<Comment> listcomentaris;
    private String m_Text = "";
    private String newComent;

    public CommentAdapter(Context context, ArrayList<Comment> coment) {
        super(context, 0, coment);
        listcomentaris = coment;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final Comment comentaris = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_comment_item, parent, false);
        }

        // Lookup view for data population
        TextView Autor = (TextView) convertView.findViewById(R.id.autorcomentari);
        final TextView Content = (TextView) convertView.findViewById(R.id.descripciocomentari);
        final ImageView more = (ImageView) convertView.findViewById(R.id.more);
        final TextView data = (TextView) convertView.findViewById(R.id.dia);

        // Populate the data into the template view using the data object
        assert comentaris != null;
        Autor.setText(comentaris.getAutor());
        Content.setText(comentaris.getComentario());
        data.setText(Helpers.showDate(comentaris.getCreated()));

        if (comentaris.getAutor().equals(Constants.Username.toLowerCase())) {
            more.setVisibility(View.VISIBLE);
        }

        else {
            more.setVisibility(View.GONE);
        }

        final Resources res = getContext().getResources();

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(v.getRootView().getContext(), more);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.item_editar:

                                AlertDialog.Builder dialogoeditar = new AlertDialog.Builder(v.getRootView().getContext());

                                dialogoeditar.setTitle(res.getString(R.string.editarcomentario));
                                dialogoeditar.setMessage(res.getString(R.string.mensajeditar));
                                dialogoeditar.setCancelable(false);
                                dialogoeditar.setIcon(R.drawable.logo);
                                dialogoeditar.setCancelable(false);

                                final TextInputLayout textInputLayout = new TextInputLayout(v.getRootView().getContext());
                                final EditText input = new EditText(v.getRootView().getContext());
                                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                                input.setSingleLine();
                                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params.leftMargin = v.getRootView().getContext().getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                                input.setLayoutParams(params);

                                //input.setPadding(10,5,5,10);
                                input.setText(comentaris.getComentario());

                                input.setInputType(InputType.TYPE_CLASS_TEXT);
                                dialogoeditar.setView(input);

                                dialogoeditar.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                                    @SuppressLint("StaticFieldLeak")
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        JSONObject values = new JSONObject();
                                        try {
                                            newComent = m_Text = input.getText().toString();
                                            values.put("comment", newComent);
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        new PutAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + comentaris.getIdentificadorProp() + "/comment/" + comentaris.getIdentificador(), v.getRootView().getContext()){
                                            protected void onPostExecute(JSONObject resObject) {
                                                Boolean result = false;

                                                try {

                                                    if (resObject.has("success")) {
                                                        result = resObject.getBoolean("success");
                                                        comentaris.setComentario(m_Text);
                                                        Content.setText(comentaris.getComentario());
                                                    }

                                                    if (!result && resObject.has("errorMessage")) {
                                                        String error = resObject.getString("errorMessage");
                                                        Log.i("asdEdicion", error);
                                                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.execute(values);

                                        CommentAdapter.this.notifyDataSetChanged();

                                        Toast.makeText(getContext(), "Comentario Editado", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                dialogoeditar.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                dialogoeditar.show();

                                break;

                            case R.id.item_delete:

                                String com = comentaris.getComentario();

                                final AlertDialog.Builder dialogoborrar = new AlertDialog.Builder(v.getRootView().getContext());
                                dialogoborrar.setTitle(res.getString(R.string.elimiarcomentario));
                                dialogoborrar.setMessage(String.format(res.getString(R.string.mensajeliminar), com));
                                dialogoborrar.setCancelable(false);
                                dialogoborrar.setIcon(R.drawable.logo);
                                dialogoborrar.setCancelable(false);
                                dialogoborrar.setPositiveButton(res.getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
                                    @SuppressLint("StaticFieldLeak")
                                    public void onClick(DialogInterface dialogoborrar, int id) {

                                        new DeleteAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + comentaris.getIdentificadorProp() + "/comment/" + comentaris.getIdentificador(), v.getRootView().getContext()){
                                            @Override
                                            protected void onPostExecute(JSONObject jsonObject) {
                                                if (!jsonObject.has("error")) {
                                                    listcomentaris.remove(comentaris);
                                                    CommentAdapter.this.notifyDataSetChanged();
                                                }
                                                else {
                                                    try {
                                                        Toast.makeText(getContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                super.onPostExecute(jsonObject);
                                            }
                                        }.execute();

                                        Toast.makeText(getContext(), "Comentario Borrado", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                dialogoborrar.setNegativeButton(res.getString(R.string.Cancelar), new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialogo1, int id) {
                                        dialogo1.cancel();
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


        // Return the completed view to render on screen
        return convertView;
    }
}

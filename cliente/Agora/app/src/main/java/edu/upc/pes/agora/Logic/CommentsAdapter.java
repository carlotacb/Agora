package edu.upc.pes.agora.Logic;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.upc.pes.agora.Presentation.EditProposalActivity;
import edu.upc.pes.agora.R;

public class CommentsAdapter extends ArrayAdapter<Comment> {

    public CommentsAdapter(Context context, ArrayList<Comment> coment) {
        super(context, 0, coment);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Comment comentaris = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Lookup view for data population
        TextView Autor = (TextView) convertView.findViewById(R.id.autorcomentari);
        TextView Content = (TextView) convertView.findViewById(R.id.descripciocomentari);
        final ImageView more = (ImageView) convertView.findViewById(R.id.more);

        // Populate the data into the template view using the data object
        assert comentaris != null;
        Autor.setText(comentaris.getAutor());
        Content.setText(comentaris.getComentario());

        if (comentaris.getAutor().equals(Constants.Username)) {
            more.setVisibility(View.VISIBLE);
        }

        else {
            more.setVisibility(View.GONE);
        }

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(v.getRootView().getContext(), more);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.item_editar:

                                break;

                            case R.id.item_delete:

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

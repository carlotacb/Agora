package edu.upc.pes.agora.Logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
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
        TextView Content = (TextView)convertView.findViewById(R.id.descripciocomentari);

        // Populate the data into the template view using the data object
        assert comentaris != null;
        Autor.setText(comentaris.getAutor());
        Content.setText(comentaris.getComentario());

        // Return the completed view to render on screen
        return convertView;
    }
}

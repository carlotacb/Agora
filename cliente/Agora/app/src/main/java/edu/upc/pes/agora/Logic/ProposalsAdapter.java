package edu.upc.pes.agora.Logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import edu.upc.pes.agora.R;

public class ProposalsAdapter extends ArrayAdapter<Proposals> {

    public ProposalsAdapter(Context context, ArrayList<Proposals> propo) {
        super(context, 0, propo);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Proposals proposals = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.titleTextViewItem);
        TextView description = (TextView)convertView.findViewById(R.id.descriptionTextViewItem);

        // Populate the data into the template view using the data object
        assert proposals != null;
        title.setText(proposals.getTitle() + " (" + proposals.getOwner() + ")");
        description.setText(proposals.getDescription());

        // Return the completed view to render on screen
        return convertView;
    }
}

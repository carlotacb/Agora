package edu.upc.pes.agora.Logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
        final Proposals proposals = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.proposals_item, parent, false);
        }

        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.titolcard);
        TextView description = (TextView)convertView.findViewById(R.id.descripciocard);

        ImageButton button = (ImageButton) convertView.findViewById(R.id.compartir);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("asdCompartir", "true");
               //     Toast.makeText(v.getContext(),"entro al twitter", Toast.LENGTH_LONG).show();
                    String intro = "Mira que propuesta he encontrado en Agora!";
                    String title = proposals.getTitle();
                    String description = proposals.getDescription();
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
            });

        // Populate the data into the template view using the data object
        assert proposals != null;
        title.setText(proposals.getTitle() + " (" + proposals.getOwner() + ")");
        description.setText(proposals.getDescription());

        // Return the completed view to render on screen
        return convertView;
    }
}

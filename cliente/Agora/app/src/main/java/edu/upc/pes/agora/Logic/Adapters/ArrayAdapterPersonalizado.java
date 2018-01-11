package edu.upc.pes.agora.Logic.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.upc.pes.agora.Logic.Models.Proposal;
import edu.upc.pes.agora.Logic.ServerConection.PostAsyncTask;
import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.Presentation.DetailsProposalActivity;
import edu.upc.pes.agora.Presentation.MyProfileActivity;
import edu.upc.pes.agora.Presentation.OtherUserActivity;
import edu.upc.pes.agora.R;

public class ArrayAdapterPersonalizado extends BaseAdapter {

    private Context context;
    private List<String> values;
    private int limite;

    public ArrayAdapterPersonalizado(Context context, List<String> objects,int position) {
        this.context = context;
        this.values = objects;
        this.limite = position;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = View.inflate(context, R.layout.card_logros, null);
        }

        String item_value = values.get(position);

        TextView tv = (TextView) convertView.findViewById(R.id.textHead);
        CardView c2 = (CardView) convertView.findViewById(R.id.cardview2);
        if (position > limite-1)c2.setBackgroundColor(Color.LTGRAY);
      //  if (limite-1 <= position) c2.setBackgroundColor(Color.LTGRAY);
        else c2.setBackgroundColor(Color.WHITE);
        tv.setText(item_value);
        tv.setTextColor(Color.BLACK);

        return convertView;
    }
}


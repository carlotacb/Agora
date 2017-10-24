package edu.upc.pes.agora;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by carlo on 24/10/2017.
 */

/*public class SpinnerAdapter extends ArrayAdapter<ItemData> {

    Integer grupid;
    Activity context;
    ArrayList<ItemData> list;
    LayoutInflater inflater;

    public SpinnerAdapter(Activity context, Integer grupid, Integer id, ArrayList<ItemData> list) {

        super(context, id, list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.grupid = grupid;

    }

    public View getView(Integer position, View convertView, ViewGroup parent) {

        View itemView = inflater.inflate(grupid, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(position).getImageId());

        TextView textView = (TextView) itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getText());

        return itemView;
    }

    public View getDropDownView(Integer position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
*/

public class SpinnerAdapter extends ArrayAdapter<ItemData> {
    int groupid;
    Activity context;
    ArrayList<ItemData> list;
    LayoutInflater inflater;

    public SpinnerAdapter(Activity context, int groupid, int id, ArrayList<ItemData>
            list) {
        super(context, id, list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(groupid, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(position).getImageId());
        TextView textView = (TextView) itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getText());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent) {
        return getView(position, convertView, parent);

    }
}


package edu.upc.pes.agora.Logic.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.upc.pes.agora.R;

/**
 * Created by gerar on 04/01/2018.
 */

public class ArrayAdapterPersonalizado extends ArrayAdapter {
    private final Context context;
    private List<String> values;
    private int limite;

    public ArrayAdapterPersonalizado(@NonNull Context context, List<String> objects,int position) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context=context;
        this.values=objects;
        this.limite = position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String item_value = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView tv = (TextView) rowView.findViewById(android.R.id.text1);
        if (limite <= position)rowView.setBackgroundColor(Color.LTGRAY);
        tv.setText(item_value);
        tv.setTextColor(Color.BLACK);

        return rowView;
    }
}

package edu.upc.pes.agora.Logic.Adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.upc.pes.agora.Logic.Models.ImatgeItem;
import edu.upc.pes.agora.Logic.Models.Proposal;
import edu.upc.pes.agora.R;

public class ImatgesAdapter extends BaseAdapter {

    List<ImatgeItem> imatges;
    Context context;

    public ImatgesAdapter(Context context, List<ImatgeItem> imatges) {
        this.context = context;
        this.imatges = imatges;
    }

    @Override
    public int getCount() {
        return imatges.size();
    }

    @Override
    public Object getItem(int position) {
        return imatges.get(position);
    }

    @Override
    public long getItemId(int position) {
        return imatges.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = View.inflate(context, R.layout.imatge_list, null);
        }

        TextView numero = (TextView) convertView.findViewById(R.id.imatgenum);
        ImageView imatgeprop = (ImageView) convertView.findViewById(R.id.imatgellista);
        ImageView errase = (ImageView) convertView.findViewById(R.id.borrarfoto);

        final ImatgeItem imatgeitem = imatges.get(position);

        byte[] imageAsBytes = Base64.decode(imatgeitem.getImatge().getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        imatgeprop.setImageBitmap(bitmap);

        String num = String.valueOf(imatgeitem.getNumero());

        numero.setText(num);

        errase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imatges.remove(imatgeitem);
                ImatgesAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }
}

package edu.upc.pes.agora.Logic.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.upc.pes.agora.Logic.Models.ImatgeItem;
import edu.upc.pes.agora.Logic.Models.Imatgev2;
import edu.upc.pes.agora.R;

public class ImageAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Imatgev2> data = new ArrayList();

    public ImageAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Imatgev2 item = data.get(position);
        String imatge = item.getImage();

        byte[] imageAsBytes = Base64.decode(imatge.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        holder.image.setImageBitmap(bitmap);
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
    /*private Context mContext;
    List<ImatgeItem> imatges;

    public ImageAdapter(Context c, List<ImatgeItem> imatges) {
        mContext = c;
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
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        final ImatgeItem imatgeitem = imatges.get(position);

        byte[] imageAsBytes = Base64.decode(imatgeitem.getImatge().getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        imageView.setImageBitmap(bitmap);

        return imageView;
    }*/

}


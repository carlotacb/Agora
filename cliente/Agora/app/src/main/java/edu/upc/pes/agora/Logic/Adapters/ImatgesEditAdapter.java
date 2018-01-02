package edu.upc.pes.agora.Logic.Adapters;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.upc.pes.agora.Logic.Models.ImatgeItem;
import edu.upc.pes.agora.Logic.Models.Proposal;
import edu.upc.pes.agora.Logic.ServerConection.DeleteAsyncTask;
import edu.upc.pes.agora.R;

public class ImatgesEditAdapter extends BaseAdapter {

    List<ImatgeItem> imatges;
    Context context;

    public ImatgesEditAdapter(Context context, List<ImatgeItem> imatges) {
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

        final Resources res = context.getResources();

        final ImatgeItem imatgeitem = imatges.get(position);

        byte[] imageAsBytes = Base64.decode(imatgeitem.getImatge().getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        imatgeprop.setImageBitmap(bitmap);

        final Integer proposalID = imatgeitem.getIdproposta();

        String num = String.valueOf(imatgeitem.getNumero());

        numero.setText(num);

        errase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(v.getRootView().getContext());
                dialogo1.setTitle(res.getString(R.string.importante));
                dialogo1.setMessage(res.getString(R.string.seguro));
                dialogo1.setCancelable(false);
                dialogo1.setIcon(R.drawable.logo);
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton(res.getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    public void onClick(DialogInterface dialogo1, int id) {

                        new DeleteAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + proposalID + "/image/" + imatgeitem.getNumero(), v.getRootView().getContext()){
                            @Override
                            protected void onPostExecute(JSONObject jsonObject) {
                                if (!jsonObject.has("error")) {
                                    imatges.remove(imatgeitem);
                                    ImatgesEditAdapter.this.notifyDataSetChanged();
                                }
                                else {
                                    try {
                                        Toast.makeText(context, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                super.onPostExecute(jsonObject);
                            }
                        }.execute();

                    }
                });
                dialogo1.setNegativeButton(res.getString(R.string.Cancelar), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                }).show();
            }
        });

        return convertView;
    }
}

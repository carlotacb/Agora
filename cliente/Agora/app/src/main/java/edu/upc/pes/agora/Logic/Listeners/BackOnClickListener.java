package edu.upc.pes.agora.Logic.Listeners;


import android.content.Context;
import android.content.Intent;
import android.view.View;

public class BackOnClickListener implements View.OnClickListener {

    Intent refresh;
    Context mContext;

    public BackOnClickListener(Intent actu, Context context) {
        refresh = actu;
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        mContext.startActivity(refresh);
    }
}

package edu.upc.pes.agora.Logic.Listeners;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import java.util.Locale;

import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.R;

public class LanguageOnClickListener implements View.OnClickListener {

    ImageView canviidioma2;
    private Configuration config = new Configuration();
    private Locale locale;
    Resources res;
    Intent refresh;
    Context mContext;

    public LanguageOnClickListener(Intent actu, ImageView canviidioma, Resources resources, Context context) {
        canviidioma2 = canviidioma;
        res = resources;
        refresh = actu;
        mContext = context;
    }

    @Override public void onClick(final View v) {

        PopupMenu popupMenu = new PopupMenu(v.getRootView().getContext(), canviidioma2);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.men_castella:
                        Constants.Idioma = "es";
                        break;

                    case R.id.men_catala:
                        Constants.Idioma = "ca";
                        break;

                    case R.id.men_angles:
                        Constants.Idioma = "en";
                        break;
                }

                locale = new Locale(Constants.Idioma);
                config.locale = locale;
                res.updateConfiguration(config, null);
                mContext.startActivity(refresh);
                return false;
            }
        });
        popupMenu.inflate(R.menu.idioma);
        popupMenu.show();
    }

}

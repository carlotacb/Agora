package edu.upc.pes.agora.Logic;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class DrawerToggleAdvanced extends ActionBarDrawerToggle {
    private Activity activity;

    public DrawerToggleAdvanced(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.activity = activity;
    }


    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        if (slideOffset != 0) {
            // Check if no view has focus:
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        super.onDrawerSlide(drawerView, slideOffset);
    }
}
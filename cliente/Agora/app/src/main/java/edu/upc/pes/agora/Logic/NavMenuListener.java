package edu.upc.pes.agora.Logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upc.pes.agora.Presentation.LoginActivity;
import edu.upc.pes.agora.Presentation.MainActivity;
import edu.upc.pes.agora.Presentation.MyProposalsActivity;
import edu.upc.pes.agora.Presentation.ProfileActivity;
import edu.upc.pes.agora.Presentation.ProposalsActivity;
import edu.upc.pes.agora.R;

public class NavMenuListener implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private DrawerLayout navDrawer;
    private static boolean activityChanged;

    public static final int homneButton = 0;
    public static final int myproposals = 1;
    public static final int profile = 2;

    public NavMenuListener(Context context, DrawerLayout navDrawer) {
        this.context = context;
        this.navDrawer = navDrawer;
    }

    public static boolean getActivityChanged() {
        if (activityChanged) {
            activityChanged = false;
            return true;
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Va a la pagina principal on surten totes les propostes
            if (!context.getClass().equals(MainActivity.class)) {
                Intent myIntent = new Intent(context, MainActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

        } else if (id == R.id.nav_myporposals) {
            // Va a la pantalla de MyProposals
            if (!context.getClass().equals(MyProposalsActivity.class)) {
                Intent myIntent = new Intent(context, MyProposalsActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

        } else if (id == R.id.nav_perfilprinc) {
            // Va a la Pagina principal del perfil
            if (!context.getClass().equals(ProfileActivity.class)) {
                Intent myIntent = new Intent(context, ProfileActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

        } else if (id == R.id.nav_perfilprinc) {
            // Va a la Pagina principal del perfil
            if (!context.getClass().equals(ProfileActivity.class)) {
                Intent myIntent = new Intent(context, ProfileActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

        }  else if (id == R.id.nav_logout) {
            // Es desasigna el token per sortir de l'aplicació
            JSONObject jObject = new JSONObject();

            new DeleteAsyncTask("https://agora-pes.herokuapp.com/api/logout", context) {
                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    try {
                        if(jsonObject.has("error")) {
                            String error = jsonObject.get("error").toString();
                            Log.i("asd123", "Error");
                        }
                        else {
                            Helpers.logout(context);
                            Intent myIntent = new Intent(context, LoginActivity.class);
                            context.startActivity(myIntent);
                        }
                    } catch (JSONException ignored) {
                        Log.i("DEBUG","error al get user");
                    }
                }
            }.execute(jObject);
        }

        activityChanged = true;
        return true;
    }
}
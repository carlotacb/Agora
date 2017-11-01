package edu.upc.pes.agora.Logic;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import edu.upc.pes.agora.Presentation.MainActivity;
import edu.upc.pes.agora.Presentation.propuestaActivity;
import edu.upc.pes.agora.R;

public class NavMenuListener implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private DrawerLayout navDrawer;
    private static boolean activityChanged;

    public static final int homneButton = 0;
    public static final int addproposalbutton = 1;

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

        } else if (id == R.id.nav_addproposta) {
            if (!context.getClass().equals(propuestaActivity.class)) {
                Intent myIntent = new Intent(context, propuestaActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

            //  Va a Propostes
        } else if (id == R.id.nav_perfilprinc) {
            // Va a la Pagina principal del perfil
        } else if (id == R.id.nav_editperf) {
            // Va a editar perfil
        } else if (id == R.id.nav_logout) {
            // Logout --> TODO: Desasignacion de token
        }

        activityChanged = true;
        return true;
    }
}


        /*switch (menuItem.getItemId()) {
            case R.id.home_button: {

                break;
            }
            case R.id.switchActivity: {
                if (!context.getClass().equals(RecyclerViewActivity.class)) {
                    Intent myIntent = new Intent(context, RecyclerViewActivity.class);
                    context.startActivity(myIntent);
                }
                navDrawer.closeDrawers();
                break;
            }
            case R.id.addFilmButton: {
                if (!context.getClass().equals(InsertFilmActivity.class)) {
                    Intent myIntent = new Intent(context, InsertFilmActivity.class);
                    context.startActivity(myIntent);
                }
                navDrawer.closeDrawers();
                break;
            }
            case R.id.help_button: {
                if (!context.getClass().equals(HelpActivity.class)) {
                    Intent myIntent = new Intent(context, HelpActivity.class);
                    context.startActivity(myIntent);
                }
                navDrawer.closeDrawers();
                break;
            }
            case R.id.about_button: {
                if (!context.getClass().equals(AboutActivity.class)) {
                    Intent myIntent = new Intent(context, AboutActivity.class);
                    context.startActivity(myIntent);
                }
                navDrawer.closeDrawers();
                break;
            }*/

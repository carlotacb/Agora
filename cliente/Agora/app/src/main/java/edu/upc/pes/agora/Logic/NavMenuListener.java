package edu.upc.pes.agora.Logic;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import edu.upc.pes.agora.Presentation.EditProfileActivity;
import edu.upc.pes.agora.Presentation.LoginActivity;
import edu.upc.pes.agora.Presentation.MainActivity;
import edu.upc.pes.agora.Presentation.MyPropuestasActivity;
import edu.upc.pes.agora.Presentation.ProfileActivity;
import edu.upc.pes.agora.Presentation.propuestaActivity;
import edu.upc.pes.agora.R;

public class NavMenuListener implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private DrawerLayout navDrawer;
    private static boolean activityChanged;

    public static final int homneButton = 0;
    public static final int addproposalbutton = 1;
    public static final int myproposals = 2;
    public static final int profile = 3;
    public static final int editprofile = 4;

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

            //  Va a Proposals
        } else if (id == R.id.nav_myporposals) {
            if (!context.getClass().equals(MyPropuestasActivity.class)) {
                Intent myIntent = new Intent(context, MyPropuestasActivity.class);
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
        } else if (id == R.id.nav_editperf) {
            // Va a editar perfil --> TODO: poner el boton dentro del perfil
            if (!context.getClass().equals(EditProfileActivity.class)) {
                Intent myIntent = new Intent(context, EditProfileActivity.class);
                context.startActivity(myIntent);
            }
        } else if (id == R.id.nav_logout) {
            // Logout --> TODO: Desasignacion de token

            Helpers.logout(context);

            if (!context.getClass().equals(LoginActivity.class)) {
                Intent myIntent = new Intent(context, LoginActivity.class);
                context.startActivity(myIntent);
            }
        }

        activityChanged = true;
        return true;
    }
}
package edu.upc.pes.agora.Presentation;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.upc.pes.agora.Logic.Listeners.DrawerToggleAdvanced;
import edu.upc.pes.agora.Logic.Listeners.NavMenuListener;
import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.R;

public class LogrosActivity extends AppCompatActivity {

    private List<String> logros = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navMenu);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        TextView headerUserName = (TextView) navigationView.findViewById(R.id.head_username);
        headerUserName.setText(Constants.Username);

        navigationView.getMenu().getItem(NavMenuListener.logros).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavMenuListener(this, drawer));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.logros);
        setSupportActionBar(toolbar);

        DrawerToggleAdvanced toggle = new DrawerToggleAdvanced(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        ListView listView = (ListView) findViewById(R.id.lista);
        logros.add("Crear 1 propuesta");
        logros.add("Crear 5 propuestas");
        logros.add("Crear 10 propuestas");
        logros.add("Compartir 1 propuesta en Twitter");
        logros.add("Compartir 5 propuestas en Twitter");
        logros.add("Compartir 10 propuestas en Twitter");

        final List<Boolean> list = new ArrayList<Boolean>(Arrays.asList(new Boolean[10]));
        Collections.fill(list, Boolean.FALSE);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logros);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setBackgroundColor(Color.CYAN);
                Toast.makeText(getApplicationContext(), "Ha pulsado el item en posicion " + i, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Estado del boolean de la lista " + list.get(i), Toast.LENGTH_LONG).show();
           //     list.set(i,true);
           //     Toast.makeText(getApplicationContext(), "Estado del boolean de la lista version 2" + list.get(i), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LogrosActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_trophy, null);

                TextView textView = (TextView)mView.findViewById(R.id.textView);
                Button mAccept = (Button) mView.findViewById(R.id.etAccept);

                ImageView imageView = (ImageView) mView.findViewById(R.id.image);
                imageView.setImageResource(R.drawable.ic_trofeo_logro2);

             /*   if (i == 0){
                    textView.setText("Comparte 1 propuesta en twitter");
                }
                else if ( i == 1){
                    textView.setText("Logro generico");
                }*/

                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                mAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

}

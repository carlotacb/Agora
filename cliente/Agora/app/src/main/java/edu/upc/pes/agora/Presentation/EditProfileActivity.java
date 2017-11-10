package edu.upc.pes.agora.Presentation;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import edu.upc.pes.agora.Logic.DrawerToggleAdvanced;
import edu.upc.pes.agora.Logic.NavMenuListener;

import edu.upc.pes.agora.R;

public class EditProfileActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private Locale locale;

    private EditText Nombre;
    private EditText CP;
    private EditText Barrio;
    private EditText Fecha;
    private EditText Descripcion;

    private TextView Change;

    private Button Aceptar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_edit_profile);
        toolbar.setLogo(R.mipmap.ic_editw);
        setSupportActionBar(toolbar);


        Nombre = (EditText) findViewById(R.id.nameprofile);
        CP = (EditText) findViewById(R.id.codipostal);
        Barrio = (EditText) findViewById(R.id.barrio);
        Fecha = (EditText) findViewById(R.id.fecha);
        Descripcion = (EditText) findViewById(R.id.descript);

        Change = (TextView) findViewById(R.id.changePassword);

        Aceptar = (Button) findViewById(R.id.aceptar);


        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditProfileActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_changepass, null);

                final EditText mOldPass = (EditText) mView.findViewById(R.id.etPassword);
                final EditText mNewPass1 = (EditText) mView.findViewById(R.id.etPassword2);
                final EditText mNewPass2 = (EditText) mView.findViewById(R.id.etPassword3);
                Button mAccept = (Button) mView.findViewById(R.id.etAccept);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                mAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //implementar la obtencion del password del usuario
                        if (!mOldPass.getText().toString().equals("123") | mOldPass.equals("")){
                            mOldPass.setText("");
                            mNewPass1.setText("");
                            mNewPass2.setText("");

                            Toast.makeText(getApplicationContext(),"Password introducido incorrecto", Toast.LENGTH_LONG).show();

                        }
                        else if (!mNewPass1.getText().toString().equals(mNewPass2.getText().toString()) ){
                            mOldPass.setText("");
                            mNewPass1.setText("");
                            mNewPass2.setText("");
                            Toast.makeText(getApplicationContext(),"Nueva Password incorrecta", Toast.LENGTH_LONG).show();
                        }
                        else if (mNewPass1.getText().toString().equals("") | mNewPass2.getText().toString().equals("")) {
                            mOldPass.setText("");
                            mNewPass1.setText("");
                            mNewPass2.setText("");
                            Toast.makeText(getApplicationContext(),"Rellena todos los campos", Toast.LENGTH_LONG).show();
                        }
                        else {
                            //implementar el cambio de password del usuario
                            Toast.makeText(getApplicationContext(),"Password actualizado correctamente", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });


                Toast.makeText(getApplicationContext(),"A CAMBIAR PASSWORD", Toast.LENGTH_LONG).show();
               // show.DialogFragment();

            }
        });

        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // implementar cambios de los atributos del usuario en el servidor
                Toast.makeText(getApplicationContext(),"aqui cambiaremos los valores al server", Toast.LENGTH_LONG).show();

            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent refresh = new Intent(this, EditProfileActivity.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //noinspection SimplifiableIfStatement
        if (id == R.id.men_castella) {
            locale = new Locale("es");
            config.locale = locale;
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();
        }

        else if (id == R.id.men_catala){
            locale = new Locale("ca");
            config.locale = locale;
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();

        }

        else if (id == R.id.men_angles){
            locale = new Locale("en");
            config.locale = locale;
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}

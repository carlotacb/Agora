package edu.upc.pes.agora.Presentation;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.upc.pes.agora.Logic.DrawerToggleAdvanced;
import edu.upc.pes.agora.Logic.NavMenuListener;

import edu.upc.pes.agora.Logic.PostAsyncTask;
import edu.upc.pes.agora.Logic.Profile;
import edu.upc.pes.agora.R;

public class EditProfileActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    private Configuration config = new Configuration();
    private Locale locale;

    private EditText Nombre;
    private EditText CP;
    private EditText Barrio;
    private EditText Fecha;
    private EditText Descripcion;
    private Spinner spin;

    private TextView Change;

    private Button Aceptar;


    String[] diferentesSexos; //{getString(R.string.M), getString(R.string.F), getString(R.string.I)};
    String[] diferentesSexosGenerico = {"I", "F", "M"};

    Profile p ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_edit_profile);
        toolbar.setLogo(R.mipmap.ic_editw);
        setSupportActionBar(toolbar);

        diferentesSexos = new String[]{getString(R.string.I), getString(R.string.F), getString(R.string.M)};

        Nombre = (EditText) findViewById(R.id.nameprofile);
        CP = (EditText) findViewById(R.id.codipostal);
        Barrio = (EditText) findViewById(R.id.barrio);
        Fecha = (EditText) findViewById(R.id.fecha);
        Descripcion = (EditText) findViewById(R.id.descript);

        p = new Profile();
      /*  p.setName("pepe");
        p.setCP(123);
        p.setNeighborhood("sants");
        p.setBorn(new Date());
        */
        Intent i = getIntent();

        if(i.hasExtra("cp")) {
       //     editTitle.setText(i.getStringExtra("Title"));
            CP.setText(i.getIntExtra("cp",0));
        }
        if(i.hasExtra("barrio")) {
        //    editDescription.setText(i.getStringExtra("Description"));
            Barrio.setText(i.getStringExtra("barrio"));
        }
        if(i.hasExtra("nombre")){
            Nombre.setText(i.getStringExtra("nombre"));
        }
        if(i.hasExtra("fecha")){
            Long f = i.getLongExtra("fecha",0);
            if (f != 0) {
                Date b = new Date(f);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String s = dateFormat.format(b);
                Fecha.setText(s);
            }
        }


        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        // spin.setOnItemSelectedListener(this);
        spin = (Spinner) findViewById(R.id.sexo);

        if(i.hasExtra("sex")){
            spin.setSelection(i.getIntExtra("sex",0));
        }

        //  Nombre.setText(p.getName());
     //   if (p.getCP() != null)  CP.setText(String.valueOf(p.getCP()));

      //  Barrio.setText(p.getNeighborhood());

    /*    Date b = p.getBorn();
        if(b!= null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String s = dateFormat.format(b);
            Fecha.setText(s);
        }
*/

        Change = (TextView) findViewById(R.id.changePassword);

        Aceptar = (Button) findViewById(R.id.aceptar);

        final Resources res = this.getResources();



//Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,diferentesSexos);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);



        // Spinner element
     //   Spinner spinner = (Spinner) findViewById(R.id.sexo);

        // Spinner click listener
     //   spinner.setOnItemSelectedListener(this);


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

                JSONObject values = new JSONObject();
                try {
                  //  strTitulo = Titulo.getText().toString();
                  //  strDescripcion = Descripcion.getText().toString();


                    String nombre = Nombre.getText().toString() ;
                    String CPcode = CP.getText().toString() ;
                    String barrio = Barrio.getText().toString() ;
                    String fecha = Fecha.getText().toString() ;
                    String sexo = diferentesSexosGenerico[spin.getSelectedItemPosition()];
                    String descripcion = Descripcion.getText().toString() ;

                    values.put("bdate",fecha);
                    values.put("cpCode",CPcode);
                    values.put("sex",sexo);
                    values.put("neighborhood",barrio);
                    values.put("realname",nombre);
                    values.put("description",descripcion);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // nou server : agora-pes.herokuapp.com/api/proposal
                new PostAsyncTask("https://agora-pes.herokuapp.com/api/profile", EditProfileActivity.this) {
                    @Override
                    protected void onPostExecute(JSONObject resObject) {
                        Boolean result = false;


                        try {

                            if (resObject.has("success")) {
                                result = resObject.getBoolean("success");
                            }

                            if (!result && resObject.has("errorMessage")) {
                                String error = res.getString(R.string.errorCreacion);
                                Log.i("asdCreacion", error);
                                Toast.makeText(getApplicationContext(), error , Toast.LENGTH_LONG).show();
                            }
                            //Toast.makeText(getApplicationContext(), "Result : " + result , Toast.LENGTH_LONG).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.i("asdBool", result.toString());

                      //  String creacionok = String.format(res.getString(R.string.done), strTitulo);

                        if (result) {
                            //Toast.makeText(getApplicationContext(), "Titulo : " + strTitulo + " Descripcion : " + strDescripcion, Toast.LENGTH_LONG).show();
                       //     Toast.makeText(getApplicationContext(), creacionok, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                        }

                        else {
                            Log.i("asdCreacion", "reset");
                           // Titulo.setText("");
                            Descripcion.setText("");
                         //   Create.setVisibility(View.VISIBLE);
                         //   prog.setVisibility(View.GONE);
                        }

                    }
                }.execute(values);





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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

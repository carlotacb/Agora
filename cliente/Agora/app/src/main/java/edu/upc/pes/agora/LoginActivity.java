package edu.upc.pes.agora;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button login;
    private Spinner spin;
    private TextView register;
    private EditText etUsername, etPassword;
    private String username, password;
    private Configuration config = new Configuration();
    private Locale locale;

    private String[] data = {"Castellano", "Català", "English"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button)findViewById(R.id.btnLogin);
        register = (TextView)findViewById(R.id.btnRegister);

        spin = (Spinner)findViewById(R.id.spinner);

        // Primer valor indica el context, el segundo valor tipo de "estilo" que nos proporciona Android y el tercero los datos que queremos mostar
        //ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        //spin.setAdapter(adaptador);

        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.idiomes, android.R.layout.simple_spinner_dropdown_item);
        //spin.setAdapter(adapter);
        //spin.setOnItemSelectedListener(this);

        ArrayList<ItemData> list = new ArrayList<>();
        list.add(new ItemData("Select Language:",R.drawable.terra));
        list.add(new ItemData("Castellano", R.drawable.esp));
        list.add(new ItemData("Català", R.drawable.cat));
        list.add(new ItemData("English", R.drawable.eng));


        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_layout, R.id.txt, list);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(this);

        etUsername = (EditText)findViewById(R.id.username);
        etPassword = (EditText)findViewById(R.id.password);

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                username = etUsername.getText().toString();
                password = etPassword.getText().toString();


                JSONObject values=new JSONObject();
                try {
                    values.put("username",username);
                    values.put("password",password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new PostAsyncTask("http://sandshrew.fib.upc.es:3000/api/login",LoginActivity.this){
                    @Override
                    protected void onPostExecute(JSONObject resObject) {
                        Boolean res = false;
                        String error = "Usuario o password incorrectos";

                        try {
                            if(resObject.has("success")) res = resObject.getBoolean("success");
                            if(!res && resObject.has("errorMessage") ) error = resObject.getString("errorMessage");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("asdBool", res.toString());

                        if (res) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        else {
                            Log.i("asd", "gfgffgfgf");
                            Toast.makeText(getApplicationContext(),error, Toast.LENGTH_LONG).show();
                        }

                    }
                }.execute(values);

            }
        });


        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Intent refresh = new Intent(LoginActivity.this, LoginActivity.class);

        switch(position) {
            case 0:
                break;
            case 1:
                locale = new Locale("es");
                config.locale = locale;
                getResources().updateConfiguration(config, null);
                startActivity(refresh);
                //Toast.makeText(this, "You Selected Castellano", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                locale = new Locale("ca");
                config.locale = locale;
                getResources().updateConfiguration(config, null);
                startActivity(refresh);
                //Toast.makeText(this, "You Selected Català", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                locale = new Locale("en");
                config.locale = locale;
                getResources().updateConfiguration(config, null);
                startActivity(refresh);
                //Toast.makeText(this, "You Selected English", Toast.LENGTH_SHORT).show();
                break;
        }

        //Resources res = this.getResources();
        // Change locale settings in the app.
        //DisplayMetrics dm = res.getDisplayMetrics();
        //res.updateConfiguration(config, dm);

        getResources().updateConfiguration(config,null);

        //getResources().updateConfiguration(config, null);
        //Intent refresh = new Intent(LoginActivity.this, LoginActivity.class);
        //startActivity(refresh);
        //finish();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


/*



        //obtiene los idiomas del array de string.xml
        String[] types = getResources().getStringArray(R.array.languages);
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch(which){
                    case 0:
                        locale = new Locale("en");
                        config.locale =locale;
                        break;
                    case 1:
                        locale = new Locale("es");
                        config.locale =locale;
                        break;
                    case 2:
                        locale = new Locale("ca");
                        config.locale =locale;
                        break;
                }
                getResources().updateConfiguration(config, null);
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();

* */

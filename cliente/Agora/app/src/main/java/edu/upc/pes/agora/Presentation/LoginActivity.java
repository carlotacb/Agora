package edu.upc.pes.agora.Presentation;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
<<<<<<< HEAD:cliente/Agora/app/src/main/java/edu/upc/pes/agora/LoginActivity.java
import android.provider.Settings;
import android.support.annotation.VisibleForTesting;
=======
>>>>>>> b44d47790c83c2ea1f5218b9eb9be9625457aff3:cliente/Agora/app/src/main/java/edu/upc/pes/agora/Presentation/LoginActivity.java
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
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

import edu.upc.pes.agora.Logic.ItemData;
import edu.upc.pes.agora.Logic.PostAsyncTask;
import edu.upc.pes.agora.R;
import edu.upc.pes.agora.Logic.SpinnerAdapter;


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

        final Resources res = this.getResources();

        String sel = res.getString(R.string.tria_idioma);
        String cast = res.getString(R.string.Castella);
        String cata = res.getString(R.string.Catalan);
        String engl = res.getString(R.string.Ingles);

        ArrayList<ItemData> list = new ArrayList<>();
        list.add(new ItemData(sel, R.drawable.terra));
        list.add(new ItemData(cast, R.drawable.esp));
        list.add(new ItemData(cata, R.drawable.cat));
        list.add(new ItemData(engl, R.drawable.eng));


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

                if (username.length() == 0 || password.length() == 0) {
                    String error2 = res.getString(R.string.error2);
                    Toast.makeText(getApplicationContext(), error2, Toast.LENGTH_LONG).show();
                }

                else {
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
                            Boolean result = false;
                            String error = res.getString(R.string.error);

                            try {
                                if(resObject.has("success")) result = resObject.getBoolean("success");
                                if(!result && resObject.has("errorMessage") ) error = res.getString(R.string.error);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("asdBool", result.toString());

                            if (result) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            else {
                                Log.i("asd", "gfgffgfgf");
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                                etUsername.setText("");
                                etPassword.setText("");
                            }

                        }
                    }.execute(values);
                }
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
        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        switch(position) {
            case 0:
                break;
            case 1:
                locale = new Locale("es");
                config.locale = locale;
                getResources().updateConfiguration(config, null);
                startActivity(refresh);
                finish();
                break;
            case 2:
                locale = new Locale("ca");
                config.locale = locale;
                getResources().updateConfiguration(config, null);
                startActivity(refresh);
                finish();
                break;
            case 3:
                locale = new Locale("en");
                config.locale = locale;
                getResources().updateConfiguration(config, null);
                startActivity(refresh);
                finish();
                break;
        }


        getResources().updateConfiguration(config,null);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        finish();
        //Intent refresh = new Intent(LoginActivity.this, LoginActivity.class);
        //startActivity(refresh);
    }

    /**
     * This is a method that will be overridden in order to test response.
     */
    @VisibleForTesting
    public void handleLoginResponse(LoginResponse loginResponse) {
        // handle login response here
    }
}
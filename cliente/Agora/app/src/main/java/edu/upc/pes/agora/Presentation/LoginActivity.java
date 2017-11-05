package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import java.util.Objects;

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

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.btnLogin);
        register = (TextView) findViewById(R.id.btnRegister);

        spin = (Spinner) findViewById(R.id.spinner);

        //Get SharedPreferences containing token
        prefs = this.getSharedPreferences("SavedToken",MODE_PRIVATE);
        edit = prefs.edit();

        final Resources res = this.getResources();

        String sel = res.getString(R.string.tria_idioma);
        String cast = res.getString(R.string.Castella);
        String cata = res.getString(R.string.Catalan);
        String engl = res.getString(R.string.Ingles);

        ArrayList<ItemData> list = new ArrayList<>();
        list.add(new ItemData(sel, R.drawable.terra));
        list.add(new ItemData(cast, R.drawable.spa));
        list.add(new ItemData(cata, R.drawable.rep));
        list.add(new ItemData(engl, R.drawable.ing));


        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_layout, R.id.txt, list);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(this);

        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);

        login.setOnClickListener(new OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (username.length() == 0 || password.length() == 0) {
                    String error2 = res.getString(R.string.error2);
                    Toast.makeText(getApplicationContext(), error2, Toast.LENGTH_LONG).show();
                } else {
                    JSONObject values = new JSONObject();
                    try {
                        values.put("username", username);
                        values.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new PostAsyncTask("https://agora-pes.herokuapp.com/api/login", LoginActivity.this) {
                        @Override
                        protected void onPostExecute(JSONObject resObject) {

                            Boolean result = false;
                            String error = res.getString(R.string.error);

                            try {
                                if (resObject.has("success")) {
                                    result = resObject.getBoolean("success");

                                    //Saves token in SharedPreferences if it is not yet saved there
                                    if (resObject.has("token")) {
                                        String t = resObject.getString("token");
                                        if(!Objects.equals(prefs.getString("token", ""), t)) {
                                            edit.putString("token", t);
                                            edit.apply();
                                        }
                                        Log.i("SavedToken", prefs.getString("token","none saved"));
                                    }
                                }
                                if (!result && resObject.has("errorMessage"))
                                    error = res.getString(R.string.error);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("asdBool", result.toString());

                            if (result) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
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

        switch (position) {
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


        getResources().updateConfiguration(config, null);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import edu.upc.pes.agora.Logic.Constants;
import edu.upc.pes.agora.Logic.DeleteAsyncTask;
import edu.upc.pes.agora.Logic.ItemData;
import edu.upc.pes.agora.Logic.PostSesionAsyncTask;
import edu.upc.pes.agora.Logic.RecyclerAdapter;
import edu.upc.pes.agora.R;
import edu.upc.pes.agora.Logic.SpinnerAdapter;

import static edu.upc.pes.agora.Logic.Constants.SH_PREF_NAME;


public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button login;
    private TextView register;
    private ProgressBar prog;
    private EditText etUsername, etPassword;
    private String username, password;
    private Configuration config = new Configuration();
    private Locale locale;
    private ImageView canviidioma;
    private TextInputLayout errorusername, errorpassword;

    private String[] data = {"Castellano", "Català", "English"};

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.btnLogin);
        register = (TextView) findViewById(R.id.btnRegister);
        canviidioma = (ImageView) findViewById(R.id.multiidioma);

        if (Constants.Idioma.equals("ca")) {
            canviidioma.setImageResource(R.drawable.rep);
        }

        else if (Constants.Idioma.equals("es")) {
            canviidioma.setImageResource(R.drawable.spa);
        }

        else if (Constants.Idioma.equals("en")) {
            canviidioma.setImageResource(R.drawable.ing);
        }

        //Get SharedPreferences containing token
        prefs = this.getSharedPreferences(SH_PREF_NAME,MODE_PRIVATE);
        edit = prefs.edit();

        final Resources res = this.getResources();

        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);

        prog = (ProgressBar) findViewById(R.id.loginprogressbar);
        errorusername = (TextInputLayout) findViewById(R.id.username_up);
        errorpassword = (TextInputLayout) findViewById(R.id.password_up);

        login.setOnClickListener(new OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (username.length() == 0) {
                    errorusername.setErrorEnabled(true);
                    errorusername.setError("Campo necesario");
                    errorpassword.setErrorEnabled(false);
                    String error2 = res.getString(R.string.error2);
                    Toast.makeText(getApplicationContext(), error2, Toast.LENGTH_LONG).show();
                }

                else if (password.length() == 0) {
                    errorpassword.setErrorEnabled(true);
                    errorpassword.setError("Campo necesario");
                    errorusername.setErrorEnabled(false);
                    String error2 = res.getString(R.string.error2);
                    Toast.makeText(getApplicationContext(), error2, Toast.LENGTH_LONG).show();
                }

                else {
                    errorusername.setErrorEnabled(false);
                    errorpassword.setErrorEnabled(false);
                    login.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);

                    JSONObject values = new JSONObject();

                    try {
                        values.put("username", username);
                        values.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new PostSesionAsyncTask("https://agora-pes.herokuapp.com/api/login", LoginActivity.this) {
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
                                Constants.Username = username;
                            } else {
                                Log.i("asd", "gfgffgfgf");
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                                etUsername.setText("");
                                etPassword.setText("");
                                login.setVisibility(View.VISIBLE);
                                prog.setVisibility(View.GONE);
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

        canviidioma.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent refresh = new Intent(LoginActivity.this, LoginActivity.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PopupMenu popupMenu = new PopupMenu(v.getRootView().getContext(), canviidioma);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.men_castella:
                                Constants.Idioma = "es";
                                break;

                            case R.id.men_catala:
                                Constants.Idioma = "ca";
                                break;

                            case R.id.men_angles:
                                Constants.Idioma = "en";
                                break;
                        }

                        locale = new Locale(Constants.Idioma);
                        config.locale = locale;
                        getResources().updateConfiguration(config, null);
                        startActivity(refresh);
                        finish();

                        return false;
                    }
                });
                popupMenu.inflate(R.menu.idioma);
                popupMenu.show();

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
                startActivity(refresh)
                ;                finish();
                break;
            case 2:
                locale = new Locale("ca");
                config.locale = locale;
                getResources().updateConfiguration(config, null);
                startActivity(refresh);
                finish();
                break;
            case 3:
                locale = new Locale(Constants.Idioma);
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
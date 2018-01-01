package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import edu.upc.pes.agora.Logic.Listeners.BackOnClickListener;
import edu.upc.pes.agora.Logic.Listeners.LanguageOnClickListener;
import edu.upc.pes.agora.Logic.ServerConection.PostSesionAsyncTask;
import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.Logic.Utils.Helpers;
import edu.upc.pes.agora.R;

import static edu.upc.pes.agora.Logic.Utils.Constants.SH_PREF_NAME;

public class RegisterActivity extends AppCompatActivity {

    private EditText identifier, username, password1, password2;
    private String user, id, pw1, pw2;
    private Button registro;
    private TextInputLayout codiup, userup, pas1up, pas2up;
    private ProgressBar progbar;

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.i("asdCreate", "creando usuario");

        ImageView canviidioma = (ImageView) findViewById(R.id.multiidiomareg);
        ImageView enrerre = (ImageView) findViewById(R.id.backbutton);

        identifier = (EditText) findViewById(R.id.identifier);
        username = (EditText) findViewById(R.id.username);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);

        codiup = (TextInputLayout) findViewById(R.id.codiregistro_up);
        userup = (TextInputLayout) findViewById(R.id.usernamereg_up);
        pas1up = (TextInputLayout) findViewById(R.id.passwordreg_up);
        pas2up = (TextInputLayout) findViewById(R.id.password2reg_up);

        registro = (Button) findViewById(R.id.btnRegistration);
        progbar = (ProgressBar) findViewById(R.id.registerprogressbar);

        //Get SharedPreferences containing token
        prefs = this.getSharedPreferences(SH_PREF_NAME, MODE_PRIVATE);
        edit = prefs.edit();

        final Resources res = getResources();

        Helpers.changeFlag(canviidioma);

        Intent idioma = new Intent(RegisterActivity.this, RegisterActivity.class);
        Intent back = new Intent(RegisterActivity.this, LoginActivity.class);
        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        canviidioma.setOnClickListener(new LanguageOnClickListener(idioma, canviidioma, res, getApplicationContext()));

        enrerre.setOnClickListener(new BackOnClickListener(back, getApplicationContext()));

        username.getBackground().clearColorFilter();
        identifier.getBackground().clearColorFilter();
        password1.getBackground().clearColorFilter();
        password2.getBackground().clearColorFilter();

        registro.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                Log.i("asdCreate", "entra al boton registro");
                id = identifier.getText().toString();
                user = username.getText().toString();
                pw1 = password1.getText().toString();
                pw2 = password2.getText().toString();

                String camponecesario = res.getString(R.string.fieldnecesary);

                if (id.length() == 0) {
                    codiup.setErrorEnabled(true);
                    codiup.setError(camponecesario);
                    identifier.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                    if (user.length() != 0) {
                        userup.setErrorEnabled(false);
                        username.getBackground().clearColorFilter();
                    }
                    if (pw1.length() != 0) {
                        pas1up.setErrorEnabled(false);
                        password1.getBackground().clearColorFilter();
                        password1.setText("");
                    }
                    if (pw2.length() != 0) {
                        pas2up.setErrorEnabled(false);
                        password2.getBackground().clearColorFilter();
                        password2.setText("");
                    }
                }

                if (user.length() == 0) {
                    userup.setErrorEnabled(true);
                    userup.setError(camponecesario);
                    username.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                    if (id.length() != 0) {
                        codiup.setErrorEnabled(false);
                        identifier.getBackground().clearColorFilter();
                    }
                    if (pw1.length() != 0) {
                        pas1up.setErrorEnabled(false);
                        password1.getBackground().clearColorFilter();
                        password1.setText("");
                    }
                    if (pw2.length() != 0) {
                        pas2up.setErrorEnabled(false);
                        password2.getBackground().clearColorFilter();
                        password2.setText("");
                    }
                }

                if (pw1.length() == 0) {
                    pas1up.setErrorEnabled(true);
                    pas1up.setError(camponecesario);
                    password1.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                    if (id.length() != 0) {
                        codiup.setErrorEnabled(false);
                        identifier.getBackground().clearColorFilter();
                    }
                    if (user.length() != 0) {
                        userup.setErrorEnabled(false);
                        username.getBackground().clearColorFilter();
                    }
                    if (pw2.length() != 0) {
                        pas2up.setErrorEnabled(false);
                        password2.getBackground().clearColorFilter();
                        password2.setText("");
                    }
                }

                if (pw2.length() == 0) {
                    pas2up.setErrorEnabled(true);
                    pas2up.setError(camponecesario);
                    password2.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                    if (id.length() != 0) {
                        codiup.setErrorEnabled(false);
                        identifier.getBackground().clearColorFilter();
                    }
                    if (user.length() != 0) {
                        userup.setErrorEnabled(false);
                        username.getBackground().clearColorFilter();
                    }
                    if (pw1.length() != 0) {
                        pas1up.setErrorEnabled(false);
                        password1.getBackground().clearColorFilter();
                        password1.setText("");
                    }
                }

                else {
                    if(pw1.equals(pw2)){

                        registro.setVisibility(View.GONE);
                        progbar.setVisibility(View.VISIBLE);

                        JSONObject data = new JSONObject();

                        try {
                            data.put("signupCode", id);
                            data.put("username", user);
                            data.put("password", pw1);
                            data.put("confirmPassword", pw2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new PostSesionAsyncTask("https://agora-pes.herokuapp.com/api/signup",RegisterActivity.this){
                            @Override
                            protected void onPostExecute(JSONObject resObject) {
                                Boolean result = false;

                                try {
                                    if(resObject.has("success")) {
                                        result = resObject.getBoolean("success");

                                    }
                                    if(!result && resObject.has("errorMessage") ) {
                                        String error = res.getString(R.string.errorreg);
                                        pas1up.setErrorEnabled(false);
                                        pas2up.setErrorEnabled(false);
                                        codiup.setErrorEnabled(true);
                                        codiup.setError(error);
                                        userup.setErrorEnabled(true);
                                        userup.setError(error);
                                        identifier.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                                        username.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                                        password1.getBackground().clearColorFilter();
                                        password2.getBackground().clearColorFilter();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("asdBool", result.toString());

                                //String registreok = String.format(res.getString(R.string.Registrat), user);

                                if (result){
                                    //access app
                                    //Toast.makeText(RegisterActivity.this, registreok, Toast.LENGTH_SHORT).show();

                                    Constants.Username = user;
                                    try {
                                        if (resObject.has("token")) {
                                            String t = resObject.getString("token");
                                            Constants.SH_PREF_NAME = t;

                                            if (!Objects.equals(prefs.getString("token", ""), t)) {
                                                edit.putString("token", t);
                                                edit.apply();
                                            }

                                            Log.i("SavedToken", prefs.getString("token", "none saved"));
                                        }
                                        if (resObject.has("zone")) {
                                            Constants.zone = resObject.getInt("zone");
                                            Log.i("Zone:", "" + resObject.getInt("zone"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Intent fillProfile = new Intent(RegisterActivity.this, FillProfileActivity.class);
                                    Log.i("Asd:", "launch intent");
                                    startActivity(fillProfile);

                                } else {
                                    identifier.setText("");
                                    username.setText("");
                                    password1.setText("");
                                    password2.setText("");
                                    registro.setVisibility(View.VISIBLE);
                                    progbar.setVisibility(View.GONE);
                                }

                            }
                        }.execute(data);

                    } else {
                        String passwordsIguales = res.getString(R.string.passwodsiguals);
                        pas1up.setErrorEnabled(true);
                        pas2up.setErrorEnabled(true);
                        userup.setErrorEnabled(false);
                        codiup.setErrorEnabled(false);
                        password1.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                        password2.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                        pas1up.setError(passwordsIguales);
                        pas2up.setError(passwordsIguales);
                        username.getBackground().clearColorFilter();
                        identifier.getBackground().clearColorFilter();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent backb = new Intent(this, LoginActivity.class);
        backb.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(backb);
        finish();
    }
}

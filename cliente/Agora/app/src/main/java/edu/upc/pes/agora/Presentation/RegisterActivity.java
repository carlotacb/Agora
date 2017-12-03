package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import edu.upc.pes.agora.Logic.Constants;
import edu.upc.pes.agora.Logic.ItemData;
import edu.upc.pes.agora.Logic.PostSesionAsyncTask;
import edu.upc.pes.agora.R;
import edu.upc.pes.agora.Logic.SpinnerAdapter;

public class RegisterActivity extends AppCompatActivity {

    private EditText identifier, username, password1, password2;
    private String user, id, pw1, pw2;
    private ImageView canviaridioma;
    private ImageView enrerre;
    private Button registro;
    private Configuration config = new Configuration();
    private Locale locale;
    private TextInputLayout codiup, userup, pas1up, pas2up;
    private ProgressBar progbar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.i("asdCreate", "creando usuario");

        identifier = (EditText) findViewById(R.id.identifier);
        username = (EditText) findViewById(R.id.username);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        canviaridioma = (ImageView) findViewById(R.id.multiidiomareg);
        enrerre = (ImageView) findViewById(R.id.backbutton);
        registro = (Button) findViewById(R.id.btnRegistration);
        codiup = (TextInputLayout) findViewById(R.id.codiregistro_up);
        userup = (TextInputLayout) findViewById(R.id.usernamereg_up);
        pas1up = (TextInputLayout) findViewById(R.id.passwordreg_up);
        pas2up = (TextInputLayout) findViewById(R.id.password2reg_up);
        progbar = (ProgressBar) findViewById(R.id.registerprogressbar);

        final Resources res = getResources();

        if (Constants.Idioma.equals("ca")) {
            canviaridioma.setImageResource(R.drawable.rep);
        }

        else if (Constants.Idioma.equals("es")) {
            canviaridioma.setImageResource(R.drawable.spa);
        }

        else if (Constants.Idioma.equals("en")) {
            canviaridioma.setImageResource(R.drawable.ing);
        }

        canviaridioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent refresh = new Intent(RegisterActivity.this, RegisterActivity.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PopupMenu popupMenu = new PopupMenu(v.getRootView().getContext(), canviaridioma);
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

        enrerre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log = new Intent(RegisterActivity.this, LoginActivity.class);
                log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(log);
            }
        });

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
                                String error = res.getString(R.string.errorreg);

                                try {
                                    if(resObject.has("success")) {
                                        result = resObject.getBoolean("success");

                                    }
                                    if(!result && resObject.has("errorMessage") ) {
                                        error = res.getString(R.string.errorreg);
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

                                String registreok = String.format(res.getString(R.string.Registrat), user);

                                if (result){
                                    //access app
                                    Toast.makeText(RegisterActivity.this, registreok, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

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
                        //String passerror = res.getString(R.string.samepass);
                        String passwordsIguales = res.getString(R.string.passwodsiguals);
                        //Toast.makeText(getApplicationContext(), passerror, Toast.LENGTH_SHORT).show();
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
        Intent log = new Intent(this, LoginActivity.class);
        log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(log);
        finish();
    }
}

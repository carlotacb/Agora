package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.Logic.Listeners.LanguageOnClickListener;
import edu.upc.pes.agora.Logic.ServerConection.PostSesionAsyncTask;
import edu.upc.pes.agora.Logic.Utils.Helpers;
import edu.upc.pes.agora.R;

import static edu.upc.pes.agora.Logic.Utils.Constants.SH_PREF_NAME;


public class LoginActivity extends AppCompatActivity /*implements AdapterView.OnItemSelectedListener*/ {

    private Button login;
    private ProgressBar prog;
    private EditText etUsername, etPassword;
    private String username, password;
    private TextInputLayout errorusername, errorpassword;

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);

        errorusername = (TextInputLayout) findViewById(R.id.username_up);
        errorpassword = (TextInputLayout) findViewById(R.id.password_up);

        ImageView canviidioma = (ImageView) findViewById(R.id.multiidioma);

        prog = (ProgressBar) findViewById(R.id.loginprogressbar);
        login = (Button) findViewById(R.id.btnLogin);
        TextView register = (TextView) findViewById(R.id.btnRegister);

        //Get SharedPreferences containing token
        prefs = this.getSharedPreferences(SH_PREF_NAME, MODE_PRIVATE);
        edit = prefs.edit();

        final Resources res = this.getResources();

        etPassword.getBackground().clearColorFilter();
        etUsername.getBackground().clearColorFilter();

        Helpers.changeFlag(canviidioma);

        Intent idioma = new Intent(LoginActivity.this, LoginActivity.class);
        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        canviidioma.setOnClickListener(new LanguageOnClickListener(idioma, canviidioma, res, getApplicationContext()));

        login.setOnClickListener(new OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                String camponecesario = res.getString(R.string.fieldnecesary);

                if (username.length() == 0) {
                    errorusername.setErrorEnabled(true);
                    errorusername.setError(camponecesario);
                    etUsername.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                    if (password.length() != 0) {
                        errorpassword.setErrorEnabled(false);
                        etPassword.getBackground().clearColorFilter();
                    }
                }

                if (password.length() == 0) {
                    errorpassword.setErrorEnabled(true);
                    errorpassword.setError(camponecesario);
                    etPassword.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                    if (username.length() != 0) {
                        errorusername.setErrorEnabled(false);
                        etUsername.getBackground().clearColorFilter();
                    }
                } else {
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
                                }
                                if (!result && resObject.has("errorMessage"))
                                    error = res.getString(R.string.error);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("asdBool", result.toString());

                            if (result) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Constants.Username = username.toLowerCase();
                            } else {
                                Log.i("asd", "gfgffgfgf");
                                etUsername.setText("");
                                etPassword.setText("");
                                login.setVisibility(View.VISIBLE);
                                prog.setVisibility(View.GONE);
                                errorpassword.setErrorEnabled(true);
                                errorpassword.setError(error);
                                etPassword.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                                errorusername.setErrorEnabled(true);
                                errorusername.setError(error);
                                etUsername.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
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
    public void onBackPressed() {
        // Close Aplication
        finish();
    }


}
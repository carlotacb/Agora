package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import edu.upc.pes.agora.Logic.ItemData;
import edu.upc.pes.agora.Logic.PostAsyncTask;
import edu.upc.pes.agora.R;
import edu.upc.pes.agora.Logic.SpinnerAdapter;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText identifier, username, password1, password2;
    private String user, id, pw1, pw2;
    private Spinner spin;
    private Configuration config = new Configuration();
    private Locale locale;
    private Boolean _registerdone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        identifier = (EditText) findViewById(R.id.identifier);
        username = (EditText) findViewById(R.id.username);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        spin = (Spinner)findViewById(R.id.spinner);

        final Resources res = this.getResources();

        String sel = res.getString(R.string.tria_idioma);
        String cast = res.getString(R.string.Castella);
        String cata = res.getString(R.string.Catalan);
        String engl = res.getString(R.string.Ingles);

        ArrayList<ItemData> list = new ArrayList<>();
        list.add(new ItemData(sel, R.drawable.planeta));
        list.add(new ItemData(cast, R.drawable.spa));
        list.add(new ItemData(cata, R.drawable.rep));
        list.add(new ItemData(engl, R.drawable.ing));

        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_layout, R.id.txt, list);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(this);

    }

    @SuppressLint("StaticFieldLeak")
    public void register(){

        id = identifier.getText().toString();
        user = username.getText().toString();
        pw1 = password1.getText().toString();
        pw2 = password2.getText().toString();

        final Resources res = this.getResources();

        if (id.length() == 0) {
            String errorid = res.getString(R.string.errorid);
            Toast.makeText(RegisterActivity.this, errorid, Toast.LENGTH_SHORT).show();
            password1.setText("");
            password2.setText("");
        }

        else if (user.length() == 0) {
            String erroruser = res.getString(R.string.erroruser);
            Toast.makeText(RegisterActivity.this, erroruser, Toast.LENGTH_SHORT).show();
            password1.setText("");
            password2.setText("");
        }

        else if (pw1.length() == 0 || pw2.length() == 0) {
            String errorpass = res.getString(R.string.errorpw);
            Toast.makeText(RegisterActivity.this, errorpass, Toast.LENGTH_SHORT).show();
            password1.setText("");
            password2.setText("");
        }

        else {
            if(pw1.equals(pw2)){

                JSONObject data = new JSONObject();
                try {
                    data.put("signupCode", id);
                    data.put("username", user);
                    data.put("password", pw1);
                    data.put("confirmPassword", pw2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new PostAsyncTask("https://agora-pes.herokuapp.com/api/signup",RegisterActivity.this){
                    @Override
                    protected void onPostExecute(JSONObject resObject) {
                        Boolean result = false;
                        String error = res.getString(R.string.errorreg);

                        try {
                            if(resObject.has("success")) {
                                result = resObject.getBoolean("success");

                            }
                            if(!result && resObject.has("errorMessage") ) error = res.getString(R.string.errorreg);

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
                            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                            identifier.setText("");
                            username.setText("");
                            password1.setText("");
                            password2.setText("");
                        }

                    }
                }.execute(data);

            } else {
                String passerror = res.getString(R.string.samepass);
                Toast.makeText(this.getApplicationContext(), passerror, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnRegistration:
                register();
                break;
            default:
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Intent refresh = new Intent(RegisterActivity.this, RegisterActivity.class);
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

                break;
            case 3:
                locale = new Locale("en");
                config.locale = locale;
                getResources().updateConfiguration(config, null);
                startActivity(refresh);
                break;
        }


        getResources().updateConfiguration(config,null);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Intent log = new Intent(this, LoginActivity.class);
        log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(log);
        finish();
    }
}

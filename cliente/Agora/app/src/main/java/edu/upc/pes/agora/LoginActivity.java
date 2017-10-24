package edu.upc.pes.agora;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button login;
    private Spinner spin;
    private TextView register;
    private EditText etUsername, etPassword;
    private String username, password;
    private String URI = "https://dragos.ngrok.io";

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
        list.add(new ItemData("Castellano", R.drawable.espicon));
        list.add(new ItemData("Català", R.drawable.caticon));
        list.add(new ItemData("English", R.drawable.engicon));


        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_layout, R.id.txt, list);
        spin.setAdapter(adapter);

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
                new GetAsyncTask("http://sandshrew.fib.upc.es:3000/api/login",LoginActivity.this){
                    @Override
                    protected void onPostExecute(Boolean res) {

                        Log.i("asdBool", res.toString());

                        if (res) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }

                        else {
                            Log.i("asd", "gfgffgfgf");
                            Toast.makeText(getApplicationContext(),"Usuari o password incorrectos", Toast.LENGTH_LONG).show();
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
        TextView myText = (TextView) view;
        Toast.makeText(this, "You Selected "+myText.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


/* SPINNER:

   - STEP 1: al arxiu XML crear el array de Strings
   - STEP 2: Posar el Spinner al XML corresponent
   - STEP 3: Crear Adaptador
*
*
* */

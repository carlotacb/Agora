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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class LoginActivity extends AppCompatActivity {

    private Button login, register;
    private EditText etUsername, etPassword;
    private String URI = "https://dragos.ngrok.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.btnLogin);
        register = (Button) findViewById(R.id.btnRegister);

        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);


       /* login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText() + "";
                String password = etPassword.getText() + "";

                Context con = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                // TODO: PONER EL MULTIIDIOMA
                CharSequence textusercas = "Porfavor introduce un usuario válido";
                CharSequence textpascas = "Contraseña incorrecta";

                if (username.length() == 0) {
                    Toast toast = Toast.makeText(con, textusercas, duration);
                    toast.show();
                }

                if(password.length() == 0) {
                    Toast toast = Toast.makeText(con, textpascas, duration);
                    toast.show();
                }
                
            }
        });*/

        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }

    /*public class Networking extends AsyncTask {

        public static final int NETWORK_STATE_REGISTER=1;
        @Override
        protected Object doInBackground(Object[] params) {

            getJson(URI, NETWORK_STATE_REGISTER);
            return null;
        }
    }

    private void getJson(String url, int state) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        List<NameValuePair> postParameters = new ArrayList<NameValuePais>();

        switch(state) {
            case Networking.NETWORK_STATE_REGISTER:

                postParameters.get(new BasicNameValuePair("username", etUsername));


                break;
            default:
                break;
        }

    }*/
}


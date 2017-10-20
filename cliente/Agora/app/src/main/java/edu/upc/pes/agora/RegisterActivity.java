package edu.upc.pes.agora;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    EditText identificador = (EditText) findViewById(R.id.identificador);
    EditText username = (EditText) findViewById(R.id.username);
    EditText password1 = (EditText) findViewById(R.id.password1);
    EditText password2 = (EditText) findViewById(R.id.password2);
    String user;
    String id;
    String pw1;
    String pw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClick(View v) throws IOException {
        Intent button = new Intent();
        switch (v.getId()){
            case R.id.btnRegistration:
                id = identificador.getText().toString();
                user = username.getText().toString();
                pw1 = password1.getText().toString();
                pw2 = password2.getText().toString();

                if(pw1.equals(pw2)){
                    if ((new HttpHelper()).verifyData(id, user, pw1, pw2)){
                        //access app
                        break;
                    }else{
                        Toast.makeText(this.getApplicationContext(),"ID not valid or username already taken.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this.getApplicationContext(),"Passwords must be the same.", Toast.LENGTH_SHORT).show();
                }
        }
        startActivity(button);
    }




}

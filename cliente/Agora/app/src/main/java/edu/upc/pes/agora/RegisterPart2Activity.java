package edu.upc.pes.agora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegisterPart2Activity extends AppCompatActivity {

    EditText username = (EditText) findViewById(R.id.username);
    String user;

    EditText password = (EditText) findViewById(R.id.password);
    EditText passwordNew1 = (EditText) findViewById(R.id.passwordNew1);
    EditText passwordNew2 = (EditText) findViewById(R.id.passwordNew2);
    String pw;
    String pwNew1;
    String pwNew2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_part_2);
    }

    public void onClick(View v) throws IOException {
        Intent button = new Intent();
        switch (v.getId()){
            case R.id.btnRegistration:
                user =  username.getText().toString();
                pw = password.getText().toString();
                pwNew1 = passwordNew1.getText().toString();
                pwNew2 = passwordNew2.getText().toString();
                //data correct?
                if(true){
                    //enter application
                    break;
                }else{
                    Toast.makeText(this.getApplicationContext(),"Username o contraseña no está correcto.", Toast.LENGTH_SHORT).show();
                }
        }
        startActivity(button);
    }


}

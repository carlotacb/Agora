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
                    if (verifyData(id, user, pw1, pw2)){
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


    /**
     * Verifies if entered data is correct.
     * @param i entered ID
     * @param u entered username
     * @param p1 entered password 1
     * @param p2 entered password 2
     * @return true if server verifies data successfully, false otherwise
     * @throws IOException
     */
    public boolean verifyData(String i, String u, String p1, String p2) throws IOException {

        URL server =  new URL("http://sandshrew.fib.upc.es:3000/api/signup");
        HttpURLConnection client = null;

        try{
            client = (HttpURLConnection) server.openConnection();
            client.setReadTimeout(15000);
            client.setConnectTimeout(15000);
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            client.setRequestProperty("Accept","application/json");
            client.setDoInput(true);
            client.setDoOutput(true);

            JSONObject data = new JSONObject();
            data.put("signupCode", i);
            data.put("username", u);
            data.put("password", p1);
            data.put("confirmPassword", p2);

            Log.i("JSON", data.toString());

            DataOutputStream os = new DataOutputStream(client.getOutputStream());
            os.writeBytes(data.toString());
            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(client.getResponseCode()));
            Log.i("MSG" , client.getResponseMessage());

            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while((line = br.readLine()) != null ) {
                responseOutput.append(line);
            }
            String result = responseOutput.toString();
            br.close();

            client.disconnect();

            return (result.toString().equals("200"));

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }


}

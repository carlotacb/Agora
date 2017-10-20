package edu.upc.pes.agora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    EditText username = (EditText) findViewById(R.id.username);
    String user;

    EditText password = (EditText) findViewById(R.id.password);
    String pw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClick(View v) throws IOException {
        Intent button = new Intent();
        switch (v.getId()){
            case R.id.btnRegistration:
                user =  username.getText().toString();
                pw = password.getText().toString();

                if(verifyData(user, pw)){
                    //enter application
                    button = new Intent(this, RegisterPart2Activity.class);
                    startActivity(button);
                }else{
                    Toast.makeText(this.getApplicationContext(),"Username o contraseña no está correcto.", Toast.LENGTH_SHORT).show();
                }

            case R.id.btnAlLogin:
                button = new Intent(this, LoginActivity.class);
        }
    }

    /**
     * Verifies if entered login data is correct.
     * @param u entered username
     * @param p entered password
     * @return true if server verifies data successfully, false otherwise
     * @throws IOException
     */
    public boolean verifyData(String u, String p) throws IOException {

        URL server =  new URL("https://dragos.ngrok.io");
        HttpURLConnection client = null;

        try{
            client = (HttpURLConnection) server.openConnection();
            client.setReadTimeout(15000);
            client.setConnectTimeout(15000);
            client.setRequestMethod("POST");
            client.setDoOutput(true);
            DataOutputStream ds = new DataOutputStream(client.getOutputStream());
            ds.flush();
            ds.close();

            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while((line = br.readLine()) != null ) {
                responseOutput.append(line);
            }
            br.close();

            return (responseOutput.toString().equals("true"));

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
}

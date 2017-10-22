package edu.upc.pes.agora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    EditText identifier;
    EditText username;
    EditText password1;
    EditText password2;
    String user;
    String id;
    String pw1;
    String pw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        identifier = (EditText) findViewById(R.id.identifier);
        username = (EditText) findViewById(R.id.username);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);

    }

    public void onClick(View v) throws Exception {
        Intent button = new Intent();
        switch (v.getId()){
            case R.id.btnRegistration:
                id = identifier.getText().toString();
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
        startActivity(new Intent (this, RegisterActivity.class));
    }




}

package edu.upc.pes.agora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClick(View v){
        Intent button = new Intent();
        switch (v.getId()){
            case R.id.btnRegistration:
                button = new Intent(this, RegisterSocialMediaActivity.class);
            case R.id.btnAlLogin:
                button = new Intent(this, LoginActivity.class);
        }
        startActivity(button);
    }
}

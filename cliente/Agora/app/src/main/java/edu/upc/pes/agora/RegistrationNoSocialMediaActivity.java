package edu.upc.pes.agora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegistrationNoSocialMediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_no_social_media);
    }

    public void onClick(View v){
        Intent button = new Intent();
        switch (v.getId()){
            case R.id.btnRegistration:
                break;
        }
        startActivity(button);
    }
}

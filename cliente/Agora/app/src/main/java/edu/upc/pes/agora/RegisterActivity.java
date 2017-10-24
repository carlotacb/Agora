package edu.upc.pes.agora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

                    JSONObject data = new JSONObject();
                    data.put("signupCode", id);
                    data.put("username", user);
                    data.put("password", pw1);
                    data.put("confirmPassword", pw2);
                    new PostAsyncTask("http://sandshrew.fib.upc.es:3000/api/signup",RegisterActivity.this){
                        @Override
                        protected void onPostExecute(JSONObject resObject) {
                            Boolean res = false;
                            String error = "ID not valid or username already taken.";

                            try {
                                if(resObject.has("success")) res = resObject.getBoolean("success");
                                if(!res && resObject.has("errorMessage") ) error = resObject.getString("errorMessage");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("asdBool", res.toString());

                            if (res){
                                //access app
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                            }else{
                                Toast.makeText(RegisterActivity.this,error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }.execute(data);

                }else{
                    Toast.makeText(this.getApplicationContext(),"Passwords must be the same.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
        startActivity(new Intent (this, RegisterActivity.class));
    }
}

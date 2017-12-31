package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import edu.upc.pes.agora.Logic.Models.Profile;
import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.R;

public class OtherUserActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private Locale locale;

    private TextView user;
    private TextView nameProfile;
    private TextView codiPostal;
    private TextView barrio;
    private TextView born;
    private TextView sexo;
    private TextView descript;
    private TextView verpropuestas;

    private JSONObject values;

    private Profile p;
    private String nameJ;
    private String neighJ;
    private Integer cpJ;
    private String bornJ;
    private String sexJ;
    private String descriptJ;

    private String username;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        user = (TextView) findViewById(R.id.user);
        nameProfile = (TextView) findViewById(R.id.nameprofile);
        codiPostal = (TextView) findViewById(R.id.codipostal);
        barrio = (TextView) findViewById(R.id.barrio);
        born = (TextView) findViewById(R.id.born);
        sexo = (TextView) findViewById(R.id.sexo);
        descript = (TextView) findViewById(R.id.descript);
        verpropuestas = (TextView) findViewById(R.id.verpropuestas);
        verpropuestas.setClickable(true);
        p = new Profile();
        values = new JSONObject();

        if(getIntent().hasExtra("username")){
            username = getIntent().getStringExtra("username");
            user.setText(username);

            String profileURL = "https://agora-pes.herokuapp.com/api/user/"+username.toLowerCase();

            new GetTokenAsyncTask(profileURL, this){
                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    try {
                        if (jsonObject.has("error")) {
                            String error = jsonObject.get("error").toString();
                            Log.i("asdProfile", "Error");

                            Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        else {

                            Log.i("asdProfile", (jsonObject.toString()));

                            if(jsonObject.has("realname")) {
                                nameJ = jsonObject.getString("realname");
                                nameProfile.setText(nameJ);
                                p.setName(nameJ);
                            }
                            else {
                                user.setText("");
                            }

                            if(jsonObject.has("neighborhood")) {
                                neighJ = jsonObject.getString("neighborhood");
                                barrio.setText(neighJ);
                                p.setNeighborhood(neighJ);
                            }
                            else {
                                barrio.setText("");
                            }

                            if(jsonObject.has("cpCode")) {
                                cpJ = jsonObject.getInt("cpCode");
                                codiPostal.setText(cpJ.toString());
                                p.setCP(cpJ);
                            }
                            else {
                                codiPostal.setText("");
                            }

                            if(jsonObject.has("bdate")) {
                                bornJ = jsonObject.getString("bdate"); //TODO call showDate after merge
                                born.setText(bornJ);
                                p.setBorn(bornJ);
                            }
                            else {
                                born.setText("");
                            }

                            if(jsonObject.has("sex")) {
                                sexJ = jsonObject.getString("sex");
                                if (sexJ.equals("I")) sexo.setText(R.string.I);
                                else if (sexJ.equals("M")) sexo.setText(R.string.M);
                                else if (sexJ.equals("F")) sexo.setText(R.string.F);

                                p.setSex(sexJ);
                            }
                            else {
                                sexo.setText("");
                            }

                            if(jsonObject.has("description")){
                                descriptJ = jsonObject.getString("description");
                                descript.setText(descriptJ);
                                p.setDescription(descriptJ);
                            }
                            else{
                                descript.setText("");
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(values);
        }

        verpropuestas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), OtherUserProposalsActivity.class);
                i.putExtra("username", username);
                startActivity(i);
            }
        });
    }

}

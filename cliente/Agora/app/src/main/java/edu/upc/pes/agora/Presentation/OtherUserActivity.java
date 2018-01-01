package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upc.pes.agora.Logic.Listeners.BackOnClickListener;
import edu.upc.pes.agora.Logic.Listeners.LanguageOnClickListener;
import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.Utils.Helpers;
import edu.upc.pes.agora.R;

public class OtherUserActivity extends AppCompatActivity {

    private TextView nameProfile;
    private TextView codiPostal;
    private TextView barrio;
    private TextView born;
    private TextView sexo;

    private String nameJ;
    private String neighJ;
    private Integer cpJ;
    private String bornJ;
    private String sexJ;

    private String username;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        ImageView canviidioma = (ImageView) findViewById(R.id.multiidiomareg);
        ImageView enrerre = (ImageView) findViewById(R.id.backbutton);
        TextView user = (TextView) findViewById(R.id.user);
        nameProfile = (TextView) findViewById(R.id.nameprofile);
        codiPostal = (TextView) findViewById(R.id.codipostal);
        barrio = (TextView) findViewById(R.id.barrio);
        born = (TextView) findViewById(R.id.born);
        sexo = (TextView) findViewById(R.id.sexo);
        TextView verpropuestas = (TextView) findViewById(R.id.verpropuestas);
        verpropuestas.setClickable(true);
        JSONObject values = new JSONObject();

        final Resources res = getResources();

        Helpers.changeFlag(canviidioma);

        Intent idioma = new Intent(OtherUserActivity.this, OtherUserActivity.class);
        Intent back = new Intent(OtherUserActivity.this, MainActivity.class);
        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        canviidioma.setOnClickListener(new LanguageOnClickListener(idioma, canviidioma, res, getApplicationContext()));

        enrerre.setOnClickListener(new BackOnClickListener(back, getApplicationContext()));


        if(getIntent().hasExtra("username")){
            username = getIntent().getStringExtra("username");
            user.setText(username);

            String profileURL = "https://agora-pes.herokuapp.com/api/user/" + username.toLowerCase();

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
                            }
                            else {
                                nameProfile.setText("");
                            }

                            if(jsonObject.has("neighborhood")) {
                                neighJ = jsonObject.getString("neighborhood");
                                barrio.setText(neighJ);
                            }

                            if(jsonObject.has("cpCode")) {
                                cpJ = jsonObject.getInt("cpCode");
                                codiPostal.setText(String.valueOf(cpJ));
                            }
                            else {
                                codiPostal.setText("");
                            }

                            if(jsonObject.has("bdate")) {
                                bornJ = jsonObject.getString("bdate");

                                String databona = Helpers.showDate(bornJ);

                                born.setText(databona);
                            }
                            else {
                                born.setText("");
                            }

                            if(jsonObject.has("sex")) {
                                sexJ = jsonObject.getString("sex");
                                switch (sexJ) {
                                    case "I":
                                        sexo.setText(R.string.I);
                                        break;
                                    case "M":
                                        sexo.setText(R.string.M);
                                        break;
                                    case "F":
                                        sexo.setText(R.string.F);
                                        break;
                                }
                            }
                            else {
                                sexo.setText("");
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

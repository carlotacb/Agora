package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import edu.upc.pes.agora.Logic.PostAsyncTask;
import edu.upc.pes.agora.Logic.PutAsyncTask;
import edu.upc.pes.agora.R;

import static edu.upc.pes.agora.Logic.Constants.SH_PREF_NAME;

public class EditProposalActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private Locale locale;

    EditText editTitle;
    EditText editDescription;
    Button saveButton;
    Button cancelButton;

    String newTitle;
    String newDescription;
    String token;

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_proposal);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        prefs = this.getSharedPreferences(SH_PREF_NAME, MODE_PRIVATE);
        edit = prefs.edit();

        Intent i = getIntent();

        if(i.hasExtra("Title")) {
            editTitle.setText(i.getStringExtra("Title"));
        }
        if(i.hasExtra("Description")) {
            editDescription.setText(i.getStringExtra("Description"));
        }

        final int id = i.getIntExtra("id",0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_edit_proposal);
        toolbar.setLogo(R.mipmap.ic_editw);
        setSupportActionBar(toolbar);

        final Resources res = this.getResources();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyProposalsActivity.class);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                if (editTitle.getText().toString().equals("") ){
                    String error = res.getString(R.string.errorTitulo);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
                else if (editDescription.getText().toString().equals("")){
                    String error = res.getString(R.string.errorDescripcion);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();

                }
                else {
                    JSONObject values = new JSONObject();
                    try {
                        newTitle = editTitle.getText().toString();
                        newDescription = editDescription.getText().toString();
                        if (prefs.contains("token")){
                            token = prefs.getString("token","");
                        }
                        values.put("title", newTitle);
                        values.put("content", newDescription);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String editUrl = "https://agora-pes.herokuapp.com/api/proposal/"+id;
                    Log.i("Link",editUrl);

                    // nou server : agora-pes.herokuapp.com/api/proposal
                    new PutAsyncTask(editUrl, EditProposalActivity.this){
                        protected void onPostExecute(JSONObject resObject) {
                            Boolean result = false;
                            String error = res.getString(R.string.errorEdit);

                            try {

                                if (resObject.has("success")) {
                                    result = resObject.getBoolean("success");
                                }

                                if (!result && resObject.has("errorMessage")) {
                                    error = resObject.getString("errorMessage");
                                    Log.i("asdCreacion", error);
                                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(values);

                    Intent myIntent = new Intent(getApplicationContext(),MyProposalsActivity.class);
                    startActivity(myIntent);

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent refresh = new Intent(this, EditProposalActivity.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //noinspection SimplifiableIfStatement
        if (id == R.id.men_castella) {
            locale = new Locale("es");
            config.locale = locale;
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();
        }

        else if (id == R.id.men_catala){
            locale = new Locale("ca");
            config.locale = locale;
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();

        }

        else if (id == R.id.men_angles){
            locale = new Locale("en");
            config.locale = locale;
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

}

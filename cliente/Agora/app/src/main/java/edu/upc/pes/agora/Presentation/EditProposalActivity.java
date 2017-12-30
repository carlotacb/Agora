package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import edu.upc.pes.agora.Logic.Adapters.RecyclerAdapter;
import edu.upc.pes.agora.Logic.ServerConection.DeleteAsyncTask;
import edu.upc.pes.agora.Logic.ServerConection.PutAsyncTask;
import edu.upc.pes.agora.R;

import static edu.upc.pes.agora.Logic.Utils.Constants.SH_PREF_NAME;

public class EditProposalActivity extends AppCompatActivity {

    EditText editTitle;
    EditText editDescription;
    Button editButton;
    Button deleteButton;
    Button saveButton;
    Button cancelButton;

    String newTitle;
    String newDescription;
    String token;

    private ImageView image;
    private String encoded;
    private ProgressBar prog;
    private final int SELECT_PICTURE=200;

    private Integer idprop;

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_proposal);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);
        editButton = (Button) findViewById(R.id.editButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        prog = (ProgressBar) findViewById(R.id.saveprogressbar);

        image = (ImageView) findViewById(R.id.setImage);

        prefs = this.getSharedPreferences(SH_PREF_NAME, MODE_PRIVATE);
        edit = prefs.edit();

        Intent i = getIntent();

        idprop = i.getIntExtra("id", 0);

        if(i.hasExtra("Title")) {
            editTitle.setText(i.getStringExtra("Title"));
        }
        if(i.hasExtra("Description")) {
            editDescription.setText(i.getStringExtra("Description"));
        }

        final int id = i.getIntExtra("id",0);

        final Resources res = this.getResources();

        /*editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Galería", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditProposalActivity.this);
                builder.setTitle("Escoge una opción");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selection) {
                        if(options[selection]=="Galería") {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                        }
                        else if(options[selection]=="Cancelar"){
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });*/

        /*deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + idprop, this) {
                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {
                        if (!jsonObject.has("error")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else {
                            //try {
                                JSONObject values = new JSONObject();
                                values.remove("images");
                            //} catch (JSONException e) {
                                //e.printStackTrace();
                            //}
                        }
                        super.onPostExecute(jsonObject);
                    }
                }.execute();
            }
        });*/

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

                    saveButton.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);

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

                    Intent myIntent = new Intent(getApplicationContext(), MyProposalsActivity.class);
                    startActivity(myIntent);

                }
            }
        });

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_PICTURE:
                if(resultCode == RESULT_OK) {
                    Bitmap bitmap =null;
                    if (data != null) {
                        try {
                            bitmap  = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    image.setImageBitmap(bitmap);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    //ArrayImages.put(encoded);
                }
                break;
        }
    }*/

}

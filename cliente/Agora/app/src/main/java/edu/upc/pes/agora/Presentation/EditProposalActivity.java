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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import edu.upc.pes.agora.Logic.Adapters.CommentAdapter;
import edu.upc.pes.agora.Logic.Adapters.ImatgesAdapter;
import edu.upc.pes.agora.Logic.Adapters.RecyclerAdapter;
import edu.upc.pes.agora.Logic.Models.Comment;
import edu.upc.pes.agora.Logic.Models.ImatgeItem;
import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.ServerConection.PostAsyncTask;
import edu.upc.pes.agora.Logic.ServerConection.PutAsyncTask;
import edu.upc.pes.agora.R;

import static edu.upc.pes.agora.Logic.Utils.Constants.SH_PREF_NAME;

public class EditProposalActivity extends AppCompatActivity {

    EditText editTitle;
    EditText editDescription;
    Button editButton;
    Button saveButton;
    Button cancelButton;
    Button editPosButton;

    String newTitle;
    String newDescription;
    String token;

    private ListView limatges;
    private String encoded;
    private ProgressBar prog;
    private final int SELECT_PICTURE=200;

    private JSONArray ArrayImages = new JSONArray();
    private ArrayList<ImatgeItem> mImatgeItems = new ArrayList<> ();

    private Integer idprop;
    private int numimatges = 0;
    private JSONObject Jason = new JSONObject();

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_proposal);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);
        editButton = (Button) findViewById(R.id.editButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        editPosButton = (Button) findViewById(R.id.editPosButton);
        prog = (ProgressBar) findViewById(R.id.saveprogressbar);

        limatges = (ListView) findViewById(R.id.llistaimatges);

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

        llistarimatges();

        editButton.setOnClickListener(new View.OnClickListener(){
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
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyProposalsActivity.class);
                startActivity(intent);
            }
        });

        editPosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddLocationActivity.class);
                i.putExtra("Title", editTitle.getText().toString());
                i.putExtra("Description", editDescription.getText().toString());
                i.putExtra("CallingActivity", "Edit");
                i.putExtra("id",id);
                if (getIntent().hasExtra("lat") && getIntent().getDoubleExtra("lat",0) != 0){
                    i.putExtra("lat", getIntent().getDoubleExtra("lat",0));
                    i.putExtra("lng", getIntent().getDoubleExtra("lng",0));
                }
                startActivityForResult(i,1);
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
                    JSONObject location = new JSONObject();

                    try {
                        newTitle = editTitle.getText().toString();
                        newDescription = editDescription.getText().toString();
                        if (prefs.contains("token")){
                            token = prefs.getString("token","");
                        }
                        values.put("id",id);
                        values.put("title", newTitle);
                        values.put("content", newDescription);
                        location.put("lat", getIntent().getDoubleExtra("lat",0));
                        location.put("long", getIntent().getDoubleExtra("lng",0));
                        values.put("location", location);

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
                                    if(resObject.getString("errorMessage").equals("Selected location outside of allowed zone.")){
                                        Toast.makeText(getApplicationContext(), res.getString(R.string.errorPosition), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //String creacionok = String.format(res.getString(R.string.done), strTitulo);

                            if (result) {

                                if (numimatges > 0) {
                                    afegirimatges();
                                }

                                else {
                                    //Toast.makeText(getApplicationContext(), creacionok, Toast.LENGTH_LONG).show();
                                    //startActivity(new Intent(CreateProposalActivity.this, MainActivity.class));
                                }
                            }

                            else {
                                Log.i("asdCreacion", "reset");
                                //Create.setVisibility(View.VISIBLE);
                                //prog.setVisibility(View.GONE);
                            }

                        }
                    }.execute(values);

                    Intent myIntent = new Intent(getApplicationContext(), MyProposalsActivity.class);
                    startActivity(myIntent);

                }
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private void afegirimatges(){

        final Resources res = this.getResources();

        JSONObject imatgesperpropostes = new JSONObject();

        try {
            imatgesperpropostes.put("images", ArrayImages);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new PostAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + idprop + "/image", EditProposalActivity.this) {
            @Override
            protected void onPostExecute(JSONObject resObject) {
                Boolean result = false;

                Log.i("asdimatge", "entra aqui");

                try {
                    if (resObject.has("success")) {
                        result = resObject.getBoolean("success");
                    }

                    if (!result && resObject.has("errorMessage")) {
                        String error = res.getString(R.string.errorCreacion);
                        Log.i("asdCreacion", error);
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*String creacionok = String.format(res.getString(R.string.done), strTitulo);

                if (result) {
                    Toast.makeText(getApplicationContext(), creacionok, Toast.LENGTH_LONG).show();

                    startActivity(new Intent(EditProposalActivity.this, MainActivity.class));
                }

                else {
                    Create.setVisibility(View.VISIBLE);
                    prog.setVisibility(View.GONE);
                }*/
            }
        }.execute(imatgesperpropostes);

    }

    @Override
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

                    ImatgeItem i = new ImatgeItem();

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    ArrayImages.put(encoded);

                    ++numimatges;
                    i.setImatge(encoded);
                    i.setNumero(mImatgeItems.size()+1);

                    afegirimatgellista(i);
                }
                break;
        }
    }

    private void afegirimatgellista(ImatgeItem im) {

        mImatgeItems.add(im);

        limatges.setAdapter(new ImatgesAdapter(getApplicationContext(), mImatgeItems));
    }

    @SuppressLint("StaticFieldLeak")
    private void llistarimatges() {
        new GetTokenAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + idprop, this) {

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    if (jsonObject.has("error")) {
                        String error = jsonObject.get("error").toString();
                        Log.i("asd123", "Error");

                        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    else if (jsonObject != null) {

                        JSONArray ArrayImages = jsonObject.getJSONArray("images");
                        ArrayList<ImatgeItem> imatges = new ArrayList<>();

                        if (ArrayImages != null) {
                            for (int i=0; i < ArrayImages.length(); i++){

                                Log.i("asd123", (ArrayImages.get(i).toString()));

                                JSONObject jas = ArrayImages.getJSONObject(i);
                                String id = jas.getString("id");
                                String contentimage = jas.getString("image");

                                ImatgeItem aux = new ImatgeItem();
                                aux.setNumero(Integer.parseInt(id));
                                aux.setImatge(contentimage);

                                mImatgeItems.add(aux);
                            }
                        }
                        limatges.setAdapter(new ImatgesAdapter(getApplicationContext(), mImatgeItems));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(Jason);
    }

}

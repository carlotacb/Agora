package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import edu.upc.pes.agora.Logic.Comment;
import edu.upc.pes.agora.Logic.CommentsAdapter;
import edu.upc.pes.agora.Logic.Constants;
import edu.upc.pes.agora.Logic.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.PostAsyncTask;
import edu.upc.pes.agora.Logic.ProposalAdapter;
import edu.upc.pes.agora.Logic.Proposals;
import edu.upc.pes.agora.Logic.PutAsyncTask;
import edu.upc.pes.agora.R;

public class DetailsProposalActivity extends AppCompatActivity {

    private TextView titol;
    private TextView descripcio;
    private TextView owner;

    private ListView llista_comentaris;
    private String newComent;
    private ImageView canviidioma, enrerre;

    String mtit, mdesc, mowner;

    private Configuration config = new Configuration();
    private Locale locale;

    private FloatingActionButton addcoment;

    private JSONObject Jason = new JSONObject();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_proposal);

        titol = (TextView) findViewById(R.id.titolproposal);
        descripcio = (TextView) findViewById(R.id.descripcioproposta);
        llista_comentaris = (ListView) findViewById(R.id.listcommentaris);
        addcoment = (FloatingActionButton) findViewById(R.id.fabcoment);
        owner = (TextView) findViewById(R.id.ownerproposal);
        canviidioma = (ImageView) findViewById(R.id.multiidiomareg);
        enrerre = (ImageView) findViewById(R.id.backbutton);

        Intent i = getIntent();

        if (i.hasExtra("Title")) {
            titol.setText(i.getStringExtra("Title"));
            mtit = i.getStringExtra("Title");
        }
        if (i.hasExtra("Description")) {
            descripcio.setText(i.getStringExtra("Description"));
            mdesc = i.getStringExtra("Description");
        }
        if (i.hasExtra("Owner")) {
            owner.setText(i.getStringExtra("Owner"));
            mowner = i.getStringExtra("Owner");
        }

        final Integer idprop = i.getIntExtra("id", 0);

        final Resources res = this.getResources();

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

                        JSONArray ArrayComments = jsonObject.getJSONArray("comments");
                        ArrayList<Comment> comentarios = new ArrayList<>();

                        if (ArrayComments != null) {
                            for (int i=0; i < ArrayComments.length(); i++){

                                Log.i("asd123", (ArrayComments.get(i).toString()));

                                JSONObject jas = ArrayComments.getJSONObject(i);
                                String id = jas.getString("id");
                                String contentcoment = jas.getString("comment");

                                JSONObject Usuario = jas.getJSONObject("author");
                                Log.i("asd123", (Usuario.toString()));
                                String owner = Usuario.getString("username");

                                Comment aux = new Comment(owner, id, contentcoment);
                                aux.setIdentificadorProp(idprop);

                                comentarios.add(aux);
                            }
                        }
                        llista_comentaris.setAdapter(new CommentsAdapter(getApplicationContext(), comentarios));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(Jason);


        addcoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogoaddcoment = new AlertDialog.Builder(v.getRootView().getContext());

                dialogoaddcoment.setTitle("Nou Comentari");
                dialogoaddcoment.setCancelable(false);
                dialogoaddcoment.setCancelable(false);

                final EditText input = new EditText(v.getRootView().getContext());
                input.setSingleLine();
                FrameLayout container = new FrameLayout(v.getRootView().getContext());
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = v.getRootView().getContext().getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                input.setLayoutParams(params);

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogoaddcoment.setView(input);

                dialogoaddcoment.setPositiveButton("Afegir", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        newComent = input.getText().toString();

                        JSONObject values = new JSONObject();
                        try {
                            values.put("comment", newComent);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new PostAsyncTask("https://agora-pes.herokuapp.com/api/proposal/" + idprop + "/comment", DetailsProposalActivity.this) {
                            @Override
                            protected void onPostExecute(JSONObject resObject) {
                                Boolean result = false;
                                String error = res.getString(R.string.errorCreacion);

                                try {
                                    if (resObject.has("success")) {
                                        result = resObject.getBoolean("success");
                                    }

                                    if (!result && resObject.has("errorMessage")) {
                                        Log.i("asdCreacion", error);
                                        Toast.makeText(getApplicationContext(), error , Toast.LENGTH_LONG).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (result) {
                                    Toast.makeText(getApplicationContext(), "Comentari creat", Toast.LENGTH_LONG).show();

                                    Intent myIntent = new Intent(getApplicationContext(), DetailsProposalActivity.class);
                                    myIntent.putExtra("Title", mtit);
                                    myIntent.putExtra("Description", mdesc);
                                    myIntent.putExtra("id", idprop);
                                    startActivity(myIntent);
                                }

                                else {
                                    Log.i("asdCreacion", "reset");
                                    input.setText("");
                                }
                            }
                        }.execute(values);
                    }
                });

                dialogoaddcoment.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                final AlertDialog dialog = dialogoaddcoment.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {
                        if(input.getText().toString().equals("")) ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                });

                dialog.show();

                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (input.getText().toString().equals("")) {
                            // Disable ok button
                            ((AlertDialog) dialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            // Something into edit text. Enable the button.
                            ((AlertDialog) dialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                });

            }
        });

        /*canviidioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent refresh = new Intent(DetailsProposalActivity.this, DetailsProposalActivity.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PopupMenu popupMenu = new PopupMenu(v.getRootView().getContext(), canviidioma);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.men_castella:
                                Constants.Idioma = "es";
                                break;

                            case R.id.men_catala:
                                Constants.Idioma = "ca";
                                break;

                            case R.id.men_angles:
                                Constants.Idioma = "en";
                                break;
                        }

                        locale = new Locale(Constants.Idioma);
                        config.locale = locale;
                        getResources().updateConfiguration(config, null);
                        startActivity(refresh);
                        finish();

                        return false;
                    }
                });
                popupMenu.inflate(R.menu.idioma);
                popupMenu.show();

            }
        });

        enrerre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log = new Intent(DetailsProposalActivity.this, MainActivity.class);
                log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(log);
            }
        });*/

    }

    @Override
    public void onBackPressed() {
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
    }

}

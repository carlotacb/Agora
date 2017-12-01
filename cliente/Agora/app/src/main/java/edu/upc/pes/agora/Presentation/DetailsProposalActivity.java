package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import edu.upc.pes.agora.Logic.Comment;
import edu.upc.pes.agora.Logic.CommentsAdapter;
import edu.upc.pes.agora.Logic.Constants;
import edu.upc.pes.agora.Logic.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.PostAsyncTask;
import edu.upc.pes.agora.Logic.ProposalAdapter;
import edu.upc.pes.agora.Logic.Proposals;
import edu.upc.pes.agora.R;

public class DetailsProposalActivity extends AppCompatActivity {

    private TextView titol;
    private TextView descripcio;

    private ListView llista_comentaris;
    private TextView mComment;
    private ProgressBar pbar;
    private ImageView moreim;

    String mtit, mdesc;

    private Button crear;

    private String contentcoment;

    private JSONObject Jason = new JSONObject();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_proposal);

        titol = (TextView) findViewById(R.id.titolproposal);
        descripcio = (TextView) findViewById(R.id.descripcioproposta);

        Intent i = getIntent();

        if (i.hasExtra("Title")) {
            titol.setText(i.getStringExtra("Title"));
            mtit = i.getStringExtra("Title");
        }
        if (i.hasExtra("Description")) {
            descripcio.setText(i.getStringExtra("Description"));
            mdesc = i.getStringExtra("Description");
        }

        final Integer idprop = i.getIntExtra("id", 0);

        final Resources res = this.getResources();

        llista_comentaris = (ListView) findViewById(R.id.listcommentaris);

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


        crear = (Button) findViewById(R.id.crearcomentari);
        mComment = (TextView) findViewById(R.id.textComentario);
        pbar = (ProgressBar) findViewById(R.id.crcomentprogressbar);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mComment.getText().toString().equals("") ){
                    Toast.makeText(getApplicationContext(), "Comentario necesario", Toast.LENGTH_LONG).show();
                }

                else  {
                    crear.setVisibility(View.GONE);
                    pbar.setVisibility(View.VISIBLE);

                    JSONObject values = new JSONObject();
                    try {
                        contentcoment = mComment.getText().toString();
                        values.put("comment", contentcoment);
                    } catch (JSONException e) {
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
                                mComment.setText("");
                                crear.setVisibility(View.VISIBLE);
                                pbar.setVisibility(View.GONE);
                            }

                        }
                    }.execute(values);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
    }

}

package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.upc.pes.agora.Logic.Adapters.OtherProposalAdapter;
import edu.upc.pes.agora.Logic.Adapters.RecyclerAdapter;
import edu.upc.pes.agora.Logic.Listeners.BackOnClickListener;
import edu.upc.pes.agora.Logic.Listeners.LanguageOnClickListener;
import edu.upc.pes.agora.Logic.Models.Proposal;
import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.Logic.Utils.Helpers;
import edu.upc.pes.agora.R;

public class OtherUserProposalsActivity extends AppCompatActivity {

    private ListView propList;
    private JSONObject Jason = new JSONObject();
    private ArrayList<Proposal> propostes;

    private LinearLayout tot;
    private LinearLayout progres;
    private TextView titol;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_proposals);

        ImageView canviidioma = (ImageView) findViewById(R.id.multiidiomareg);
        ImageView enrerre = (ImageView) findViewById(R.id.backbutton);

        propList = (ListView) findViewById(R.id.propList);
        tot = (LinearLayout) findViewById(R.id.layouttot);
        progres = (LinearLayout) findViewById(R.id.pantallacargandoop);
        titol = (TextView) findViewById(R.id.propcreadaper);
        String user = getIntent().getStringExtra("username");

        final Resources res = getResources();

        Helpers.changeFlag(canviidioma);

        Intent idioma = new Intent(OtherUserProposalsActivity.this, OtherUserProposalsActivity.class);
        idioma.putExtra("username", user);
        Intent back = new Intent(OtherUserProposalsActivity.this, OtherUserActivity.class);
        back.putExtra("username", user);
        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        canviidioma.setOnClickListener(new LanguageOnClickListener(idioma, canviidioma, res, getApplicationContext()));
        enrerre.setOnClickListener(new BackOnClickListener(back, getApplicationContext()));

        titol.setText(String.format(res.getString(R.string.propcreadapor), user.toUpperCase()));

        new GetTokenAsyncTask("https://agora-pes.herokuapp.com/api/proposal?username=" + user, this) {

            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                tot.setVisibility(View.GONE);
                progres.setVisibility(View.VISIBLE);

                try {
                    if (jsonObject.has("error")) {
                        String error = jsonObject.get("error").toString();
                        Log.i("asd123", "Error");

                        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    else if (jsonObject != null){
                        JSONArray ArrayProp = jsonObject.getJSONArray("arrayResponse");
                        propostes = new ArrayList<>();

                        if (ArrayProp != null) {
                            for (int i=0; i < ArrayProp.length(); i++){

                                Log.i("asd123", (ArrayProp.get(i).toString()));

                                JSONObject jas = ArrayProp.getJSONObject(i);
                                JSONArray comentaris = jas.getJSONArray("comments");
                                String title = jas.getString("title");
                                String owner = jas.getString("owner");
                                String description = jas.getString("content");
                                Integer id = jas.getInt("id");
                                String ca = jas.getString("categoria");
                                String createDate = Helpers.showDate(jas.getString("createdDateTime"));
                                String updateDate = Helpers.showDate(jas.getString("updatedDateTime"));
                                Integer nvotes = jas.getInt("numberUpvotes");
                                Integer nunvotes = jas.getInt("numberDownvotes");
                                Integer vote = jas.getInt("userVoted");
                                Boolean fav = jas.getBoolean("favorited");
                                Integer numcoments = comentaris.length();
                                Proposal aux = new Proposal(id, title, description, owner, ca, createDate, updateDate);

                                aux.setNumerocomentarios(numcoments);
                                aux.setFavorite(fav);
                                aux.setNumerounvotes(nunvotes);
                                aux.setNumerovotes(nvotes);
                                aux.setVotacion(vote);

                                propostes.add(aux);
                            }
                        }
                        propList.setAdapter(new OtherProposalAdapter(propostes, getApplicationContext()));
                    }
                } catch (JSONException e ) {
                    e.printStackTrace();
                }

                tot.setVisibility(View.VISIBLE);
                progres.setVisibility(View.GONE);

            }
        }.execute(Jason);
    }
}

package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.upc.pes.agora.Logic.Listeners.BackOnClickListener;
import edu.upc.pes.agora.Logic.Listeners.LanguageOnClickListener;
import edu.upc.pes.agora.Logic.ServerConection.PostAsyncTask;
import edu.upc.pes.agora.Logic.ServerConection.PutAsyncTask;
import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.Logic.Utils.Helpers;
import edu.upc.pes.agora.R;

public class FillProfileActivity extends AppCompatActivity {

    private TextInputLayout nombre, cp, fechanacimiento;
    private EditText enombre, ecp, efechanacimiento, edescription;
    private TextView username, zona;
    private ImageView profileimage;
    private Spinner sexo;
    private Button okey;
    private ProgressBar progbar;
    private String barrio;

    private String encoded;

    private Integer selection = 0;

    private final int SELECT_PICTURE = 200;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String[] diferentesSexos; //{getString(R.string.M), getString(R.string.F), getString(R.string.I)};
    String[] diferentesSexosGenerico = {"X", "M", "F", "I"};

    String nom, pocode, birthdate, desc, seex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_profile);

        Log.i("Username", Constants.Username);
        Log.i("Extras", Integer.toString(Constants.zone));

        username = (TextView) findViewById(R.id.usernameprofile);
        username.setText(Constants.Username);
        zona = (TextView) findViewById(R.id.barrio);
        zona.setText(Helpers.getBarrio(Constants.zone));
        barrio = Helpers.getBarrio(Constants.zone);

        nombre = (TextInputLayout) findViewById(R.id.nombre_up);
        cp = (TextInputLayout) findViewById(R.id.codipostal_up);
        fechanacimiento = (TextInputLayout) findViewById(R.id.fechanaix_up);

        enombre = (EditText) findViewById(R.id.nombrecompleto);
        ecp = (EditText) findViewById(R.id.cpostal);
        efechanacimiento = (EditText) findViewById(R.id.fecha);
        edescription = (EditText) findViewById(R.id.descriptionc);

        efechanacimiento.getBackground().clearColorFilter();
        enombre.getBackground().clearColorFilter();
        ecp.getBackground().clearColorFilter();
        edescription.getBackground().clearColorFilter();

        sexo = (Spinner) findViewById(R.id.sexo);

        okey = (Button) findViewById(R.id.CompletedReg);
        progbar = (ProgressBar) findViewById(R.id.completeprog);

        profileimage = (ImageView) findViewById(R.id.profileimage);

        ImageView canviidioma = (ImageView) findViewById(R.id.multiidiomareg);

        final Resources res = getResources();

        switch (Constants.Idioma) {
            case "ca":
                canviidioma.setImageResource(R.drawable.rep);
                break;
            case "es":
                canviidioma.setImageResource(R.drawable.spa);
                break;
            case "en":
                canviidioma.setImageResource(R.drawable.ing);
                break;
        }

        Intent idioma = new Intent(FillProfileActivity.this, FillProfileActivity.class);
        nom = enombre.getText().toString();
        idioma.putExtra("nombrecompleto", enombre.getText().toString());
        pocode = ecp.getText().toString();
        idioma.putExtra("codipostal", pocode);
        birthdate = efechanacimiento.getText().toString();
        idioma.putExtra("cumple", birthdate);
        desc = edescription.getText().toString();
        idioma.putExtra("descripcio", desc);
        if (sexo.getSelectedItemPosition() == -1) sexo.setSelection(0);
        seex = diferentesSexosGenerico[sexo.getSelectedItemPosition()];
        idioma.putExtra("sexe", seex);
        idioma.putExtra("foto", encoded);
        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        canviidioma.setOnClickListener(new LanguageOnClickListener(idioma, canviidioma, res, getApplicationContext()));

        Intent i = getIntent();

        if (i.hasExtra("nombrecompleto")) {
            Log.i("23s", "af");
            Log.i("23s", i.getStringExtra("nombrecompleto"));
            enombre.setText(i.getStringExtra("nombrecompleto"));
        }
        else if (i.hasExtra("codipostal")) {
            ecp.setText(i.getStringExtra("codipostal"));
        }
        else if (i.hasExtra("cumple")) {
            efechanacimiento.setText(i.getStringExtra("cumple"));
        }
        else if (i.hasExtra("descripcio")) {
            edescription.setText(i.getStringExtra("descripcio"));
        }
        else if (i.hasExtra("sexe")) {
            String sexeConcret = i.getStringExtra("sexof");
            switch (sexeConcret) {
                case "I":
                    selection = 3;
                    break;
                case "F":
                    selection = 2;
                    break;
                case "M":
                    selection = 1;
                    break;
            }
        }
        else if (i.hasExtra("foto")) {

        }

        profileimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {res.getString(R.string.Galeria), res.getString(R.string.cancelButton)};
                final AlertDialog.Builder builder = new AlertDialog.Builder(FillProfileActivity.this);
                builder.setTitle(res.getString(R.string.escojeropcion));
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selection) {
                        if(options[selection] == res.getString(R.string.Galeria)) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, res.getString(R.string.appdeimagen)), SELECT_PICTURE);
                        }
                        else if(options[selection] == res.getString(R.string.cancelButton)){
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        final String[] sexos = new String[]{
                getString(R.string.spinnerhintsexo),
                getString(R.string.hombre),
                getString(R.string.mujer),
                getString(R.string.indefinido)};

        final List<String> sexosList = new ArrayList<>(Arrays.asList(sexos));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_categories_layout, sexosList){
            @Override
            public boolean isEnabled(int position){
                if(sexos[position].equals(getString(R.string.spinnerhintsexo))) {
                    // Disable the first item from Spinner. The first item will be use for hint
                    return false;
                }
                else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (sexos[position].equals(getString(R.string.spinnerhintsexo))){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_categories_layout);
        sexo.setAdapter(spinnerArrayAdapter);
        sexo.setSelection(selection);

        efechanacimiento.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        FillProfileActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                calendar.set(year-18,month,day);
                long value=calendar.getTimeInMillis();
                dialog.getDatePicker().setMaxDate(value);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                Log.d("EditProfileActivity", "onDateSet: dd/mm/yyyy: " + year + "/" + monthOfYear + "/" + dayOfMonth);
                String date = + year + "/" + monthOfYear + "/" + dayOfMonth;
                efechanacimiento.setText(date);
            }
        };

        okey.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                String nameS = enombre.getText().toString();
                String CPcodeS = ecp.getText().toString();
                String fechaS = efechanacimiento.getText().toString();
                String sexS = diferentesSexosGenerico[sexo.getSelectedItemPosition()];
                String descripcioS = edescription.getText().toString();
                String username = Constants.Username;

                String camponecesario = res.getString(R.string.fieldnecesary);

                if (nameS.length() == 0) {
                    nombre.setErrorEnabled(true);
                    nombre.setError(camponecesario);
                    enombre.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                    if (CPcodeS.length() != 0) {
                        cp.setErrorEnabled(false);
                        ecp.getBackground().clearColorFilter();
                    }
                    if (fechaS.length() != 0) {
                        fechanacimiento.setErrorEnabled(false);
                        efechanacimiento.getBackground().clearColorFilter();
                    }
                }

                if (CPcodeS.length() == 0) {
                    cp.setErrorEnabled(true);
                    cp.setError(camponecesario);
                    ecp.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                    if (nameS.length() != 0) {
                        nombre.setErrorEnabled(false);
                        enombre.getBackground().clearColorFilter();
                    }
                    if (fechaS.length() != 0) {
                        fechanacimiento.setErrorEnabled(false);
                        efechanacimiento.getBackground().clearColorFilter();
                    }
                }

                if (fechaS.length() == 0) {
                    fechanacimiento.setErrorEnabled(true);
                    fechanacimiento.setError(camponecesario);
                    efechanacimiento.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                    if (nameS.length() != 0) {
                        nombre.setErrorEnabled(false);
                        enombre.getBackground().clearColorFilter();
                    }
                    if (CPcodeS.length() != 0) {
                        cp.setErrorEnabled(false);
                        ecp.getBackground().clearColorFilter();
                    }
                }

                if (sexS.equals("X")) {
                    TextView errorText = (TextView)sexo.getSelectedView();
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error

                    if (nameS.length() != 0) {
                        nombre.setErrorEnabled(false);
                        enombre.getBackground().clearColorFilter();
                    }
                    if (CPcodeS.length() != 0) {
                        cp.setErrorEnabled(false);
                        ecp.getBackground().clearColorFilter();
                    }
                    if (fechaS.length() != 0) {
                        fechanacimiento.setErrorEnabled(false);
                        efechanacimiento.getBackground().clearColorFilter();
                    }
                }

                else {
                    okey.setVisibility(View.GONE);
                    progbar.setVisibility(View.VISIBLE);

                    JSONObject values = new JSONObject();
                    try {
                        values.put("username", username);
                        values.put("bdate", fechaS);
                        values.put("cpCode", CPcodeS);
                        values.put("sex", sexS);
                        values.put("realname", nameS);
                        values.put("description", descripcioS);
                        values.put("neighborhood", barrio);
                        values.put("image", encoded);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new PostAsyncTask("https://agora-pes.herokuapp.com/api/profile", FillProfileActivity.this) {
                        @Override
                        protected void onPostExecute(JSONObject resObject) {
                            Boolean result = false;

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

                            String achievement = this.getNewAchievement();

                            if (result && achievement != null && !achievement.equals("")) {
                                sendNot(achievement);
                            }
                            if (result) {
                                //Toast.makeText(getApplicationContext(), "Titulo : " + strTitulo + " Descripcion : " + strDescripcion, Toast.LENGTH_LONG).show();
                                //     Toast.makeText(getApplicationContext(), creacionok, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(FillProfileActivity.this, MainActivity.class));
                            } else {
                                Log.i("asdCreacion", "reset");
                                okey.setVisibility(View.VISIBLE);
                                progbar.setVisibility(View.GONE);
                            }
                        }

                    }.execute(values);
                }



            }
        });

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
                    profileimage.setImageBitmap(bitmap);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }
                break;
        }
    }


    public void sendNot(String achievement){

        Intent i=new Intent(FillProfileActivity.this, LogrosActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(FillProfileActivity.this, 0, i, 0);

        String[] parts = achievement.split(",");
        int count = parts.length;
        for ( int j = 0; j < count; j++ ){
            String decoded = codificaLogro(parts[j]);
            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.logo);

            NotificationCompat.Builder mBuilder;
            NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_trofeo_logro)
                    .setContentTitle(getString(R.string.nuevo))
                    .setLargeIcon(icon)
                    .setContentText(decoded)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(decoded))
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(true);

            mNotifyMgr.notify(j+1, mBuilder.build());
        }

    }

private String codificaLogro(String codigoLogro) {

     /*   String[] parts = codigoLogro.split(",");
        int count = parts.length;
        String[] Logros = new String[count];*/
    // for (int i = 0; i < count; i++){
    String Logro = "";
    switch(codigoLogro) {
        case "PROP1": Logro = getApplicationContext().getString(R.string.PROP1);
            break;
        case "PROP5": Logro = getApplicationContext().getString(R.string.PROP5);
            break;
        case "PROP10": Logro = getApplicationContext().getString(R.string.PROP10);
            break;
        case "PROP50": Logro = getApplicationContext().getString(R.string.PROP50);
            break;
        case "PROP100": Logro = getApplicationContext().getString(R.string.PROP100);
            break;
        case "FAV1": Logro = getApplicationContext().getString(R.string.FAV1);
            break;
        case "FAV10": Logro = getApplicationContext().getString(R.string.FAV10);
            break;
        case "UBI1": Logro = getApplicationContext().getString(R.string.UBI1);
            break;
        case "UBI10": Logro = getApplicationContext().getString(R.string.UBI10);
            break;
        case "PROPC": Logro = getApplicationContext().getString(R.string.PROPC);
            break;
        case "PROPD": Logro = getApplicationContext().getString(R.string.PROPD);
            break;
        case "PROPO": Logro = getApplicationContext().getString(R.string.PROPO);
            break;
        case "PROPM": Logro = getApplicationContext().getString(R.string.PROPM);
            break;
        case "PROPE": Logro = getApplicationContext().getString(R.string.PROPE);
            break;
        case "PROPT": Logro = getApplicationContext().getString(R.string.PROPT);
            break;
        case "PROPQ": Logro = getApplicationContext().getString(R.string.PROPQ);
            break;
        case "PROPS": Logro = getApplicationContext().getString(R.string.PROPS);
            break;
        case "TWIT1": Logro = getApplicationContext().getString(R.string.TWIT1);
            break;
        case "TWIT100": Logro = getApplicationContext().getString(R.string.TWIT100);
            break;
        case "GLIKE1": Logro = getApplicationContext().getString(R.string.GLIKE1);
            break;
        case "GLIKE10": Logro = getApplicationContext().getString(R.string.GLIKE10);
            break;
        case "GLIKE100": Logro = getApplicationContext().getString(R.string.GLIKE100);
            break;
        case "PLIKE1": Logro = getApplicationContext().getString(R.string.PLIKE1);
            break;
        case "PLIKE10": Logro = getApplicationContext().getString(R.string.PLIKE10);
            break;
        case "PLIKE100": Logro = getApplicationContext().getString(R.string.PLIKE100);
            break;
        case "COM1": Logro = getApplicationContext().getString(R.string.COM1);
            break;
        case "COM5": Logro = getApplicationContext().getString(R.string.COM5);
            break;
        case "COM25": Logro = getApplicationContext().getString(R.string.COM25);
            break;
        case "COM100": Logro = getApplicationContext().getString(R.string.COM100);
            break;
        case "GCOM1": Logro = getApplicationContext().getString(R.string.GCOM1);
            break;
        case "GCOM10": Logro = getApplicationContext().getString(R.string.GCOM10);
            break;
        case "GCOM100": Logro = getApplicationContext().getString(R.string.GCOM100);
            break;
        default: Logro = "Something went wrong";
            break;
    }
    //   Logros[i]=Logro;
    //  }
    return Logro;
}
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

}

package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

    private final int SELECT_PICTURE = 200;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String[] diferentesSexos; //{getString(R.string.M), getString(R.string.F), getString(R.string.I)};
    String[] diferentesSexosGenerico = {"X", "I", "F", "M"};

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
        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        canviidioma.setOnClickListener(new LanguageOnClickListener(idioma, canviidioma, res, getApplicationContext()));

        profileimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Galería", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(FillProfileActivity.this);
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
                                //Toast.makeText(getApplicationContext(), "Result : " + result , Toast.LENGTH_LONG).show();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Log.i("asdBool", result.toString());

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

    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

}

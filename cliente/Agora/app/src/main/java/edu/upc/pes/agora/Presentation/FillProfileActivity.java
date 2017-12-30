package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import edu.upc.pes.agora.R;

public class FillProfileActivity extends AppCompatActivity {

    private TextInputLayout nombre, cp, fechanacimiento;
    private EditText enombre, ecp, efechanacimiento;
    private TextView username, zona;
    private String name, codipost, fech;
    private ImageView profileimage;
    private Spinner sexo;
    private Button okey;
    private ProgressBar progbar;

    private String encoded;

    private final int SELECT_PICTURE=200;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String[] diferentesSexos; //{getString(R.string.M), getString(R.string.F), getString(R.string.I)};
    String[] diferentesSexosGenerico = {"I", "F", "M"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_profile);

        Log.i("Username", Constants.Username);
        Log.i("Extras", Integer.toString(Constants.zone));

        username = (TextView) findViewById(R.id.usernameprofile);
        username.setText(Constants.Username);
        zona = (TextView) findViewById(R.id.barrio);
        zona.setText(Integer.toString(Constants.zone));

        nombre = (TextInputLayout) findViewById(R.id.nombre_up);
        cp = (TextInputLayout) findViewById(R.id.codipostal_up);
        fechanacimiento = (TextInputLayout) findViewById(R.id.fechanaix_up);

        enombre = (EditText) findViewById(R.id.nombrecompleto);
        ecp = (EditText) findViewById(R.id.cpostal);
        efechanacimiento = (EditText) findViewById(R.id.fecha);

        sexo = (Spinner) findViewById(R.id.sexo);

        okey = (Button) findViewById(R.id.CompletedReg);
        progbar = (ProgressBar) findViewById(R.id.completeprog);

        profileimage = (ImageView) findViewById(R.id.profileimage);

        ImageView canviidioma = (ImageView) findViewById(R.id.multiidiomareg);
        ImageView enrerre = (ImageView) findViewById(R.id.backbutton);

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
        Intent back = new Intent(FillProfileActivity.this, RegisterActivity.class);
        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        canviidioma.setOnClickListener(new LanguageOnClickListener(idioma, canviidioma, res, getApplicationContext()));

        enrerre.setOnClickListener(new BackOnClickListener(back, getApplicationContext()));

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

        final String[] categorias = new String[]{
                "Selecciona un Sexo",
                "Hombre",
                "Mujer",
                "Indefinido"};

        final List<String> categoriesList = new ArrayList<>(Arrays.asList(categorias));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_categories_layout, categoriesList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
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
                if (categorias[position].equals(getString(R.string.spinnerhint))){
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
                // implementar cambios de los atributos del usuario en el servidor
                // Toast.makeText(getApplicationContext(),"aqui cambiaremos los valores al server", Toast.LENGTH_LONG).show();

                okey.setVisibility(View.GONE);
                progbar.setVisibility(View.VISIBLE);

                JSONObject values = new JSONObject();
                try {
                    //  strTitulo = Titulo.getText().toString();
                    //  strDescripcion = Descripcion.getText().toString();

                    String nombre = enombre.getText().toString();
                    String CPcode = ecp.getText().toString();
                    String fecha = efechanacimiento.getText().toString();
                    String sex = diferentesSexosGenerico[sexo.getSelectedItemPosition()];
                    String username = Constants.Username;

                    values.put("username", username);
                    values.put("bdate", fecha);
                    values.put("cpCode", CPcode);
                    values.put("sex", sex);
                    values.put("realname", nombre);
                    values.put("image", encoded);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // nou server : agora-pes.herokuapp.com/api/proposal
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

}
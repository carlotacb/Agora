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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.upc.pes.agora.Logic.ServerConection.PostAsyncTask;
import edu.upc.pes.agora.Logic.Models.Profile;
import edu.upc.pes.agora.R;

public class EditProfileActivity extends AppCompatActivity {

    private EditText Nombre;
    private EditText CP;
    private EditText Barrio;
    private TextView Fecha;
    private EditText Descripcion;
    private Spinner spin;
    private TextView Username;

    private ImageView image;
    private TextView button;
    private String encoded;

    private final int SELECT_PICTURE=200;

    private ProgressBar prog;

    private TextView Change;

    private Button Aceptar;
    private Button Cancelar;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String[] diferentesSexos; //{getString(R.string.M), getString(R.string.F), getString(R.string.I)};
    String[] diferentesSexosGenerico = {"I", "F", "M"};

    Profile p ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        diferentesSexos = new String[]{getString(R.string.I), getString(R.string.F), getString(R.string.M)};

        Nombre = (EditText) findViewById(R.id.nameprofile);
        CP = (EditText) findViewById(R.id.codipostal);
        Barrio = (EditText) findViewById(R.id.barrio);
        Fecha = (TextView) findViewById(R.id.fecha);
        //Descripcion = (EditText) findViewById(R.id.descript);
        Username = (TextView) findViewById(R.id.usernameprofile);
        image = (ImageView) findViewById(R.id.setImage);
        button = (TextView) findViewById(R.id.buttonImage);

        p = new Profile();
        Intent i = getIntent();

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Galería", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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

        if (i.hasExtra("cp")) {
            Integer cp = i.getIntExtra("cp", 0);
            CP.setText(cp.toString());
        }
        if (i.hasExtra("barrio")) {
            Barrio.setText(i.getStringExtra("barrio"));
        }
        if (i.hasExtra("nombre")) {
            Nombre.setText(i.getStringExtra("nombre"));
        }
        Fecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditProfileActivity.this,
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
                Fecha.setText(date);
            }
        };
        /*if (i.hasExtra("fecha")) {
            Long f = i.getLongExtra("fecha", 0);
            if (f != 0) {
                Date b = new Date(f);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String s = dateFormat.format(b);
                Fecha.setText(s);
            }
        }*/
        if (i.hasExtra("username")) {
            Username.setText(i.getStringExtra("username"));
        }

        //Getting the instance of Spinner and applying OnItemSelectedListener on it

        //spin = (Spinner) findViewById(R.id.sexo);
        if (i.hasExtra("sex")) {

            spin = (Spinner) findViewById(R.id.sexo);

            if (i.hasExtra("sex")) {

                spin.setSelection(i.getIntExtra("sex", 0));
            }

            Change = (TextView) findViewById(R.id.changePassword);

            Aceptar = (Button) findViewById(R.id.aceptar);
            Cancelar = (Button) findViewById(R.id.cancelar);

            prog = (ProgressBar) findViewById(R.id.saveprogressbar);

            final Resources res = this.getResources();


            //Creating the ArrayAdapter instance having the bank name list
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, diferentesSexos);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spin.setAdapter(aa);


            Change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditProfileActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_changepass, null);

                    final EditText mOldPass = (EditText) mView.findViewById(R.id.etPassword);
                    final EditText mNewPass1 = (EditText) mView.findViewById(R.id.etPassword2);
                    final EditText mNewPass2 = (EditText) mView.findViewById(R.id.etPassword3);
                    Button mAccept = (Button) mView.findViewById(R.id.etAccept);
                    Button mCancel = (Button) mView.findViewById(R.id.etCancel);

                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    mAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //implementar la obtencion del password del usuario
                            if (!mOldPass.getText().toString().equals("123") | mOldPass.equals("")) {
                                mOldPass.setText("");
                                mNewPass1.setText("");
                                mNewPass2.setText("");

                                Toast.makeText(getApplicationContext(), "Password introducido incorrecto", Toast.LENGTH_LONG).show();

                            } else if (!mNewPass1.getText().toString().equals(mNewPass2.getText().toString())) {
                                mOldPass.setText("");
                                mNewPass1.setText("");
                                mNewPass2.setText("");
                                Toast.makeText(getApplicationContext(), "Nueva Password incorrecta", Toast.LENGTH_LONG).show();
                            } else if (mNewPass1.getText().toString().equals("") | mNewPass2.getText().toString().equals("")) {
                                mOldPass.setText("");
                                mNewPass1.setText("");
                                mNewPass2.setText("");
                                Toast.makeText(getApplicationContext(), "Rellena todos los campos", Toast.LENGTH_LONG).show();
                            } else {
                                //implementar el cambio de password del usuario
                                Toast.makeText(getApplicationContext(), "Password actualizado correctamente", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                    });

                    mCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getApplicationContext(),"Password actualizado correctamente", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
                    Toast.makeText(getApplicationContext(), "A CAMBIAR PASSWORD", Toast.LENGTH_LONG).show();
                    // show.DialogFragment();

                }
            });

            Aceptar.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onClick(View view) {
                    // implementar cambios de los atributos del usuario en el servidor
                    // Toast.makeText(getApplicationContext(),"aqui cambiaremos los valores al server", Toast.LENGTH_LONG).show();

                    Aceptar.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);

                    JSONObject values = new JSONObject();
                    try {
                        //  strTitulo = Titulo.getText().toString();
                        //  strDescripcion = Descripcion.getText().toString();

                        String nombre = Nombre.getText().toString();
                        String CPcode = CP.getText().toString();
                        String barrio = Barrio.getText().toString();
                        String fecha = Fecha.getText().toString();
                        String sexo = diferentesSexosGenerico[spin.getSelectedItemPosition()];
                        //String descripcion = Descripcion.getText().toString() ;
                        String username = Username.getText().toString();

                        values.put("username", username);
                        values.put("bdate", fecha);
                        values.put("cpCode", CPcode);
                        values.put("sex", sexo);
                        values.put("neighborhood", barrio);
                        values.put("realname", nombre);
                        //values.put("description",descripcion);
                        values.put("image", encoded);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // nou server : agora-pes.herokuapp.com/api/proposal
                    new PostAsyncTask("https://agora-pes.herokuapp.com/api/profile", EditProfileActivity.this) {
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
                                startActivity(new Intent(EditProfileActivity.this, MyProfileActivity.class));
                            } else {
                                Log.i("asdCreacion", "reset");
                                Aceptar.setVisibility(View.VISIBLE);
                                prog.setVisibility(View.GONE);
                            }

                        }
                    }.execute(values);
                }
            });

            Cancelar.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(EditProfileActivity.this, MyProfileActivity.class));
                }
            });
        }
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
                    image.setImageBitmap(bitmap);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }
                break;
        }
    }
}

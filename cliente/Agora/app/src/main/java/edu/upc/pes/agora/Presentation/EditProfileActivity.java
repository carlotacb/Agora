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
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import edu.upc.pes.agora.Logic.Models.Profile;
import edu.upc.pes.agora.Logic.ServerConection.PutAsyncTask;
import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.R;

public class EditProfileActivity extends AppCompatActivity {

    private EditText Nombre;
    private EditText CP;
    private TextView Barrio;
    private TextView Fecha;
    private EditText Descripcion;
    private Spinner spin;
    private TextView Username;

    private ImageView image;
    private TextView button;
    private String encoded;

    private final int SELECT_PICTURE = 200;

    private ProgressBar prog;

    private TextView Change;

    private Button Aceptar;
    private Button Cancelar;

    private boolean activa1, activa2, activa3;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String[] diferentesSexos; //{getString(R.string.M), getString(R.string.F), getString(R.string.I)};
    String[] diferentesSexosGenerico = {"X", "M", "F", "I"};

    Profile p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //final Resources res = this.getResources();


        diferentesSexos = new String[]{getString(R.string.I), getString(R.string.F), getString(R.string.M)};

        image = (ImageView) findViewById(R.id.setImage);

        Nombre = (EditText) findViewById(R.id.nameprofile);
        CP = (EditText) findViewById(R.id.codipostal);
        Descripcion = (EditText) findViewById(R.id.descript);
        Fecha = (TextView) findViewById(R.id.fecha);

        Nombre.getBackground().clearColorFilter();
        CP.getBackground().clearColorFilter();
        Descripcion.getBackground().clearColorFilter();
        Fecha.getBackground().clearColorFilter();

        Barrio = (TextView) findViewById(R.id.barrio);
        Username = (TextView) findViewById(R.id.usernameprofile);
        button = (TextView) findViewById(R.id.buttonImage);
        Change = (TextView) findViewById(R.id.changePassword);

        spin = (Spinner) findViewById(R.id.sexo);

        Aceptar = (Button) findViewById(R.id.aceptar);
        Cancelar = (Button) findViewById(R.id.cancelar);

        prog = (ProgressBar) findViewById(R.id.saveprogressbar);

        ImageView canviidioma = (ImageView) findViewById(R.id.multiidiomareg);
        ImageView enrerre = (ImageView) findViewById(R.id.backbutton);

        p = new Profile();
        Intent i = getIntent();
        final Resources res = this.getResources();

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

        Intent idioma = new Intent(EditProfileActivity.this, EditProfileActivity.class);
        idioma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        canviidioma.setOnClickListener(new LanguageOnClickListener(idioma, canviidioma, res, getApplicationContext()));

        Intent back = new Intent(EditProfileActivity.this, MyProfileActivity.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        enrerre.setOnClickListener(new BackOnClickListener(back, getApplicationContext()));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Galería", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Escoge una opción");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selection) {
                        if (options[selection] == "Galería") {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                        } else if (options[selection] == "Cancelar") {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        Username.setText(Constants.Username);

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
        if (i.hasExtra("descripcion")) {
            Descripcion.setText(i.getStringExtra("descripcion"));
        }
        if (i.hasExtra("fecha")) {
            Fecha.setText(i.getStringExtra("fecha"));
        }
        if (i.hasExtra("image")) {
            //image.setImageBitmap(i.getStringExtra("image"));
        }
        Integer selection = 0;
        if (i.hasExtra("sexof")) {
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

        final String[] sexos = new String[]{
                getString(R.string.spinnerhintsexo),
                getString(R.string.hombre),
                getString(R.string.mujer),
                getString(R.string.indefinido)};

        final List<String> sexosList = new ArrayList<>(Arrays.asList(sexos));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_categories_layout, sexosList) {
            @Override
            public boolean isEnabled(int position) {
                if (sexos[position].equals(getString(R.string.spinnerhintsexo))) {
                    // Disable the first item from Spinner. The first item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (sexos[position].equals(getString(R.string.spinnerhintsexo))) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_categories_layout);
        spin.setAdapter(spinnerArrayAdapter);
        spin.setSelection(selection);


        Fecha.setOnClickListener(new View.OnClickListener() {
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
                String date = +year + "/" + monthOfYear + "/" + dayOfMonth;
                Fecha.setText(date);
            }
        };

        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("asd123", "es clica");

                final AlertDialog.Builder dialogochange = new AlertDialog.Builder(view.getRootView().getContext());

                final EditText input = new EditText(view.getRootView().getContext());
                final EditText input2 = new EditText(view.getRootView().getContext());
                final EditText input3 = new EditText(view.getRootView().getContext());
                //FrameLayout container = new FrameLayout(EditProfileActivity.this);
                //FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout layout = new LinearLayout(EditProfileActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                //layout.setLayoutParams(params);

                input.setLayoutParams(params);
                input.setHint(getString(R.string.pwactual));
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                input.getBackground().clearColorFilter();
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});

                input2.setLayoutParams(params);
                input2.setHint(getString(R.string.pwnueva));
                input2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                input2.getBackground().clearColorFilter();
                input2.setInputType(InputType.TYPE_CLASS_TEXT);
                input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});

                input3.setLayoutParams(params);
                input3.setHint(getString(R.string.pwnueva2));
                input3.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input3.setTransformationMethod(PasswordTransformationMethod.getInstance());
                input3.getBackground().clearColorFilter();
                input3.setInputType(InputType.TYPE_CLASS_TEXT);
                input3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});

                layout.addView(input);
                layout.addView(input2);
                layout.addView(input3);
                dialogochange.setTitle(getString(R.string.cps));
                dialogochange.setMessage(getString(R.string.cambio_password));
                dialogochange.setIcon(R.drawable.logo);

                dialogochange.setCancelable(false);
                dialogochange.setView(layout);

                dialogochange.setPositiveButton(getString(R.string.Aceptar), null);

                dialogochange.setNegativeButton(getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialogochange.create();

                final AlertDialog dialog2 = dialogochange.create();

                dialog2.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        final Button acceptar = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                        Log.i("asdentra", "entraaa4");

                        acceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!input.getText().toString().equals("") & (input.getText().toString().equals(input2.getText().toString()) | input.getText().toString().equals(input3.getText().toString()))) {
                                    Log.i("asdentra", "entraaa22");
                                    input.setText("");
                                    input2.setText("");
                                    input3.setText("");
                                    Toast.makeText(getApplicationContext(), getString(R.string.diferente), Toast.LENGTH_LONG).show();

                                } else if (!input2.getText().toString().equals(input3.getText().toString())) {
                                    Log.i("asdentra", "entraaa33");
                                    input.setText("");
                                    input2.setText("");
                                    input3.setText("");
                                    input2.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                                    input3.getBackground().setColorFilter(getResources().getColor(R.color.red_500_primary), PorterDuff.Mode.SRC_ATOP);
                                    Toast.makeText(getApplicationContext(), "Nueva Password incorrecta", Toast.LENGTH_LONG).show();
                                } else {
                                    //implementar el cambio de password del usuario
                                    Log.i("asdentra", "entraaa");
                                    cambiarPassword(input, input2, input3, res);
                                    Toast.makeText(getApplicationContext(), "Password actualizado correctamente", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }
                        });

                    }
                });

                dialog2.show();

                activa1 = false;
                activa2 = false;
                activa3 = false;

                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (input.getText().toString().length() == 0) {
                            // Disable ok button
                            activa1 = false;
                            (dialog2).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                        } else {
                            // Something into edit text. Enable the button.
                            activa1 = true;
                            if (activa2 && activa3) {
                                (dialog2).getButton(
                                        AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            } else if (!activa2 || !activa3) {
                                (dialog2).getButton(
                                        AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            }
                        }
                    }
                });

                input2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (input2.getText().toString().length() == 0) {
                            // Disable ok button
                            activa2 = false;
                            (dialog2).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            // Something into edit text. Enable the button.
                            activa2 = true;
                            if (activa1 && activa3) {
                                (dialog2).getButton(
                                        AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            } else if (!activa1 || !activa3) {
                                (dialog2).getButton(
                                        AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            }
                        }
                    }
                });

                input3.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (input3.getText().toString().length() == 0) {
                            // Disable ok button
                            activa3 = false;
                            (dialog2).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            // Something into edit text. Enable the button.
                            activa3 = true;
                            if (activa2 && activa1) {
                                (dialog2).getButton(
                                        AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            } else if (!activa2 || !activa1) {
                                (dialog2).getButton(
                                        AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            }
                        }
                    }
                });

            }
        });

        Aceptar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                Aceptar.setVisibility(View.GONE);
                prog.setVisibility(View.VISIBLE);

                JSONObject values = new JSONObject();
                try {
                    String nombre = Nombre.getText().toString();
                    String CPcode = CP.getText().toString();
                    String barrio = Barrio.getText().toString();
                    String fecha = Fecha.getText().toString();
                    String sexo = diferentesSexosGenerico[spin.getSelectedItemPosition()];
                    Log.i("asd123", diferentesSexosGenerico[spin.getSelectedItemPosition()]);
                    String descripcion = Descripcion.getText().toString();
                    String username = Username.getText().toString();

                    values.put("username", username);
                    values.put("bdate", fecha);
                    values.put("cpCode", CPcode);
                    values.put("sex", sexo);
                    values.put("neighborhood", barrio);
                    values.put("realname", nombre);
                    values.put("description", descripcion);
                    values.put("image", encoded);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (result) {
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

    @SuppressLint("StaticFieldLeak")
    private void cambiarPassword(EditText mOldPass, EditText mNewPass1, EditText mNewPass2, final Resources res) {

        final JSONObject values = new JSONObject();
        try {
            values.put("oldpassword", mOldPass.getText().toString());
            values.put("password", mNewPass1.getText().toString());
            values.put("confirmpassword", mNewPass2.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new PutAsyncTask("https://agora-pes.herokuapp.com/api/profile/", EditProfileActivity.this) {
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
                        //Toast.makeText(getApplicationContext(), error , Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (result) {

                    Toast.makeText(getApplicationContext(), "Password Cambiado correctamente", Toast.LENGTH_LONG).show();

                } else {
                    Log.i("asdCreacion", "reset");

                }

            }
        }.execute(values);

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
                    Constants.fotoperfil = bitmap;

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }
                break;
        }
    }
}

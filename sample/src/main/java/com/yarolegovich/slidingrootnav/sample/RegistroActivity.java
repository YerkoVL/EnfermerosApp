package com.yarolegovich.slidingrootnav.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.slidingrootnav.sample.conexion.Singleton;
import com.yarolegovich.slidingrootnav.sample.entity.Respuesta;
import com.yarolegovich.slidingrootnav.sample.tools.GenericAlerts;

import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.CONTIN;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.INICIAL;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_APELLIDOS;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_CORREO;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_LATITUD;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_LONGITUD;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_NOMBRES;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_PASS;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_TELEFONO;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.URL_REGISTRO;

public class RegistroActivity extends AppCompatActivity {
    Gson gson = new Gson();
    Context mCtx;
    ProgressDialog progressDialog = null;
    GenericAlerts alertas = new GenericAlerts();
    String URL_REG = "";

    EditText EdtNombres, EdtApellidos, EdtCorreo, EdtTelefono, EdtPass;
    String EdtLongitud, EdtLatitud;
    Button BtnRegistrar;

    String nombreValidado, apellidoValidado, correoValidado, telefonoValidado, passValidado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_fragment);
        mCtx = this;
        progressDialog = new ProgressDialog(mCtx);
        getSupportActionBar().setTitle("Registro de cuenta");

        EdtNombres = findViewById(R.id.regEdtNombres);
        EdtApellidos = findViewById(R.id.regEdtApellidos);
        EdtCorreo = findViewById(R.id.regEdtCorreo);
        EdtTelefono = findViewById(R.id.regEdtTelefono);
        EdtPass = findViewById(R.id.regEdtPass);
        BtnRegistrar = findViewById(R.id.regBtnRegistrar);

        EdtLongitud = "0";
        EdtLatitud = "0";

        BtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarTexto()){
                    validarDatos();
                }
            }
        });

    }

    public boolean validarTexto() {
        boolean valor = false;

        nombreValidado = EdtNombres.getText().toString();
        validarNombres(nombreValidado);
        apellidoValidado = EdtApellidos.getText().toString();
        validarApellido(apellidoValidado);
        correoValidado = EdtCorreo.getText().toString();
        telefonoValidado = EdtTelefono.getText().toString();
        passValidado = EdtPass.getText().toString();

        if (!nombreValidado.equals("")) {
            if (!apellidoValidado.equals("")) {
                if (!correoValidado.equals("")) {
                    if (!telefonoValidado.equals("")) {
                        if (!passValidado.equals("")) {
                            valor = true;
                        } else {
                            Toast.makeText(mCtx, "Ingrese Contraseña", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mCtx, "Ingrese telefono", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mCtx,"Ingrese correo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mCtx, "Ingrese apellidos", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mCtx, "Ingrese nombres", Toast.LENGTH_SHORT).show();
        }
        return valor;
    }

    public void validarNombres(String nombres){
        String c[] = nombres.split(" ");
        if(c.length>=2) {
            for (int i = 0; i < c.length; i++) {
                String nuevo = c[i];
                nombres = nuevo + "%";
                nombreValidado = nombres;
            }
        }
    }

    public void validarApellido(String apellidos){
        String c[] = apellidos.split(" ");
        if(c.length>=2) {
            for (int i = 0; i < c.length; i++) {
                String nuevo = c[i];
                if(i<=c.length-1) {
                    apellidos = nuevo + "%";
                }else{
                    apellidos = nuevo;
                }
                apellidoValidado = apellidos;
            }
        }
    }

    public void validarDatos(){
        URL_REG = URL_REGISTRO + INICIAL
                + PARAM_NOMBRES + nombreValidado
                + CONTIN + PARAM_APELLIDOS + apellidoValidado
                + CONTIN + PARAM_CORREO + correoValidado
                + CONTIN + PARAM_TELEFONO + telefonoValidado
                + CONTIN + PARAM_PASS + passValidado
                + CONTIN + PARAM_LONGITUD + EdtLongitud
                + CONTIN + PARAM_LATITUD + EdtLatitud;
        //Nombres=Pepe&Apellidos=Vasquez&Correo=micorreo@gmail.com&Telefono=12345678&Contrasena=654321&Longitud=0&Latitud=0

        progressDialog.show();

        StringRequest respuestaRegistro = new StringRequest(Request.Method.GET,URL_REG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {

                        if(Response!=null) {
                            try {
                                Respuesta respuesta = gson.fromJson(Response, Respuesta.class);
                                if(respuesta.getValor()==true) {
                                    progressDialog.dismiss();
                                    new LovelyStandardDialog(mCtx)
                                            .setTopColorRes(R.color.colorPrimary)
                                            .setButtonsColorRes(R.color.colorAccent)
                                            .setIcon(R.drawable.ic_enfermera)
                                            .setTitle("Completado")
                                            .setMessage(respuesta.getMensaje())
                                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(new Intent(mCtx, LoginActivity.class));
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, null)
                                            .show();
                                }else{
                                    progressDialog.dismiss();
                                    alertas.mensajeInfo("Error",respuesta.getMensaje(),mCtx);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                progressDialog.dismiss();
                                alertas.mensajeInfo("Error","None",mCtx);
                            }
                        }else{
                            Respuesta respuesta = gson.fromJson(Response,Respuesta.class);
                            progressDialog.dismiss();
                            alertas.mensajeInfo("Error",respuesta.getMensaje(),mCtx);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                progressDialog.dismiss();
                alertas.mensajeInfo("Error","Error desconocido",mCtx);
            }
        });

        respuestaRegistro.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Singleton.getInstance(mCtx).addToRequestQueue(respuestaRegistro);
    }
}

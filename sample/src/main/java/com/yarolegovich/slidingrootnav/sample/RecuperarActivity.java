package com.yarolegovich.slidingrootnav.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.INICIAL;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_EMAIL;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.URL_RESTAURAR;

public class RecuperarActivity extends AppCompatActivity {
    EditText correo;
    Button recuperar;

    Gson gson = new Gson();
    Context mCtx;
    ProgressDialog progressDialog = null;
    GenericAlerts alertas = new GenericAlerts();

    String URL_REC = "",correoValidado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

        mCtx = this;

        progressDialog = new ProgressDialog(mCtx);

        correo = findViewById(R.id.recEdtEmail);
        recuperar = findViewById(R.id.recBtnRecuperar);

        recuperar.setOnClickListener(new View.OnClickListener() {
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

        correoValidado = correo.getText().toString();

        if (!correoValidado.equals("")) {
            valor = true;
        } else {
            Toast.makeText(mCtx, "Ingrese Correo", Toast.LENGTH_SHORT).show();
        }
        return valor;
    }

    public void validarDatos(){
        URL_REC = URL_RESTAURAR + INICIAL
                + PARAM_EMAIL + correoValidado;

        progressDialog.show();

        StringRequest respuestaRecuperada = new StringRequest(Request.Method.GET,URL_REC,
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
                                            .setTitle("Éxito")
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
                                    alertas.mensajeInfo("Fallo Recuperar",respuesta.getMensaje(),mCtx);
                                    progressDialog.dismiss();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                alertas.mensajeInfo("Fallo Recuperar","None",mCtx);
                                progressDialog.dismiss();
                            }
                        }else{
                            Respuesta respuesta = gson.fromJson(Response,Respuesta.class);
                            alertas.mensajeInfo("Fallo Recuperar",respuesta.getMensaje(),mCtx);
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                progressDialog.dismiss();
                alertas.mensajeInfo("Fallo Recuperar","Error Desconocido",mCtx);
            }
        });

        respuestaRecuperada.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Singleton.getInstance(mCtx).addToRequestQueue(respuestaRecuperada);
    }

}

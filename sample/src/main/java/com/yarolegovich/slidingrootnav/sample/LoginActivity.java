package com.yarolegovich.slidingrootnav.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.gson.Gson;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.slidingrootnav.sample.conexion.Singleton;
import com.yarolegovich.slidingrootnav.sample.entity.Respuesta;
import com.yarolegovich.slidingrootnav.sample.entity.Usuario;
import com.yarolegovich.slidingrootnav.sample.tools.GenericAlerts;
import com.yarolegovich.slidingrootnav.sample.tools.YourPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.CONTIN;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.INICIAL;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.LOGIN_PARAM_PASS;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.LOGIN_PARAM_USUARIO;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_APELLIDOS;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_CORREO;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_LATITUD;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_LONGITUD;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_NOMBRES;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_PASS;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PARAM_TELEFONO;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.URL_LOGIN;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.URL_REGISTRO;

public class LoginActivity extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    private KenBurnsView mImg;
    private Usuario usuario;
    private YourPreference preferencias;
    Gson gson = new Gson();
    Context mCtx;
    ProgressDialog progressDialog = null;
    GenericAlerts alertas = new GenericAlerts();
    String URL_LOG = "";

    EditText EdtUsuario, EdtPass;
    TextView TxtIngresar, TxtRegistrar, TxtOlvido;

    String usuarioValidado,contrasenaValidada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCtx = this;

        progressDialog = new ProgressDialog(mCtx);
        preferencias = YourPreference.getInstance(mCtx);

        EdtUsuario = findViewById(R.id.logEdtUsuario);
        EdtPass = findViewById(R.id.logEdtPassword);
        TxtIngresar = findViewById(R.id.logBtnIngresar);
        TxtRegistrar = findViewById(R.id.logBtnRegistrar);
        TxtOlvido = findViewById(R.id.logTxtOlvido);
        loginButton = findViewById(R.id.login_button);

        mImg = findViewById(R.id.imgFondo);

        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_birthday");
        loginButton.setReadPermissions(permissions);

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                obtenerDatos(loginResult.getAccessToken());
                dirigirMenuPrincipal();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this,"Se canceló el login",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this,"Ocurrió un error",Toast.LENGTH_SHORT).show();
            }
        });

        TxtIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarTexto()){
                    validarDatos();
                }
            }
        });

        TxtOlvido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RecuperarActivity.class));
            }
        });

        TxtRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistroActivity.class));
            }
        });

        if(preferencias.sesionIniciada()){
            startActivity(new Intent(LoginActivity.this,SampleActivity.class));
        }
    }

    public void dirigirMenuPrincipal(){
        Intent intent = new Intent(mCtx,SampleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void obtenerDatos(AccessToken token){
        GraphRequest fb = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Profile profile = Profile.getCurrentProfile();
                usuario = new Usuario();
                usuario.setId(profile.getId().toString());
                usuario.setNombres(profile.getName());
                //String pruebaNombre = profile.getMiddleName();
                usuario.setNombreUsuario(profile.getFirstName());
                usuario.setPassword(profile.getFirstName() + "|" + profile.getId());
                usuario.setApellidos(validarEspacios(profile.getLastName()));
                usuario.setImagen(profile.getProfilePictureUri(100,100).toString());
                usuario.setPerfil("CLIENTE");
                try {
                    usuario.setCorreo(object.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    usuario.setPassword(object.getString("password"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    usuario.setDireccion(object.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String pruebaHometown = object.getString("hometown");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    usuario.setImagen(object.getString("picture"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    usuario.setTelefono(Integer.parseInt(object.getString("user_mobile_phone")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                registrarUsuarioFB(usuario);
            }
        });
        Bundle adicional = new Bundle();
        adicional.putString("fields","email, gender, address, hometown, location, short_name, picture");
        fb.setParameters(adicional);
        fb.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    public String validarEspacios(String texto){
        String nuevo[] = texto.split(" ");
        String textoValidado = "";

        for (int i=0;i<nuevo.length;i++){
            textoValidado = nuevo[i].toString();
            if(i<=nuevo.length-1) {
                textoValidado = textoValidado + "%";
            }
        }
        return textoValidado;
    }

    public void registrarUsuarioFB(Usuario usuario){
        String URL_REG = URL_REGISTRO + INICIAL
                + PARAM_NOMBRES + usuario.getNombreUsuario()
                + CONTIN + PARAM_APELLIDOS + usuario.getApellidos()
                + CONTIN + PARAM_CORREO + usuario.getCorreo()
                + CONTIN + PARAM_TELEFONO + usuario.getTelefono()
                + CONTIN + PARAM_PASS + usuario.getPassword()
                + CONTIN + PARAM_LONGITUD + 0
                + CONTIN + PARAM_LATITUD + 0;
        //Nombres=Pepe&Apellidos=Vasquez&Correo=micorreo@gmail.com&Telefono=12345678&Contrasena=654321&Longitud=0&Latitud=0

        progressDialog.show();

        StringRequest respuestaLogin = new StringRequest(Request.Method.GET,URL_REG,
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
                                            .setTitle(R.string.accion_completado)
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
                                    alertas.mensajeInfo("" + R.string.accion_error,respuesta.getMensaje(),mCtx);
                                    progressDialog.dismiss();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                alertas.mensajeInfo("" + R.string.accion_error,"None",mCtx);
                                progressDialog.dismiss();
                            }
                        }else{
                            Respuesta respuesta = gson.fromJson(Response,Respuesta.class);
                            alertas.mensajeInfo("" + R.string.accion_error,respuesta.getMensaje(),mCtx);
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                progressDialog.dismiss();
                alertas.mensajeInfo("" + R.string.accion_error,"Error Desconocido",mCtx);
            }
        });

        Singleton.getInstance(mCtx).addToRequestQueue(respuestaLogin);
    }

    public void finalizarActividad(){
        new LovelyStandardDialog(LoginActivity.this)
                .setTopColorRes(R.color.colorPrimary)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_enfermera)
                .setTitle(R.string.salir_app)
                .setMessage(R.string.salir_informacion)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preferencias.eliminarPreferencias(LoginActivity.this);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public boolean validarTexto() {
        boolean valor = false;

        usuarioValidado = EdtUsuario.getText().toString();
        contrasenaValidada = EdtPass.getText().toString();

        if (!usuarioValidado.equals("")) {
            if (!contrasenaValidada.equals("")) {
                valor = true;
            } else {
                Toast.makeText(mCtx, R.string.falta_password, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mCtx, R.string.falta_usuario, Toast.LENGTH_SHORT).show();
        }
        return valor;
    }

    public void validarDatos(){

        URL_LOG = URL_LOGIN + INICIAL + LOGIN_PARAM_USUARIO + usuarioValidado
                + CONTIN + LOGIN_PARAM_PASS + contrasenaValidada;

        progressDialog.show();

        StringRequest respuestaLogin = new StringRequest(Request.Method.GET,URL_LOG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {

                        if(Response!=null) {
                            try {
                                Usuario usuario = gson.fromJson(Response, Usuario.class);
                                if(usuario.getNombres()!=null) {
                                    preferencias.saveUsuario(usuario);
                                    progressDialog.dismiss();
                                    startActivity(new Intent(mCtx, SampleActivity.class));
                                }else{
                                    Respuesta respuesta = gson.fromJson(Response,Respuesta.class);
                                    alertas.mensajeInfo("" + R.string.accion_error,respuesta.getMensaje(),mCtx);
                                    progressDialog.dismiss();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                alertas.mensajeInfo("" + R.string.accion_error,"None",mCtx);
                                progressDialog.dismiss();
                            }
                        }else{
                            Respuesta respuesta = gson.fromJson(Response,Respuesta.class);
                            alertas.mensajeInfo("" + R.string.accion_error,respuesta.getMensaje(),mCtx);
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                progressDialog.dismiss();
                alertas.mensajeInfo("" + R.string.accion_error,"" + R.string.accion_error_desconocido,mCtx);
            }
        });

        Singleton.getInstance(mCtx).addToRequestQueue(respuestaLogin);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finalizarActividad();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

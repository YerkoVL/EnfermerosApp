package com.yarolegovich.slidingrootnav.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

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
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.slidingrootnav.sample.entity.Usuario;
import com.yarolegovich.slidingrootnav.sample.tools.YourPreference;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    private KenBurnsView mImg;
    private Usuario usuario;
    private YourPreference preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        mImg = findViewById(R.id.imgFondo);

        preferencias = YourPreference.getInstance(this);

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

        if(preferencias.sesionIniciada()){
            startActivity(new Intent(LoginActivity.this,SampleActivity.class));
        }
    }

    public void dirigirMenuPrincipal(){
        Intent intent = new Intent(this,SampleActivity.class);
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
                usuario.setApellidos(profile.getLastName());
                usuario.setImagen(profile.getProfilePictureUri(100,100).toString());
                usuario.setPerfil("CLIENTE");
                try {
                    usuario.setCorreo(object.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String pruebaAdrres = object.getString("address");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String pruebaHometown = object.getString("hometown");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String prubaFotos = object.getString("picture");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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

    public void finalizarActividad(){
        new LovelyStandardDialog(LoginActivity.this)
                .setTopColorRes(R.color.colorPrimary)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_enfermera)
                .setTitle("¿Desea salir de la aplicación?")
                .setMessage("Se eliminaran datos de configuración.")
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finalizarActividad();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

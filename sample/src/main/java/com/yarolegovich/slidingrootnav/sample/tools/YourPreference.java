package com.yarolegovich.slidingrootnav.sample.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.yarolegovich.slidingrootnav.sample.entity.Usuario;

import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.APELLIDOS;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.IMAGEN_PERFIL;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.NOMBRES;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.PASS_APP;
import static com.yarolegovich.slidingrootnav.sample.tools.GenericStrings.USUARIO_APP;

public class YourPreference {
    private static YourPreference yourPreference;
    private SharedPreferences sharedPreferences;

    public static YourPreference getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new YourPreference(context);
        }
        return yourPreference;
    }

    private YourPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("MisDatos",Context.MODE_PRIVATE);
    }

    public void eliminarPreferencias(Context context){
        SharedPreferences settings = context.getSharedPreferences("MisDatos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(USUARIO_APP);
        editor.apply();
        //.clear().commit();
    }

    public void saveDataString(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    public void saveDataFloat(String key,float value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putFloat(key, value);
        prefsEditor.commit();
    }

    public void saveUsuario(Usuario usuario){
        try{
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putString(USUARIO_APP, usuario.getNombreUsuario());
            prefsEditor.putString(PASS_APP, usuario.getPassword());
            prefsEditor.putString(IMAGEN_PERFIL, usuario.getImagen());
            prefsEditor.putString(NOMBRES, usuario.getNombres());
            prefsEditor.putString(APELLIDOS, usuario.getApellidos());
            prefsEditor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getDatos(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public Usuario getDatosUsuarios(String key) {
        Usuario usuario = new Usuario();
        if (sharedPreferences!= null) {
                usuario.setNombreUsuario(sharedPreferences.getString(USUARIO_APP, ""));
                usuario.setPassword(sharedPreferences.getString(PASS_APP, ""));
                usuario.setImagen(sharedPreferences.getString(IMAGEN_PERFIL, ""));
                usuario.setNombres(sharedPreferences.getString(NOMBRES, ""));
                usuario.setApellidos(sharedPreferences.getString(APELLIDOS, ""));
        }
        return usuario;
    }

    public boolean sesionIniciada(){
        boolean bandera = false;
        if (sharedPreferences!= null) {
            try {
                String usuario = sharedPreferences.getString(USUARIO_APP, "");
                if(!usuario.equals("")){
                    bandera = true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return bandera;
    }
}

package com.yarolegovich.slidingrootnav.sample.tools;

import android.content.Context;
import android.view.View;

import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.slidingrootnav.sample.R;

public class GenericAlerts {

    public void mensajeInfo(String titulo,String mensaje,Context ctx){
        new LovelyInfoDialog(ctx)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_enfermera)
                .setTitle(titulo)
                .setMessage(mensaje)
                .show();
    }

    public void mensajeUnaOpcion(String titulo, String mensaje, Context ctx){
        new LovelyStandardDialog(ctx)
                .setTopColorRes(R.color.colorPrimary)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_enfermera)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}

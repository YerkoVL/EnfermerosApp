package com.yarolegovich.slidingrootnav.sample.tools;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.yarolegovich.slidingrootnav.sample.R;

public class ViewHolderServicios extends RecyclerView.ViewHolder{
    public TextView tituloServicio;
    public ImageView imagenServicio;
    public TextView precioServicio;
    public TextView tiempoEstimadoServicio;

    public ViewHolderServicios(final View itemView) {
        super(itemView);
        tituloServicio = (TextView) itemView.findViewById(R.id.txtServicio);
        imagenServicio = (ImageView) itemView.findViewById(R.id.imgServicio);
        precioServicio = (TextView) itemView.findViewById(R.id.txtPrecio);
        tiempoEstimadoServicio = (TextView) itemView.findViewById(R.id.txtTiempoEstimado);

        itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // run scale animation and make it bigger
                    Animation anim = AnimationUtils.loadAnimation(view.getContext(),R.anim.scale_in_tv);
                    itemView.startAnimation(anim);
                    anim.setFillAfter(true);
                } else {
                    // run scale animation and make it smaller
                    Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_out_tv);
                    itemView.startAnimation(anim);
                    anim.setFillAfter(true);
                }
            }
        });
    }
}

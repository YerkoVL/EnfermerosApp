package com.yarolegovich.slidingrootnav.sample.tools;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.yarolegovich.slidingrootnav.sample.R;
import com.yarolegovich.slidingrootnav.sample.entity.ServiceDoctor;

import java.util.ArrayList;

public class AdapterServicios extends RecyclerView.Adapter<AdapterServicios.ViewHolderServicios>{

    private Animation animIn;
    private ArrayList<ServiceDoctor> lista;
    public RecyclerView recyclerView;
    public Integer previewPosition;
    public AdapterServicios(Activity activity, ArrayList<ServiceDoctor> Data, RecyclerView recyclerView) {
        lista = Data;
        this.recyclerView = recyclerView;
        previewPosition = -1;
        animIn = AnimationUtils.loadAnimation(activity, R.anim.scale_in_tv);
    }

    @Override
    public ViewHolderServicios onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_servicio, parent, false);
        ViewHolderServicios holder = new ViewHolderServicios(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderServicios holder, int position) {
        holder.tituloServicio.setText(lista.get(position).getServicio());
        holder.imagenServicio.setImageResource(lista.get(position).getImagen());
        holder.precioServicio.setText(String.valueOf(lista.get(position).getPrecio()));
        holder.tiempoEstimadoServicio.setText(lista.get(position).getTiempoEstimado());
    }

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

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            if (previewPosition == -1){
                                itemView.startAnimation(animIn);
                                previewPosition = getAdapterPosition();

                            } else if (previewPosition != getAdapterPosition()){
                                ViewHolderServicios holderServicios = (ViewHolderServicios) recyclerView.findViewHolderForAdapterPosition(previewPosition);
                                holderServicios.itemView.clearAnimation();

                                ViewHolderServicios holder = (ViewHolderServicios) recyclerView.findViewHolderForAdapterPosition(getAdapterPosition());
                                holder.itemView.startAnimation(animIn);
                                previewPosition = getAdapterPosition();
                            } else {
                                itemView.clearAnimation();
                                previewPosition = -1;
                            }
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}

package com.yarolegovich.slidingrootnav.sample.tools;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yarolegovich.slidingrootnav.sample.R;
import com.yarolegovich.slidingrootnav.sample.entity.ServiceDoctor;

import java.util.ArrayList;

public class AdapterServicios extends RecyclerView.Adapter<ViewHolderServicios>{

    private ArrayList<ServiceDoctor> lista;
    public AdapterServicios(ArrayList<ServiceDoctor> Data) {
        lista = Data;
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

    @Override
    public int getItemCount() {
        return lista.size();
    }
}

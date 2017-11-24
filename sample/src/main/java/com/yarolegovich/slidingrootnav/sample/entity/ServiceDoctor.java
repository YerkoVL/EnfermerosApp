package com.yarolegovich.slidingrootnav.sample.entity;

public class ServiceDoctor {
    int imagen;
    String servicio;
    double precio;
    String tiempoEstimado;

    public ServiceDoctor() {
    }

    public ServiceDoctor(int imagen, String servicio, double precio, String tiempoEstimado) {
        this.imagen = imagen;
        this.servicio = servicio;
        this.precio = precio;
        this.tiempoEstimado = tiempoEstimado;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(String tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }
}

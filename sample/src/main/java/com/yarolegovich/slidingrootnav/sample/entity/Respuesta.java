package com.yarolegovich.slidingrootnav.sample.entity;

public class Respuesta {
    String Mensaje;
    boolean Valor;
    String Bienvenida;

    public Respuesta() {
    }

    public Respuesta(String mensaje, boolean valor, String bienvenida) {
        Mensaje = mensaje;
        Valor = valor;
        Bienvenida = bienvenida;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public boolean getValor() {
        return Valor;
    }

    public void setValor(boolean valor) {
        Valor = valor;
    }

    public String getBienvenida() {
        return Bienvenida;
    }

    public void setBienvenida(String bienvenida) {
        Bienvenida = bienvenida;
    }
}

package com.yarolegovich.slidingrootnav.sample.entity;

public class Usuario {
    public String Id;
    public int IdUsuario;
    public int IdTipoDocumento;
    public int TipoDocumento;
    public int NroDocumento;
    public String Nombres;
    public String Apellidos;
    public String Correo;
    public int Telefono;
    public String Direccion;
    public String NombreUsuario;
    public String Password;
    public String Latitud;
    public String Longitud;
    public int IdPerfil;
    public String Perfil;
    public String IdDpto;
    public String Dpto;
    public int IdDistrito;
    public String Distrito;
    public String FechaLogueo;
    public float rating;
    public String imagen;
    public String Key;
    public int IdEstado;
    public String Desc_Estado;

    public Usuario() {
    }

    public Usuario(String id, int idUsuario, int idTipoDocumento, int tipoDocumento, int nroDocumento, String nombres, String apellidos, String correo, int telefono, String direccion, String nombreUsuario, String password, String latitud, String longitud, int idPerfil, String perfil, String idDpto, String dpto, int idDistrito, String distrito, String fechaLogueo, float rating, String imagen, String key, int idEstado, String desc_Estado) {
        Id = id;
        IdUsuario = idUsuario;
        IdTipoDocumento = idTipoDocumento;
        TipoDocumento = tipoDocumento;
        NroDocumento = nroDocumento;
        Nombres = nombres;
        Apellidos = apellidos;
        Correo = correo;
        Telefono = telefono;
        Direccion = direccion;
        NombreUsuario = nombreUsuario;
        Password = password;
        Latitud = latitud;
        Longitud = longitud;
        IdPerfil = idPerfil;
        Perfil = perfil;
        IdDpto = idDpto;
        Dpto = dpto;
        IdDistrito = idDistrito;
        Distrito = distrito;
        FechaLogueo = fechaLogueo;
        this.rating = rating;
        this.imagen = imagen;
        Key = key;
        IdEstado = idEstado;
        Desc_Estado = desc_Estado;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public int getIdTipoDocumento() {
        return IdTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        IdTipoDocumento = idTipoDocumento;
    }

    public int getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        TipoDocumento = tipoDocumento;
    }

    public int getNroDocumento() {
        return NroDocumento;
    }

    public void setNroDocumento(int nroDocumento) {
        NroDocumento = nroDocumento;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public int getTelefono() {
        return Telefono;
    }

    public void setTelefono(int telefono) {
        Telefono = telefono;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        NombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public int getIdPerfil() {
        return IdPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        IdPerfil = idPerfil;
    }

    public String getPerfil() {
        return Perfil;
    }

    public void setPerfil(String perfil) {
        Perfil = perfil;
    }

    public String getIdDpto() {
        return IdDpto;
    }

    public void setIdDpto(String idDpto) {
        IdDpto = idDpto;
    }

    public String getDpto() {
        return Dpto;
    }

    public void setDpto(String dpto) {
        Dpto = dpto;
    }

    public int getIdDistrito() {
        return IdDistrito;
    }

    public void setIdDistrito(int idDistrito) {
        IdDistrito = idDistrito;
    }

    public String getDistrito() {
        return Distrito;
    }

    public void setDistrito(String distrito) {
        Distrito = distrito;
    }

    public String getFechaLogueo() {
        return FechaLogueo;
    }

    public void setFechaLogueo(String fechaLogueo) {
        FechaLogueo = fechaLogueo;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public int getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(int idEstado) {
        IdEstado = idEstado;
    }

    public String getDesc_Estado() {
        return Desc_Estado;
    }

    public void setDesc_Estado(String desc_Estado) {
        Desc_Estado = desc_Estado;
    }
}

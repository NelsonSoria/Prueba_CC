package com.example.prueba_centrocomercial.Entidades;

public class Tienda {
    private String id;
    private String nombre;
    private String descripcionUbicacion;
    private String horario;
    private String urlLogo;
    private String telefono;
    private String idCentroComercial;

    private String idUsuarioEncargado;


    public Tienda() {
        // Constructor vacío requerido por Firebase
    }

    public Tienda(String id, String nombre, String descripcionUbicacion, String horario, String urlLogo, String telefono, String idCentroComercial,String idUsuarioEncargado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcionUbicacion = descripcionUbicacion;
        this.horario = horario;
        this.urlLogo = urlLogo;
        this.telefono = telefono;
        this.idCentroComercial = idCentroComercial;
        this.idUsuarioEncargado=idUsuarioEncargado;
    }

    public String getIdUsuarioEncargado() {
        return idUsuarioEncargado;
    }

    public void setIdUsuarioEncargado(String idUsuarioEncargado) {
        this.idUsuarioEncargado = idUsuarioEncargado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcionUbicacion() {
        return descripcionUbicacion;
    }

    public void setDescripcionUbicacion(String descripcionUbicacion) {
        this.descripcionUbicacion = descripcionUbicacion;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIdCentroComercial() {
        return idCentroComercial;
    }

    public void setIdCentroComercial(String idCentroComercial) {
        this.idCentroComercial = idCentroComercial;
    }
}

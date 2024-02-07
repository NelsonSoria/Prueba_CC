package com.example.prueba_centrocomercial;

public class CentroComercial {



    private String id;
    private String nombre;
    private String ubicacion;
    private String logoUrl;

    public CentroComercial() {
        // Constructor vacío requerido por Firebase Realtime Database
    }

    public CentroComercial(String nombre, String ubicacion, String logoUrl) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.logoUrl = logoUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

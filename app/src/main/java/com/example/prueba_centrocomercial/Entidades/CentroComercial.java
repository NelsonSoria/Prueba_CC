package com.example.prueba_centrocomercial.Entidades;

public class CentroComercial {



    private String id;
    private String nombre;
    private String ubicacion;
    private double latitud;
    private double longitud;
    private String logoUrl;
    private String imagInfraEstructuraUrl;
    private String idUsuarioPermitido;

    public CentroComercial() {

    }

    public CentroComercial(String id, String nombre, String ubicacion, double latitud, double longitud, String logoUrl, String imagInfraEstructuraUrl, String idUsuarioPermitido) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.logoUrl = logoUrl;
        this.imagInfraEstructuraUrl = imagInfraEstructuraUrl;
        this.idUsuarioPermitido = idUsuarioPermitido;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getImagInfraEstructuraUrl() {
        return imagInfraEstructuraUrl;
    }

    public void setImagInfraEstructuraUrl(String imagInfraEstructuraUrl) {
        this.imagInfraEstructuraUrl = imagInfraEstructuraUrl;
    }

    public String getIdUsuarioPermitido() {
        return idUsuarioPermitido;
    }

    public void setIdUsuarioPermitido(String idUsuarioPermitido) {
        this.idUsuarioPermitido = idUsuarioPermitido;
    }
}

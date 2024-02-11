package com.example.prueba_centrocomercial.Entidades;

public class Producto {
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String imagenURL;
    private boolean disponible;
    private int cantidadStock;
    private String idTienda;
    private String idUsuarioEncargado;

    public Producto() {
        // Constructor vacío requerido por Firebase
    }

    public Producto( String nombre, String descripcion, double precio, String imagenURL, boolean disponible, int cantidadStock, String idTienda, String idUsuarioEncargado) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenURL = imagenURL;
        this.disponible = disponible;
        this.cantidadStock = cantidadStock;
        this.idTienda = idTienda;
        this.idUsuarioEncargado = idUsuarioEncargado;
    }

    // Getters y setters
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public int getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(int cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getIdUsuarioEncargado() {
        return idUsuarioEncargado;
    }

    public void setIdUsuarioEncargado(String idUsuarioEncargado) {
        this.idUsuarioEncargado = idUsuarioEncargado;
    }
}


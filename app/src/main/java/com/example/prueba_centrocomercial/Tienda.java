package com.example.prueba_centrocomercial;

public class Tienda {
    private String id;
    private String nombre;
    private String descripcion;

    private String telefono;
    // Puedes agregar más atributos según sea necesario, como ubicación, horario, etc.

    public Tienda() {
        // Constructor vacío requerido por Firebase
    }

    public Tienda(String id, String nombre, String descripcion,String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.telefono = telefono;
    }

    // Getters y setters

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

package com.example.prueba_centrocomercial;

public class User {
    public String nombre;
    public String apellido;
    public String email;

    public String rol;

    // Constructor vacío requerido por Firebase Realtime Database
    public User() {
    }

    public User(String nombre, String apellido, String email, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
    }
}


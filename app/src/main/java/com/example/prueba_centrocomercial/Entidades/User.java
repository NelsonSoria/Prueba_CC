package com.example.prueba_centrocomercial.Entidades;

public class User {

    public String email;
    public String rol;

    // Constructor vacío requerido por Firebase Realtime Database
    public User() {
    }

    public User( String email, String rol) {
        this.email = email;
        this.rol = rol;
    }
}


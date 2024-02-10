package com.example.prueba_centrocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Agregar_Tienda extends AppCompatActivity {
    private EditText etNombreTienda;
    private EditText etDescripcionTienda;

    private EditText etTelefonoTienda;
    private Button btnAgregarTienda;

    private DatabaseReference tiendasRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tienda);
        etNombreTienda = findViewById(R.id.etNombreTienda);
        etDescripcionTienda = findViewById(R.id.etDescripcionTienda);
        etTelefonoTienda=findViewById(R.id.etTelefonoTienda);
        btnAgregarTienda = findViewById(R.id.btnCrearTienda);

        tiendasRef = FirebaseDatabase.getInstance().getReference().child("tiendas");

        btnAgregarTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarTienda();
            }
        });
    }

    private void agregarTienda() {
        String nombreTienda = etNombreTienda.getText().toString().trim();
        String descripcionTienda = etDescripcionTienda.getText().toString().trim();
        String telefonoTienda = etTelefonoTienda.getText().toString().trim(); // Obtener el teléfono

        // Validar que los campos no estén vacíos
        if (nombreTienda.isEmpty() || descripcionTienda.isEmpty() || telefonoTienda.isEmpty()) {
            Toast.makeText(Agregar_Tienda.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el ID del usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Generar una nueva clave para la tienda
            String tiendaId = tiendasRef.push().getKey();

            // Crear un objeto Tienda con los datos proporcionados
            Tienda tienda = new Tienda(tiendaId, nombreTienda, descripcionTienda, telefonoTienda); // Pasar el teléfono

            // Guardar la tienda en la base de datos
            tiendasRef.child(tiendaId).setValue(tienda).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Agregar_Tienda.this, "Tienda agregada correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // Cerrar la actividad después de agregar la tienda
                } else {
                    Toast.makeText(Agregar_Tienda.this, "Error al agregar la tienda", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // El usuario no está autenticado, mostrar mensaje de error o redirigir a pantalla de inicio de sesión
            Toast.makeText(Agregar_Tienda.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}

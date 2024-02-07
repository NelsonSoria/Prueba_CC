package com.example.prueba_centrocomercial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_add_user extends AppCompatActivity {

    private EditText etNombre, etApellido, etEmail, etContraseña, etRol;
    private Button btnAgregarUsuario;

    // Referencia a la base de datos de Firebase
    private DatabaseReference databaseReference;
    // Objeto FirebaseAuth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEmail = findViewById(R.id.etEmail);
        etContraseña = findViewById(R.id.etContraseña);
        etRol = findViewById(R.id.etRol);
        btnAgregarUsuario = findViewById(R.id.btnAgregarUsuario);

        // Inicializa la referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        // Inicializa el objeto FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        btnAgregarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = etNombre.getText().toString().trim();
                String apellido = etApellido.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String contraseña = etContraseña.getText().toString().trim();
                String rol = etRol.getText().toString().trim();

                if (!nombre.isEmpty() && !apellido.isEmpty() && !email.isEmpty() && !contraseña.isEmpty() && !rol.isEmpty()) {
                    // Crea un nuevo usuario en Firebase Authentication
                    firebaseAuth.createUserWithEmailAndPassword(email, contraseña)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Crea un nuevo usuario
                                        User newUser = new User(nombre, apellido, email, rol);

                                        // Agrega el nuevo usuario a Firebase Realtime Database
                                        addUserToFirebase(newUser);

                                    } else {
                                        showToast("Error al agregar usuario: " + task.getException().getMessage());
                                    }
                                }
                            });
                } else {
                    showToast("Por favor, completa todos los campos");
                }
            }
        });
    }

    private void addUserToFirebase(User newUser) {
        // Obtiene el UID del usuario recién registrado en Firebase Authentication
        String uid = firebaseAuth.getCurrentUser().getUid();

        // Almacena el nuevo usuario en la base de datos asociándolo con su UID
        databaseReference.child(uid).setValue(newUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showToast("Usuario agregado con éxito");
                            Intent intent = new Intent(activity_add_user.this, MainActivity.class);
                            intent.putExtra("SHOW_ADD_USER_BUTTON", true);
                            startActivity(intent);
                            finish();
                        } else {
                            showToast("Error al agregar usuario en la base de datos: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

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

public class activity_log extends AppCompatActivity {
    EditText etEmail;
    EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etContraseñaLogin);

        btnLogin = findViewById(R.id.btnIniciarSesion);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Verifica que ambos campos no estén vacíos
                if (!email.isEmpty() && !password.isEmpty()) {
                    // Inicia sesión con Firebase Auth
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(activity_log.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Inicio de sesión exitoso
                                        // Abre la nueva actividad para agregar un nuevo usuario
                                        Intent intent = new Intent(activity_log.this, MainActivity.class);
                                        // Indica que debe mostrar el botón de agregar usuario
                                        intent.putExtra("SHOW_ADD_USER_BUTTON", true);
                                        startActivity(intent);
                                        finish(); // Opcional: cierra la actividad actual si lo deseas
                                    } else {
                                        // Si el inicio de sesión falla, muestra un mensaje de error
                                        Toast.makeText(activity_log.this, "Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // Muestra un mensaje si algún campo está vacío
                    Toast.makeText(activity_log.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(activity_log.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

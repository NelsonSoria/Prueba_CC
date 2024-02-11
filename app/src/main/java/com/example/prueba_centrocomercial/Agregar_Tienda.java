package com.example.prueba_centrocomercial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.prueba_centrocomercial.Entidades.CentroComercial;
import com.example.prueba_centrocomercial.Entidades.OnUserAddedListener;
import com.example.prueba_centrocomercial.Entidades.Tienda;
import com.example.prueba_centrocomercial.Entidades.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Agregar_Tienda extends AppCompatActivity {
    private EditText etNombreTienda;
    private EditText etDescripcionUbiTienda;

    private EditText etTelefonoTienda,etHorario,etUrlLogo, editTextCorreo, editTextContrasena;
    private Button btnAgregarTienda;

    private DatabaseReference tiendasRef,databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tienda);
        etNombreTienda = findViewById(R.id.etNombreTienda);
        etDescripcionUbiTienda = findViewById(R.id.etDescripcionUbicacionTienda);
        etHorario=findViewById(R.id.ethorario);
        etUrlLogo=findViewById(R.id.eturlLogo);
        etTelefonoTienda=findViewById(R.id.ettelefono);
        btnAgregarTienda = findViewById(R.id.btnCrearTienda);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextContrasena = findViewById(R.id.editTextContrasena);

        RadioButton radioButtonSi = findViewById(R.id.radioButtonSi);
        RadioButton radioButtonNo = findViewById(R.id.radioButtonNo);
        LinearLayout layoutCamposRegistro = findViewById(R.id.layoutCamposRegistro);

        tiendasRef = FirebaseDatabase.getInstance().getReference().child("tiendas");
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        radioButtonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonSi.isChecked()) {
                    layoutCamposRegistro.setVisibility(View.VISIBLE);
                    editTextCorreo.setVisibility(View.VISIBLE); // Asegúrate de que el campo de correo esté visible cuando se selecciona "No"
                    editTextContrasena.setVisibility(View.GONE); // Asegúrate de que el campo de contraseña esté visible cuando se selecciona "Sí"
                }
            }
        });

        radioButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonNo.isChecked()) {
                    // Oculta el campo de contraseña cuando se selecciona "No"
                    layoutCamposRegistro.setVisibility(View.VISIBLE);
                    editTextCorreo.setVisibility(View.VISIBLE); // Asegúrate de que el campo de correo esté visible cuando se selecciona "Sí"
                    editTextContrasena.setVisibility(View.VISIBLE);
                }
            }
        });

        btnAgregarTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarTienda();
                Intent intent = new Intent(Agregar_Tienda.this, MainActivity.class);
                intent.putExtra("SHOW_ADD_USER_BUTTON", true);
                startActivity(intent);
                finish();
            }
        });
    }


    private void agregarTienda() {
        String nombreTienda = etNombreTienda.getText().toString().trim();
        String descripcionUbiTienda = etDescripcionUbiTienda.getText().toString().trim();
        String horario= etHorario.getText().toString().trim();
        String urlogo=etUrlLogo.getText().toString().trim();
        String telefonoTienda = etTelefonoTienda.getText().toString().trim();
        String email = editTextCorreo.getText().toString().trim();
        String contrasenia = editTextContrasena.getText().toString().trim();
        firebaseAuth = FirebaseAuth.getInstance();




        if (!TextUtils.isEmpty(nombreTienda) && !TextUtils.isEmpty(descripcionUbiTienda) && !TextUtils.isEmpty(horario)
                && !TextUtils.isEmpty(urlogo) && !TextUtils.isEmpty(telefonoTienda) && !TextUtils.isEmpty(email)) {

            if (!contrasenia.isEmpty()) {
                // Si se proporciona una contraseña, crea un nuevo usuario y luego agrega el centro comercial
                firebaseAuth.createUserWithEmailAndPassword(email, contrasenia)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String rol = "encargado";
                                    User newUser = new User(email, rol);

                                    // Agrega el nuevo usuario a Firebase Realtime Database y luego agrega el centro comercial
                                    addUserToFirebase(newUser, new OnUserAddedListener() {
                                        @Override
                                        public void onUserAdded(String userId) {
                                            // Llama a este método cuando el usuario se haya agregado correctamente
                                            String id = tiendasRef.push().getKey();
                                            String useId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            String CentroComercial = getIntent().getStringExtra("idTienda");
                                            Tienda tienda = new Tienda(id, nombreTienda, descripcionUbiTienda,horario,urlogo ,telefonoTienda,CentroComercial,useId); // Pasar el teléfono

                                            // Agrega el nuevo centro comercial a la base de datos
                                            tiendasRef.child(id).setValue(tienda);

                                            // Limpia los campos después de agregar
                                            etNombreTienda.setText("");
                                            etDescripcionUbiTienda.setText("");
                                            etHorario.setText("");
                                            etUrlLogo.setText("");
                                            etTelefonoTienda.setText("");
                                            editTextCorreo.setText("");
                                            editTextContrasena.setText("");


                                            // Muestra un mensaje de éxito
                                            Toast.makeText(Agregar_Tienda.this, "Tienda agregado con éxito", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    showToast("Error al agregar usuario: " + task.getException().getMessage());
                                }
                            }
                        });

            } else {
                // Si no se proporciona una contraseña, busca la ID del usuario por correo electrónico
                Log.e("activity_agregar_centro_comercial", "SOY EL email: " + email);
                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                                        // El correo electrónico está registrado, obtén el UID del usuario actualmente autenticado
                                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        // Agrega el nuevo centro comercial a la base de datos
                                        String id = tiendasRef.push().getKey();
                                        String CentroComercial = getIntent().getStringExtra("idTienda");
                                        Tienda tienda = new Tienda(id, nombreTienda, descripcionUbiTienda,horario,urlogo ,telefonoTienda,CentroComercial,userId); // Pasar el teléfono

                                        // Agrega el nuevo centro comercial a la base de datos
                                        tiendasRef.child(id).setValue(tienda);

                                        // Limpia los campos después de agregar
                                        etNombreTienda.setText("");
                                        etDescripcionUbiTienda.setText("");
                                        etHorario.setText("");
                                        etUrlLogo.setText("");
                                        etTelefonoTienda.setText("");
                                        editTextCorreo.setText("");
                                        editTextContrasena.setText("");

                                        // Muestra un mensaje de éxito
                                        Toast.makeText(Agregar_Tienda.this, "Tienda agregado con éxito", Toast.LENGTH_SHORT).show();
                                    } else {
                                        showToast("No hay ninguna cuenta asociada a este correo electrónico");
                                    }
                                } else {
                                    showToast("Error al buscar métodos de inicio de sesión para el correo electrónico: " + task.getException().getMessage());
                                }
                            }
                        });
            }

        } else {
            // Muestra un mensaje de error si algún campo está vacío
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void addUserToFirebase(User newUser, OnUserAddedListener listener) {
        // Obtiene el UID del usuario recién registrado en Firebase Authentication
        String uid = firebaseAuth.getCurrentUser().getUid();

        // Almacena el nuevo usuario en la base de datos asociándolo con su UID
        databaseReference.child(uid).setValue(newUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showToast("Usuario agregado con éxito");
                            // Llama al método onUserAdded de la interfaz de callback
                            listener.onUserAdded(uid); // Pasa la ID al código que llama al método
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

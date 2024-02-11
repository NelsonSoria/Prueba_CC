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
import com.example.prueba_centrocomercial.Entidades.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_agregar_centro_comercial extends AppCompatActivity {

    private EditText etNombre, etUbicacion, etLogoUrl, etlatitud, etlongitud, etimagInfraEstructuraUrl, editTextCorreo, editTextContrasena;
    private Button btnAgregarCentroComercial;
    private DatabaseReference centrosComercialesRef, databaseReference;
    private FirebaseAuth firebaseAuth;


    double latitud=0;
    double longitud=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_centro_comercial);

        etNombre = findViewById(R.id.etNombre);
        etUbicacion = findViewById(R.id.etUbicacion);
        etlatitud = findViewById(R.id.etlatitud);
        etlongitud = findViewById(R.id.etlongitud);
        etLogoUrl=findViewById(R.id.etLogoUrl);
        etimagInfraEstructuraUrl = findViewById(R.id.etimagInfraEstructuraUrl);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextContrasena = findViewById(R.id.editTextContrasena);
        btnAgregarCentroComercial = findViewById(R.id.btnAgregarCentroComercial);

        RadioButton radioButtonSi = findViewById(R.id.radioButtonSi);
        RadioButton radioButtonNo = findViewById(R.id.radioButtonNo);
        LinearLayout layoutCamposRegistro = findViewById(R.id.layoutCamposRegistro);



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


        centrosComercialesRef = FirebaseDatabase.getInstance().getReference("centrosComerciales");
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        btnAgregarCentroComercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarCentroComercial();
                Intent intent = new Intent(activity_agregar_centro_comercial.this, MainActivity.class);
                intent.putExtra("SHOW_ADD_USER_BUTTON", true);
                startActivity(intent);
                finish();
            }
        });
    }

    private void agregarCentroComercial() {
        String nombre = etNombre.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();
        String latitudaux = etlatitud.getText().toString().trim();
        String longitudaux = etlongitud.getText().toString().trim();
        String logoUrl = etLogoUrl.getText().toString().trim();
        String imgInfra = etimagInfraEstructuraUrl.getText().toString().trim();
        String email = editTextCorreo.getText().toString().trim();
        String contrasenia = editTextContrasena.getText().toString().trim();
        firebaseAuth = FirebaseAuth.getInstance();



        if (!TextUtils.isEmpty(latitudaux)) {
            try {
                latitud = Double.parseDouble(latitudaux);
            } catch (NumberFormatException e) {
                showToast("Por favor, ingresa una latitud válida");
                return; // Salir del método si la conversión falla
            }
        } else {
            showToast("Por favor, ingresa la latitud");
            return; // Salir del método si la latitud está vacía
        }

        // Validar y convertir la longitud
        if (!TextUtils.isEmpty(longitudaux)) {
            try {
                longitud = Double.parseDouble(longitudaux);
            } catch (NumberFormatException e) {
                showToast("Por favor, ingresa una longitud válida");
                return; // Salir del método si la conversión falla
            }
        } else {
            showToast("Por favor, ingresa la longitud");
            return; // Salir del método si la longitud está vacía
        }


        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(ubicacion) && !TextUtils.isEmpty(logoUrl)) {

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
                                            String id = centrosComercialesRef.push().getKey();
                                            CentroComercial centroComercial = new CentroComercial(id, nombre, ubicacion, latitud, longitud, logoUrl, imgInfra, userId);

                                            // Agrega el nuevo centro comercial a la base de datos
                                            centrosComercialesRef.child(id).setValue(centroComercial);

                                            // Limpia los campos después de agregar
                                            etNombre.setText("");
                                            etUbicacion.setText("");
                                            etLogoUrl.setText("");
                                            etlatitud.setText("");
                                            etlongitud.setText("");
                                            etimagInfraEstructuraUrl.setText("");

                                            // Muestra un mensaje de éxito
                                            Toast.makeText(activity_agregar_centro_comercial.this, "Centro Comercial agregado con éxito", Toast.LENGTH_SHORT).show();
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
                                        String id = centrosComercialesRef.push().getKey();
                                        CentroComercial centroComercial = new CentroComercial(id, nombre, ubicacion, latitud, longitud, logoUrl, imgInfra, userId);
                                        centrosComercialesRef.child(id).setValue(centroComercial);

                                        // Limpia los campos después de agregar
                                        etNombre.setText("");
                                        etUbicacion.setText("");
                                        etLogoUrl.setText("");
                                        etlatitud.setText("");
                                        etlongitud.setText("");
                                        etimagInfraEstructuraUrl.setText("");

                                        // Muestra un mensaje de éxito
                                        Toast.makeText(activity_agregar_centro_comercial.this, "Centro Comercial agregado con éxito", Toast.LENGTH_SHORT).show();
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



package com.example.prueba_centrocomercial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_agregar_centro_comercial extends AppCompatActivity {

    private EditText etNombre, etUbicacion, etLogoUrl;
    private Button btnAgregarCentroComercial;
    private DatabaseReference centrosComercialesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_centro_comercial);

        etNombre = findViewById(R.id.etNombre);
        etUbicacion = findViewById(R.id.etUbicacion);
        etLogoUrl = findViewById(R.id.etLogoUrl);
        btnAgregarCentroComercial = findViewById(R.id.btnAgregarCentroComercial);

        centrosComercialesRef = FirebaseDatabase.getInstance().getReference("centrosComerciales");

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
        String logoUrl = etLogoUrl.getText().toString().trim();

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(ubicacion) && !TextUtils.isEmpty(logoUrl)) {
            // Crea un nuevo objeto CentroComercial
            CentroComercial centroComercial = new CentroComercial(nombre, ubicacion, logoUrl);

            // Agrega el nuevo centro comercial a la base de datos
            centrosComercialesRef.push().setValue(centroComercial);

            // Limpia los campos después de agregar
            etNombre.setText("");
            etUbicacion.setText("");
            etLogoUrl.setText("");

            // Muestra un mensaje de éxito
            Toast.makeText(this, "Centro Comercial agregado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            // Muestra un mensaje de error si algún campo está vacío
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

}

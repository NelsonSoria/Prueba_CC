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

import com.example.prueba_centrocomercial.Entidades.Producto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgregarProducto extends AppCompatActivity {

    private EditText etNombreProducto;
    private EditText etDescripcionProducto;
    private EditText etPrecioProducto;
    private EditText etCantidadStock;
    private EditText etImagenURL;
    private EditText etDisponibilidad;
    private Button btnAgregarProducto;
    private DatabaseReference productosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        // Inicializar la referencia a la base de datos de productos
        productosRef = FirebaseDatabase.getInstance().getReference().child("productos");

        // Vincular los EditText y el botón desde el layout
        etNombreProducto = findViewById(R.id.etNombreProducto);
        etDescripcionProducto = findViewById(R.id.etDescripcionProducto);
        etPrecioProducto = findViewById(R.id.etPrecioProducto);
        etCantidadStock = findViewById(R.id.etCantidadStock);
        etImagenURL = findViewById(R.id.etImagenURL);
        etDisponibilidad = findViewById(R.id.etDisponibilidad);
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto);

        // Configurar el OnClickListener para el botón de agregar producto
        btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarProducto();
                Intent intent = new Intent(AgregarProducto.this, MainActivity.class);
                intent.putExtra("SHOW_ADD_USER_BUTTON", true);
                startActivity(intent);
                finish();
            }
        });
    }

    private void agregarProducto() {
        // Obtener los valores de los EditText
        String nombre = etNombreProducto.getText().toString().trim();
        String descripcion = etDescripcionProducto.getText().toString().trim();
        double precio = Double.parseDouble(etPrecioProducto.getText().toString().trim());
        int cantidadStock = Integer.parseInt(etCantidadStock.getText().toString().trim());
        String imagenURL = etImagenURL.getText().toString().trim();
        boolean disponible = etDisponibilidad.getText().toString().trim().equalsIgnoreCase("si");

        // Verificar si los campos obligatorios no están vacíos
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(descripcion) || TextUtils.isEmpty(imagenURL)) {
            Toast.makeText(this, "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        String idTienda = getIntent().getStringExtra("idTienda");
        String idUSuario = getIntent().getStringExtra("idUsuario");

        // Crear un nuevo objeto Producto
        Producto producto = new Producto(nombre, descripcion, precio, imagenURL, disponible, cantidadStock, idTienda, idUSuario);

        // Generar una clave única para el nuevo producto en la base de datos
        String productoId = productosRef.push().getKey();

        // Guardar el nuevo producto en la base de datos usando la clave generada
        productosRef.child(productoId).setValue(producto)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Producto agregado con éxito
                            Toast.makeText(AgregarProducto.this, "Producto agregado correctamente", Toast.LENGTH_SHORT).show();
                            // Limpiar los campos de entrada después de agregar el producto
                            limpiarCampos();
                        } else {
                            // Error al agregar el producto
                            Toast.makeText(AgregarProducto.this, "Error al agregar el producto", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void limpiarCampos() {
        etNombreProducto.setText("");
        etDescripcionProducto.setText("");
        etPrecioProducto.setText("");
        etCantidadStock.setText("");
        etImagenURL.setText("");
        etDisponibilidad.setText("");
    }
}

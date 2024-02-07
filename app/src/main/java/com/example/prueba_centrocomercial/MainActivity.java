package com.example.prueba_centrocomercial;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin,btnAgregarUsuario,btnCerrarSesion,btnAgregarCentroC;

    private RecyclerView recyclerView;
    private CentroComercialAdapter centroComercialAdapter;
    private DatabaseReference centrosComercialesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnAgregarUsuario = findViewById(R.id.btnAgregarUsuario);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnAgregarCentroC=findViewById(R.id.btnAgregarCentroC);

        ////HOLA

        recyclerView = findViewById(R.id.recyclerView);

        // Configurar el diseño del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener una referencia a la base de datos de Firebase
        centrosComercialesRef = FirebaseDatabase.getInstance().getReference().child("centrosComerciales");

        // Crear una lista de centros comerciales desde Firebase Realtime Database
        obtenerListaDeCentrosComerciales();

        // Crear un adaptador y establecerlo en el RecyclerView
        centroComercialAdapter = new CentroComercialAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(centroComercialAdapter);

        centroComercialAdapter.setOnItemClickListener(new CentroComercialAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CentroComercial centroComercial) {
                // Lógica para manejar el clic en un elemento del RecyclerView
                // Por ejemplo, iniciar una nueva actividad o fragmento con detalles del centro comercial
                mostrarDetalleCentroComercial(centroComercial);
            }
        });


        //FIN


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            Log.d("MainActivity", "Usuario autenticado: " + currentUser.getUid());
            // Crea una instancia de la interfaz UserRoleCallback
            UserRoleCallback userRoleCallback = new UserRoleCallback() {
                @Override
                public void onUserRoleReceived(String userRole) {
                    btnAgregarUsuario.setVisibility(View.VISIBLE);
                    btnCerrarSesion.setVisibility(View.VISIBLE);
                    btnLogin.setVisibility(View.GONE);
                    Log.d("MainActivity", "Rol del usuario: " + userRole);
                    // Si el usuario es dueño o jefe, muestra los botones correspondientes
                    if ("empleado".equals(userRole)){
                        btnAgregarUsuario.setVisibility(View.GONE);
                        btnCerrarSesion.setVisibility(View.VISIBLE);
                        btnLogin.setVisibility(View.GONE);
                        Log.d("MainActivity", "SoyEmpleado" );
                    }
                    if ("gerente".equals(userRole)) {
                        btnAgregarUsuario.setVisibility(View.GONE);
                        btnCerrarSesion.setVisibility(View.VISIBLE);
                        btnLogin.setVisibility(View.GONE);
                    } else {
                        // Si no es dueño ni jefe, oculta los botones de agregar usuario y cerrar sesión

                    }
                }
            };

            // Llama a getUserRoleFromFirebase con la instancia de UserRoleCallback
            getUserRoleFromFirebase(currentUser.getUid(), userRoleCallback);
        } else {
            // Si el usuario no está autenticado, puedes manejarlo según tus requisitos
        }

        // Resto del código...
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_log.class);
                startActivity(intent);
            }
        });
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "Botón de cerrar sesión presionado");
                // Cierra la sesión con Firebase Auth
                FirebaseAuth.getInstance().signOut();

                // Oculta los botones de agregar usuario y cerrar sesión, muestra el de inicio de sesión
                btnAgregarUsuario.setVisibility(View.GONE);
                btnCerrarSesion.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);

                // Muestra un mensaje de cierre de sesión
                Toast.makeText(MainActivity.this, "Cierre de sesión exitoso", Toast.LENGTH_SHORT).show();
            }
        });
        
        
        btnAgregarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Botón de agregar usuario presionado");
                Intent intent = new Intent(MainActivity.this, activity_add_user.class);
                startActivity(intent);
            }
        });

        btnAgregarCentroC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Botón de agregar centro Comercial presionado");
                Intent intent = new Intent(MainActivity.this, activity_agregar_centro_comercial.class);
                startActivity(intent);
            }
        });
    }

    private void getUserRoleFromFirebase(String userId, UserRoleCallback userRoleCallback) {
        Log.d("MainActivity", "Obteniendo rol del usuario desde Firebase");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("MainActivity", "Información del DataSnapshot: " + dataSnapshot.toString());
                    String userRole = dataSnapshot.child("rol").getValue(String.class);
                    Log.d("MainActivity", "Rol del usuario obtenido: " + userRole);

                    // Llama al método de devolución de llamada con l rol del usuario
                    userRoleCallback.onUserRoleReceived(userRole);
                } else {
                    Log.d("MainActivity", "El nodo del usuario no existe");
                    // El nodo del usuario no existe, puedes manejarlo según tus necesidades
                    // Llama al método de devolución de llamada con un valor predeterminado o maneja el caso vacío
                    userRoleCallback.onUserRoleReceived("default");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", "Error al leer la base de datos: " + databaseError.getMessage());
                // Manejar errores de lectura de la base de datos
                // Llama al método de devolución de llamada con un valor predeterminado o maneja el error
                userRoleCallback.onUserRoleReceived("default");
            }
        });

    }
    private void obtenerListaDeCentrosComerciales() {
        centrosComercialesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CentroComercial> centroComercialList = new ArrayList<>();

                List<String> keys = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String centroComercialId = snapshot.getKey(); // Obtener la clave (key)
                    CentroComercial centroComercial = snapshot.getValue(CentroComercial.class);

                    centroComercialList.add(centroComercial);
                    keys.add(centroComercialId.toString());

                }


                centroComercialAdapter.setCentroComercialList(centroComercialList);
                centroComercialAdapter.setCentroComercialIds(keys); // Establecer el mapa en el adaptador


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", "Error al obtener datos de centros comerciales", databaseError.toException());
            }
        });
    }


    private void mostrarDetalleCentroComercial(CentroComercial centroComercial) {
        // Oculta la lista (RecyclerView)
        recyclerView.setVisibility(View.GONE);

        // Crea una instancia del Fragment de detalle y pasa la información del centro comercial
        CentroComercialDetalleFragment detalleFragment = CentroComercialDetalleFragment.newInstance(centroComercial);

        // Reemplaza el contenido del contenedor principal con el Fragment de detalle
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, detalleFragment)
                .addToBackStack(null)
                .commit();
    }

}

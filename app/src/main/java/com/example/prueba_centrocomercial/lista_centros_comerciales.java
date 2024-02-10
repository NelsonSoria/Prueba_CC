package com.example.prueba_centrocomercial;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class lista_centros_comerciales extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView recyclerView;
    private CentroComercialAdapter centroComercialAdapter;
    private DatabaseReference centrosComercialesRef;


    public lista_centros_comerciales() {

    }


    public static lista_centros_comerciales newInstance(String param1, String param2) {
        lista_centros_comerciales fragment = new lista_centros_comerciales();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lista_centros_comerciales", "Fragmento en el layout: lista_centros_comerciales");
        View view = inflater.inflate(R.layout.fragment_lista_centros_comerciales, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        // Configurar el diseño del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtener una referencia a la base de datos de Firebase
        centrosComercialesRef = FirebaseDatabase.getInstance().getReference().child("centrosComerciales");

        // Crear una lista de centros comerciales desde Firebase Realtime Database
        obtenerListaDeCentrosComerciales();

        // Crear un adaptador y establecerlo en el RecyclerView
        centroComercialAdapter = new CentroComercialAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(centroComercialAdapter);

        centroComercialAdapter.setOnItemClickListener(new CentroComercialAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CentroComercial centroComercial) {
                // Lógica para manejar el clic en un elemento del RecyclerView
                // Por ejemplo, iniciar una nueva actividad o fragmento con detalles del centro comercial
                mostrarDetalleCentroComercial(centroComercial);
            }
        });

        // Devuelve la vista inflada
        return view;
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
                Log.e("lista_centros_comerciales", "Error al obtener datos de centros comerciales", databaseError.toException());
            }
        });
    }
    private void mostrarDetalleCentroComercial(CentroComercial centroComercial) {
        // Oculta la lista (RecyclerView)
        recyclerView.setVisibility(View.GONE);

        // Crea una instancia del Fragment de detalle y pasa la información del centro comercial
        CentroComercialDetalleFragment detalleFragment = CentroComercialDetalleFragment.newInstance(centroComercial);

        // Reemplaza el contenido del contenedor principal con el Fragment de detalle
        getParentFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, detalleFragment)
                .addToBackStack(null)
                .commit();
    }
}
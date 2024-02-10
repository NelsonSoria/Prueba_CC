package com.example.prueba_centrocomercial;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CentroComercialDetalleFragment extends Fragment {

    private TextView tvMallName;
    private TextView tvLocation;
    private ImageView ivLogo;
    private RecyclerView recyclerView;
    private TiendaAdapter tiendaAdapter;
    private DatabaseReference TiendasRef; // Referencia a la base de datos de las tiendas

    public CentroComercialDetalleFragment() {
        // Constructor vacío requerido
    }

    public static CentroComercialDetalleFragment newInstance(CentroComercial centroComercial) {
        CentroComercialDetalleFragment fragment = new CentroComercialDetalleFragment();
        Bundle args = new Bundle();
        args.putString("mallName", centroComercial.getNombre());
        args.putString("location", centroComercial.getUbicacion());
        args.putString("logoUrl", centroComercial.getLogoUrl());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("CentroComercialDetalleFragment", "Fragmento en el layout: fragment_centro_comercial_detalle");
        View view = inflater.inflate(R.layout.fragment_centro_comercial_detalle, container, false);
        tvMallName = view.findViewById(R.id.tvMallNameDetalle);
        tvLocation = view.findViewById(R.id.tvLocationDetalle);
        ivLogo = view.findViewById(R.id.ivLogoDetalle);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Inicializar la referencia a la base de datos de las tiendas
        TiendasRef = FirebaseDatabase.getInstance().getReference().child("tiendas");

        Bundle args = getArguments();
        if (args != null) {
            String mallName = args.getString("mallName", "");
            String location = args.getString("location", "");
            String logoUrl = args.getString("logoUrl", "");

            tvMallName.setText(mallName);
            tvLocation.setText(location);
            Picasso.get().load(logoUrl).into(ivLogo);
        }

        // Configurar el RecyclerView y el adaptador
        tiendaAdapter = new TiendaAdapter(new ArrayList<>(), getContext()); // Inicializar el adaptador con una lista vacía
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tiendaAdapter);

        // Obtener la lista de tiendas del centro comercial
        obtenerListaDeTiendas(); // Llamar al método para obtener los datos de la base de datos
        tiendaAdapter.setOnItemClickListener(new TiendaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tienda tienda) {
                Log.d("Click", "Di click: ");
                mostrarDetalleTienda(tienda);

            }
        });


        return view;
    }

    // Método para obtener la lista de tiendas de la base de datos
    private void obtenerListaDeTiendas() {
        // Hacer una consulta a la base de datos para obtener los datos de las tiendas
        TiendasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Tienda> tiendas = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Tienda tienda = dataSnapshot.getValue(Tienda.class);
                    tiendas.add(tienda);
                }
                Log.d("Tiendas", "Cantidad de tiendas obtenidas: " + tiendas.size());
                tiendaAdapter.setTiendas(tiendas); // Actualizar el adaptador con la lista de tiendas obtenida
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Tiendas", "Error al obtener las tiendas: " + error.getMessage());
                // Manejar error de la base de datos, si es necesario
            }
        });
    }
    private void mostrarDetalleTienda(Tienda tienda) {
        // Oculta la lista (RecyclerView)
        recyclerView.setVisibility(View.GONE);

        // Crea una instancia del Fragment de detalle y pasa la información del centro comercial
        TiendaDetalleFragment detalleFragment = TiendaDetalleFragment.newInstance(tienda);

        // Reemplaza el contenido del contenedor principal con el Fragment de detalle
        getParentFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, detalleFragment)
                .addToBackStack(null)
                .commit();
    }


}

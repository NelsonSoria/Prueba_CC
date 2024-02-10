package com.example.prueba_centrocomercial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class TiendaDetalleFragment extends Fragment {

    private TextView tvNombreTienda;
    private TextView tvDescripcionTienda;
    private TextView tvTelefonoTienda;
    private ImageView ivLlamar;

    public TiendaDetalleFragment() {
        // Constructor vacío requerido
    }

    public static TiendaDetalleFragment newInstance(Tienda tienda) {
        TiendaDetalleFragment fragment = new TiendaDetalleFragment();
        Bundle args = new Bundle();
        args.putString("nombre", tienda.getNombre());
        args.putString("descripcion", tienda.getDescripcion());
        args.putString("telefono", tienda.getTelefono());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tienda_detalle, container, false);
        tvNombreTienda = view.findViewById(R.id.tvNombreTienda);
        tvDescripcionTienda = view.findViewById(R.id.tvDescripcionTienda);
        tvTelefonoTienda = view.findViewById(R.id.tvTelefonoTienda);
        ivLlamar = view.findViewById(R.id.ivLlamar);

        Bundle args = getArguments();
        if (args != null) {
            String nombre = args.getString("nombre", "");
            String descripcion = args.getString("descripcion", "");
            String telefono = args.getString("telefono", "");

            tvNombreTienda.setText(nombre);
            tvDescripcionTienda.setText(descripcion);
            tvTelefonoTienda.setText(telefono);

            // Configurar el listener para el icono de llamar
            ivLlamar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llamar(telefono);
                }
            });
        }

        return view;
    }

    // Método para iniciar una llamada telefónica
    private void llamar(String telefono) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + telefono));
        startActivity(intent);
    }
}

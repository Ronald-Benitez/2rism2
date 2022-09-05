package com.example.tworism.Provider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.tworism.R;
import com.example.tworism.databinding.ActivitySeleccionarUbicacionBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class seleccionarUbicacion extends FragmentActivity implements GoogleMap.OnMarkerDragListener,OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivitySeleccionarUbicacionBinding binding;
    private Marker marcadorOrigen, marcadorDestino;
    LatLng Origen = new LatLng(13.439815, -88.159210);
    LatLng Destino = new LatLng(13.461510, -88.165446);
    TextView coordLatitud, coordLongitud;
    Button btnGuardarUbicacion;
    String UserId,UserName,UserVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySeleccionarUbicacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        coordLatitud = (TextView) findViewById(R.id.coordLatitud);
        coordLongitud = (TextView) findViewById(R.id.coordLongitud);
        btnGuardarUbicacion = (Button) findViewById(R.id.btnGuardarUbicacion);



        btnGuardarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(seleccionarUbicacion.this, OrganizarViaje.class);
                intent.putExtra("latitudOrigen", String.valueOf(marcadorOrigen.getPosition().latitude));
                intent.putExtra("longitudOrigen", String.valueOf(marcadorOrigen.getPosition().longitude));
                intent.putExtra("latitudDestino", String.valueOf(marcadorDestino.getPosition().latitude));
                intent.putExtra("longitudDestino", String.valueOf(marcadorDestino.getPosition().longitude));
                intent.putExtra("UserId", UserId);
                intent.putExtra("UserName", UserName);
                intent.putExtra("UserVerified", UserVerified);
                startActivity(intent);
            }
        });

        try {
            Bundle bundle = getIntent().getExtras();
            UserId = bundle.getString("UserId");
            UserName = bundle.getString("UserName");
            UserVerified = bundle.getString("UserVerified");

        }
        catch (Exception e){
            Toast.makeText(this, "Error al obtener el id del usuario", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //13.439815, -88.159210
        //13.461510, -88.165446

        // Add a marker in Sydney and move the camera


        marcadorOrigen = mMap.addMarker(new MarkerOptions()
                .position(Origen)
                .title("Origen")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .draggable(true));
        marcadorDestino = mMap.addMarker(new MarkerOptions()
                .position(Destino)
                .title("Destino")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .draggable(true));



        mMap.setMaxZoomPreference(14);
        mMap.setMinZoomPreference(8);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Origen));
        //Tocar el marcador
        googleMap.setOnMarkerClickListener(this);
        //Arrastrar marcadores
        googleMap.setOnMarkerDragListener(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (marker.equals(marcadorOrigen)) {
            Toast.makeText(this, "Marcador Origen", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (marker.equals(marcadorDestino)) {
            Toast.makeText(this, "Marcador de Llegada", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }
    //Drag listener
    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
            if (marker.equals(marcadorOrigen)) {
                String lattitude = String.valueOf(marcadorOrigen.getPosition().latitude);
                String longitude = String.valueOf(marcadorOrigen.getPosition().longitude);
                coordLatitud.setText(lattitude);
                coordLongitud.setText(longitude);
            }
             if (marker.equals(marcadorDestino)) {
                String lattitude = String.valueOf(marcadorDestino.getPosition().latitude);
                String longitude = String.valueOf(marcadorDestino.getPosition().longitude);
                coordLatitud.setText(lattitude);
                coordLongitud.setText(longitude);
            }
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
        if (marker.equals(marcadorOrigen)) {
            Toast.makeText(this, "Coloque El lugar de origen", Toast.LENGTH_SHORT).show();
        }
        if (marker.equals(marcadorDestino)) {
            Toast.makeText(this, "Coloque El lugar de Llegada", Toast.LENGTH_SHORT).show();
        }
    }
}
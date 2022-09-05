package com.example.tworism.Provider;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tworism.ListarVehiculos;
import com.example.tworism.MainActivity;
import com.example.tworism.R;
import com.example.tworism.RegistrarVehiculo;
import com.example.tworism.Users.ProfileActivity;

public class ProviderMainActivity extends AppCompatActivity {

    Button btnAddVehicle,btnListarVehiculo,btnOrganizarViaje,btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_main);
        Bundle bundle = getIntent().getExtras();
        String UserId = bundle.getString("UserId");
        String UserName = bundle.getString("UserName");
        Boolean UserVerified = Boolean.parseBoolean(bundle.getString("UserVerified"));

        btnAddVehicle = findViewById(R.id.btnAddVehicle);
        btnListarVehiculo = findViewById(R.id.btnListarVehiculo);
        btnOrganizarViaje = findViewById(R.id.btnOrganizarViaje);
        btnProfile = findViewById(R.id.btnProfile);


        if(!UserVerified){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProviderMainActivity.this);
            builder.setMessage("Necesitas verificar tu cuenta para poder registrar datos");
            builder.show();
            btnAddVehicle.setEnabled(false);
            btnListarVehiculo.setEnabled(false);

        }

        btnAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProviderMainActivity.this, RegistrarVehiculo.class);
                intent.putExtra("UserId", UserId);
                intent.putExtra("UserName", UserName);
                intent.putExtra("UserVerified", UserVerified.toString());
                startActivity(intent);
            }
        });

        btnListarVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProviderMainActivity.this, ListarVehiculos.class);
                intent.putExtra("UserId", UserId);
                intent.putExtra("UserName", UserName);
                intent.putExtra("UserVerified", UserVerified.toString());
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProviderMainActivity.this, ProfileActivity.class);
                intent.putExtra("UserId", UserId);
                intent.putExtra("UserName", UserName);
                intent.putExtra("Procedure", "Proveedor");
                intent.putExtra("UserVerified", UserVerified.toString());
                startActivity(intent);
            }
        });

        btnOrganizarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProviderMainActivity.this, seleccionarUbicacion.class);
                intent.putExtra("UserId", UserId);
                intent.putExtra("UserName", UserName);
                intent.putExtra("UserVerified", UserVerified.toString());
                startActivity(intent);
            }
        });

    }

    public void onBackPressed(){
        Intent intent = new Intent(ProviderMainActivity.this, MainActivity.class);

        startActivity(intent);
        finish();
    }
}
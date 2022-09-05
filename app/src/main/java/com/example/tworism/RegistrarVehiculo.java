package com.example.tworism;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.tworism.Provider.ProviderMainActivity;
import com.example.tworism.Retrofit.UserModel;
import com.example.tworism.Retrofit.VehicleInterface;
import com.example.tworism.Retrofit.VehicleModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

public class RegistrarVehiculo extends AppCompatActivity {
    Button btnRegister, btnBack;
    EditText TipoV, CapacidadV, PlacaV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_vehiculo);
        Bundle bundle = getIntent().getExtras();
        String UserId = bundle.getString("UserId");
        String UserName = bundle.getString("UserName");
        String UserVerified = bundle.getString("UserVerified");

        btnRegister = findViewById(R.id.btnRegistrarVe);
        btnBack = findViewById(R.id.btnBack);
        TipoV = findViewById(R.id.TVehiculo);
        CapacidadV = findViewById(R.id.CapacidadV);
        PlacaV = findViewById(R.id.NumeroPlaca);

        Retrofit retrofit = RetrofitClient.getClient();
        VehicleInterface vehicleInterface = retrofit.create(VehicleInterface.class);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tipoVe = TipoV.getText().toString();
                String capacidad = CapacidadV.getText().toString();
                String placa = PlacaV.getText().toString();
                Map<String, String> params = Map.of( "VehicleTuition", placa, "VehicleType", tipoVe, "VehicleCapacity", capacidad, "UserId", UserId);
                Call<VehicleModel> call = vehicleInterface.registerVe(params);
                call.enqueue(new retrofit2.Callback<VehicleModel>() {
                    @Override
                    public void onResponse(Call<VehicleModel> call, retrofit2.Response<VehicleModel> response) {
                        VehicleModel vehicleModel = response.body();
                        if(vehicleModel.getVehicleTuition()!=null){
                            Toast.makeText(RegistrarVehiculo.this, "Vehiculo registrado correctamente", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<VehicleModel> call, Throwable t) {
                        Toast.makeText(RegistrarVehiculo.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrarVehiculo.this, ProviderMainActivity.class);
                intent.putExtra("UserId",UserId);
                intent.putExtra("UserName",UserName);
                intent.putExtra("UserVerified",UserVerified);

                startActivity(intent);
            }
        });
    }
}
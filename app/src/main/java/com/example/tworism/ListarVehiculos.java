package com.example.tworism;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tworism.Adapter.VehiclesAdapter;
import com.example.tworism.Provider.ProviderMainActivity;
import com.example.tworism.Retrofit.VehicleInterface;
import com.example.tworism.Retrofit.VehicleModel;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarVehiculos extends AppCompatActivity {

    ArrayList<String> usuarios = new ArrayList<String>();
    RecyclerView listVehiculos;
    String UserId;
    String UserName, UserVerified;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_vehiculos);
        Bundle bundle = getIntent().getExtras();
        UserId = bundle.getString("UserId");
        UserName = bundle.getString("UserName");
        UserVerified = bundle.getString("UserVerified");

        listVehiculos = findViewById(R.id.listVehicle);
        listVehiculos.setHasFixedSize(true);
        listVehiculos.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            back();
        });

        cargarVehiculos();
    }

    public void cargarVehiculos(){
        VehicleInterface api = RetrofitClient.getClient().create(VehicleInterface.class);
        try {
            Call<List<VehicleModel>> call = api.listarVehiculos(UserId);
            call.enqueue(new Callback<List<VehicleModel>>() {
                @Override
                public void onResponse(Call<List<VehicleModel>> call, Response<List<VehicleModel>> response) {
                    List<VehicleModel> lista = response.body();
                    VehiclesAdapter adapter = new VehiclesAdapter(lista,UserId,UserName,UserVerified);
                    listVehiculos.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<VehicleModel>> call, Throwable t) {

                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Error al cargar los vehiculos", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        back();
        finish();
    }

    public void back(){
        Intent intent = new Intent(ListarVehiculos.this, ProviderMainActivity.class);
        intent.putExtra("UserId", UserId);
        intent.putExtra("UserName", UserName);
        intent.putExtra("UserVerified", UserVerified);
        startActivity(intent);
    }
}
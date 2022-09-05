package com.example.tworism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tworism.Models.TravelRegister;
import com.example.tworism.R;
import com.example.tworism.Retrofit.TravelInterface;
import com.example.tworism.RetrofitClient;

import retrofit2.Call;

public class PasareraDePagos extends AppCompatActivity {
    Button btnRealizarPago,btnCancelarTransaccion;
    String UserId,UserName,TravelId;
    TravelInterface travelInterface = RetrofitClient.getClient().create(TravelInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasarera_de_pagos);
        Bundle extras = getIntent().getExtras();
        UserId = extras.getString("UserId");
        UserName = extras.getString("UserName");
        TravelId = extras.getString("TravelId");

        btnRealizarPago = findViewById(R.id.btnRealizarPago);
        btnCancelarTransaccion = findViewById(R.id.btnCancelarTransaccion);
        btnRealizarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PasareraDePagos.this, "Pago Realizado con exito", Toast.LENGTH_SHORT).show();
                reserve();
                back();
            }
        });

        btnCancelarTransaccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PasareraDePagos.this, "Se ha cancelado el pago", Toast.LENGTH_SHORT).show();
                back();
            }
        });
    }

    public void back(){
        Intent intent = new Intent(PasareraDePagos.this, DetailsActivity.class);
        intent.putExtra("UserId",UserId);
        intent.putExtra("UserName",UserName);
        intent.putExtra("TravelId",TravelId);
        startActivity(intent);
        finish();
    }

    public void reserve(){
        try{
            Call<TravelRegister> call = travelInterface.reserveTravel(TravelId,UserId);
            call.enqueue(new retrofit2.Callback<TravelRegister>() {
                @Override
                public void onResponse(Call<TravelRegister> call, retrofit2.Response<TravelRegister> response) {
                    if(response.isSuccessful()){
                        TravelRegister travelRegister = response.body();
                        if(travelRegister!=null){
                            Toast.makeText(PasareraDePagos.this, "Reservado", Toast.LENGTH_SHORT).show();
                            back();
                        } else {
                            Toast.makeText(PasareraDePagos.this, "Error al reservar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PasareraDePagos.this, "Error al reservar", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TravelRegister> call, Throwable t) {
                    Toast.makeText(PasareraDePagos.this, "Error al reservar", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(PasareraDePagos.this, "Error al reservar", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.e("Error",e.getMessage());
        }
    }
}
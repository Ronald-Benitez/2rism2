package com.example.tworism.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tworism.R;

public class PasareraDePagos extends AppCompatActivity {
    Button btnRealizarPago,btnCancelarTransaccion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasarera_de_pagos);

        btnRealizarPago = findViewById(R.id.btnRealizarPago);
        btnCancelarTransaccion = findViewById(R.id.btnCancelarTransaccion);
        btnRealizarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(PasareraDePagos.this, MainClientActivity.class);
                Toast.makeText(PasareraDePagos.this, "Pago Realizado con exito", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        btnCancelarTransaccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(PasareraDePagos.this, ProfileActivity.class);
                Toast.makeText(PasareraDePagos.this, "Se ha cancelado el pago", Toast.LENGTH_SHORT).show();
                // startActivity(intent);
            }
        });
    }
}
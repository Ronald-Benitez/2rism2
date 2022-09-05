package com.example.tworism.Provider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tworism.Models.TravelModel;
import com.example.tworism.R;
import com.example.tworism.Retrofit.TravelInterface;
import com.example.tworism.Retrofit.VehicleInterface;
import com.example.tworism.Retrofit.VehicleModel;
import com.example.tworism.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizarViaje extends AppCompatActivity {
    String UserId,UserName,UserVerified;
    Button btnUbicaciones,btnPublicarViaje;
    ImageButton backbtn;
    TextView textoAdvertencia;
    EditText fechaPartida,txtTravelPrice,txtTravelTime,txtLLegada,txtOrigen,txtTravelExcludes,txtTravelIncludes;
    String SlatitudOrigen,SlongitudOrigen,SlatitudDestino,SlongitudDestino;
    ArrayList<String> NombreVehiculos = new ArrayList<String>();
    Spinner VehicleSpinner;
    VehicleModel Seleccionado;
    List<VehicleModel> lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizar_viaje);
        Bundle bundle = getIntent().getExtras();
        UserId = bundle.getString("UserId");
        UserName = bundle.getString("UserName");
        UserVerified = bundle.getString("UserVerified");


        TravelInterface travelInterface = RetrofitClient.getClient().create(TravelInterface.class);

        backbtn = findViewById(R.id.imageButtonBack);
        btnUbicaciones = findViewById(R.id.btnUbicaciones);
        btnPublicarViaje = findViewById(R.id.btnPublicarViaje);
        VehicleSpinner = findViewById(R.id.VehicleSpinner);
        fechaPartida = findViewById(R.id.fechaPartida);
        txtTravelPrice = findViewById(R.id.txtTravelPrice);
        txtTravelTime = findViewById(R.id.txtTravelTime);
        txtLLegada = findViewById(R.id.txtLLegada);
        txtOrigen = findViewById(R.id.txtOrigen);
        txtTravelExcludes = findViewById(R.id.txtTravelExcludes);
        txtTravelIncludes = findViewById(R.id.txtTravelIncludes);

        try {
            //Obtener datos de coordenadas
            Bundle bundle1 = getIntent().getExtras();
            UserId = bundle1.getString("UserId");
            UserName = bundle1.getString("UserName");
            UserVerified = bundle1.getString("UserVerified");

            SlatitudOrigen = bundle1.getString("latitudOrigen");
            SlongitudOrigen = bundle1.getString("longitudOrigen");
            SlatitudDestino = bundle1.getString("latitudDestino");
            SlongitudDestino = bundle1.getString("longitudDestino");

            Log.d("latitudOrigen", SlatitudOrigen);
            Log.d("longitudOrigen", SlongitudOrigen);
            Log.d("latitudDestino", SlatitudDestino);
            Log.d("longitudDestino", SlongitudDestino);

            if (SlatitudOrigen != null && SlongitudOrigen != null && SlatitudDestino != null && SlongitudDestino != null) {
                textoAdvertencia.setVisibility(View.INVISIBLE);
                btnPublicarViaje.setEnabled(true);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        cargarVehiculos(); //Carga los vehiculos del usuario en el spinner

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizarViaje.this, ProviderMainActivity.class);
                intent.putExtra("UserId", UserId);
                intent.putExtra("UserName", UserName);
                intent.putExtra("UserVerified", UserVerified.toString());
                startActivity(intent);
                finish();
            }
        });


        VehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Seleccionado = lista.get(i);
                Log.d("seleccionado:", Seleccionado.getVehicleTuition().toString());
                Log.d("Capacidad:", Seleccionado.getVehicleCapacity().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnUbicaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizarViaje.this, seleccionarUbicacion.class);
                intent.putExtra("UserId",UserId);
                intent.putExtra("UserName",UserName);
                intent.putExtra("UserVerified",UserVerified);
                startActivity(intent);
            }
        });

        btnPublicarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mandarLatitudes = SlatitudOrigen + "," + SlatitudDestino;
                String mandarLongitudes = SlongitudOrigen + "," + SlongitudDestino;
                TravelModel viajeEnviar = new TravelModel();
                viajeEnviar.setUserId(UserId);
                viajeEnviar.setVehicleId(String.valueOf(Seleccionado.getVehicleId()));
                viajeEnviar.setTravelDate(fechaPartida.getText().toString());
                viajeEnviar.setTravelTime(txtTravelTime.getText().toString());
                viajeEnviar.setTravelPrice(txtTravelPrice.getText().toString());
                viajeEnviar.setTravelCapacity(Seleccionado.getVehicleCapacity().toString());
                viajeEnviar.setTravelRegistered("0");
                viajeEnviar.setTravelOrigin(txtOrigen.getText().toString());
                viajeEnviar.setTravelDestination(txtLLegada.getText().toString());
                viajeEnviar.setTravelLatitudes(mandarLatitudes);
                viajeEnviar.setTravelLongitudes(mandarLongitudes);
                viajeEnviar.setTravelTags(" ");
                viajeEnviar.setTravelIncludes(txtTravelIncludes.getText().toString());
                viajeEnviar.setTravelExcludes(txtTravelExcludes.getText().toString());

                Log.d("UserId", viajeEnviar.getUserId());
                Log.d("VehicleId", viajeEnviar.getVehicleId());
                Log.d("TravelDate", viajeEnviar.getTravelDate());
                Log.d("TravelTime", viajeEnviar.getTravelTime());
                Log.d("TravelPrice", viajeEnviar.getTravelPrice());
                Log.d("TravelCapacity", viajeEnviar.getTravelCapacity());
                Log.d("TravelRegistered", viajeEnviar.getTravelRegistered());
                Log.d("TravelOrigin", viajeEnviar.getTravelOrigin());
                Log.d("TravelDestination", viajeEnviar.getTravelDestination());
                Log.d("TravelLatitudes", viajeEnviar.getTravelLatitudes());
                Log.d("TravelLongitudes", viajeEnviar.getTravelLongitudes());
                Log.d("TravelTags", viajeEnviar.getTravelTags());
                Log.d("TravelIncludes", viajeEnviar.getTravelIncludes());
                Log.d("TravelExcludes", viajeEnviar.getTravelExcludes());

                Call<TravelModel> call = travelInterface.createTravel(viajeEnviar.getUserId(),
                        viajeEnviar.getVehicleId(), viajeEnviar.getTravelDate(), viajeEnviar.getTravelTime(),
                        viajeEnviar.getTravelPrice(), viajeEnviar.getTravelCapacity(), viajeEnviar.getTravelRegistered(),
                        viajeEnviar.getTravelOrigin(), viajeEnviar.getTravelDestination(), viajeEnviar.getTravelLatitudes(),
                        viajeEnviar.getTravelLongitudes(), viajeEnviar.getTravelTags(), viajeEnviar.getTravelIncludes(),
                        viajeEnviar.getTravelExcludes());
call.enqueue(new Callback<TravelModel>() {
                    @Override
                    public void onResponse(Call<TravelModel> call, Response<TravelModel> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(OrganizarViaje.this, "Viaje publicado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OrganizarViaje.this, ProviderMainActivity.class);
                            intent.putExtra("UserId",UserId);
                            intent.putExtra("UserName",UserName);
                            intent.putExtra("UserVerified",UserVerified);

                            startActivity(intent);
                        }else{
                            Toast.makeText(OrganizarViaje.this, "Error al publicar viaje", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TravelModel> call, Throwable t) {
                        Toast.makeText(OrganizarViaje.this, "Error al publicar viaje", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }

    public void cargarVehiculos(){
        VehicleInterface api = RetrofitClient.getClient().create(VehicleInterface.class);
        try {
            Call<List<VehicleModel>> call = api.listarVehiculos(UserId);
            call.enqueue(new Callback<List<VehicleModel>>() {
                @Override
                public void onResponse(Call<List<VehicleModel>> call, Response<List<VehicleModel>> response) {
                    lista = response.body();
                    for (VehicleModel x:lista){
                        NombreVehiculos.add(x.getVehicleTuition());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrganizarViaje.this, android.R.layout.simple_spinner_item, NombreVehiculos);
                    VehicleSpinner.setAdapter(adapter);
                }
                @Override
                public void onFailure(Call<List<VehicleModel>> call, Throwable t) {

                }
            });
        }catch (Exception e){
            Toast.makeText(this, "No hay vehiculos Disponibles", Toast.LENGTH_SHORT).show();
            btnUbicaciones.setEnabled(false);
            e.printStackTrace();
        }
    }

}
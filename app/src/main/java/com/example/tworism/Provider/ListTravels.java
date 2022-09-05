package com.example.tworism.Provider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.tworism.Adapter.TravelSimplifyAdapter;
import com.example.tworism.R;
import com.example.tworism.Retrofit.RecentsDataModel;
import com.example.tworism.Retrofit.TravelInterface;
import com.example.tworism.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTravels extends AppCompatActivity {
    RecyclerView rvTravels;
    Button btnBack;
    String UserId, UserName, UserVerified;
    TravelInterface travelInterface = new RetrofitClient().getClient().create(TravelInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_travels);
        rvTravels = findViewById(R.id.rvTravels);
        btnBack = findViewById(R.id.btnBack);
        Bundle bundle = getIntent().getExtras();
        UserId = bundle.getString("UserId");
        UserName = bundle.getString("UserName");
        UserVerified = bundle.getString("UserVerified");

        rvTravels.setLayoutManager(new LinearLayoutManager(this));
        btnBack.setOnClickListener(v -> {
            back();
        });

        cargarViajes();

    }

    public void back(){
        Intent intent = new Intent(ListTravels.this, ProviderMainActivity.class);
        intent.putExtra("UserId", UserId);
        intent.putExtra("UserName", UserName);
        intent.putExtra("UserVerified", UserVerified);
        startActivity(intent);
        finish();
    }

    public  void onBackPressed(){
        back();
    }

    public void cargarViajes(){
        try{
            Call<List<RecentsDataModel>> call = travelInterface.getUserTravel(UserId);
            call.enqueue(new Callback<List<RecentsDataModel>>() {
                @Override
                public void onResponse(Call<List<RecentsDataModel>> call, Response<List<RecentsDataModel>> response) {
                    if(response.isSuccessful()){
                        List<RecentsDataModel> recentsDataModels = response.body();
                        if(recentsDataModels.size() > 0) {
                            TravelSimplifyAdapter travelSimplifyAdapter = new TravelSimplifyAdapter(recentsDataModels);
                            rvTravels.setAdapter(travelSimplifyAdapter);
                        }else{
                            Toast.makeText(ListTravels.this, "No hay viajes", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ListTravels.this, "No se encontraron viajes", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<RecentsDataModel>> call, Throwable t) {
                    Toast.makeText(ListTravels.this, "No se encontraron viajes", Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "No se encontraron viajes", Toast.LENGTH_SHORT).show();
            Log.e("Error", e.getMessage());
        }

    }
}
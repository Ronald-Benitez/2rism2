package com.example.tworism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tworism.Adapter.RecentsDataAdapter;
import com.example.tworism.MainActivity;
import com.example.tworism.Models.RecentsModel;
import com.example.tworism.Models.TravelHistoryModel;
import com.example.tworism.R;
import com.example.tworism.Retrofit.RecentsDataModel;
import com.example.tworism.Retrofit.RecentsInterface;
import com.example.tworism.Retrofit.TravelInterface;
import com.example.tworism.RetrofitClient;
import com.example.tworism.Users.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainClientActivity extends AppCompatActivity {

    RecyclerView recentRecycler;

    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    RecentsDataAdapter recentsDataAdapter;
    List<RecentsDataModel> recentsDataModelList = new ArrayList<>();

    RecentsDataAdapter recentsAdapter;
    ImageView profile, home;
    String UserId,UserName;
    Spinner spFilterKey;
    EditText etFilterValue;
    Button btnFind, btnHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);
        UserId = getIntent().getStringExtra("UserId");
        UserName = getIntent().getStringExtra("UserName");

        profile = findViewById(R.id.profile);
        home = findViewById(R.id.ivHome);
        spFilterKey = findViewById(R.id.spFilter);
        etFilterValue = findViewById(R.id.editTextTextPersonName);
        btnFind = findViewById(R.id.btnFind);
        btnHistory = findViewById(R.id.btnHistory);

        String [] filterValue = {"Filtro a aplicar","Origen","Destino","Fecha","Precio"};
        String [] filterKeys ={"","TravelOrigin","TravelDestination","TravelDate","TravelPrice"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, filterValue);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilterKey.setAdapter(adapter);

        recentRecycler = findViewById(R.id.recent_recycler);
        progressBar = findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(this);
        recentRecycler.setLayoutManager(layoutManager);
        recentsDataAdapter =new RecentsDataAdapter(recentsDataModelList,UserId,UserName);
        recentRecycler.setAdapter(recentsDataAdapter);


        fetchRecentData();
        setRecentDateRecycler();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadActivity();
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String filterKey = filterKeys[spFilterKey.getSelectedItemPosition()];
               String filterValue = etFilterValue.getText().toString();
               Boolean filter = true;
                if(filterKey.equals("")){
                     filter = false;
                     Toast.makeText(MainClientActivity.this, "Debe seleccionar un filtro", Toast.LENGTH_SHORT).show();
                }
                if (filterValue.equals("")){
                    filter = false;
                    Toast.makeText(MainClientActivity.this, "Debe ingresar un valor", Toast.LENGTH_SHORT).show();
                }
                if(filter){
                    try {
                        TravelInterface travelInterface = RetrofitClient.getClient().create(TravelInterface.class);
                        Call<List<RecentsDataModel>> call = travelInterface.filterTravel(filterKey,filterValue);
                        call.enqueue(new Callback<List<RecentsDataModel>>() {
                            @Override
                            public void onResponse(Call<List<RecentsDataModel>> call, Response<List<RecentsDataModel>> response) {
                                if(response.isSuccessful()){
                                    recentsDataModelList = response.body();
                                    if(recentsDataModelList.size() > 0) {
                                        recentsAdapter = new RecentsDataAdapter(recentsDataModelList, UserId, UserName);
                                        recentRecycler.setAdapter(recentsAdapter);
                                        Toast.makeText(MainClientActivity.this, "Viajes filtrados", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(MainClientActivity.this, "No se encontraron viajes", Toast.LENGTH_SHORT).show();
                                        reloadActivity();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<RecentsDataModel>> call, Throwable t) {
                                Toast.makeText(MainClientActivity.this, "No se encontraron viajes", Toast.LENGTH_SHORT).show();
                                Log.e("Error",t.getMessage());
                                reloadActivity();
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(MainClientActivity.this, "No se encontraron viajes", Toast.LENGTH_SHORT).show();
                        Log.e("Error",e.getMessage());
                        reloadActivity();
                    }
                }
           }
         });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainClientActivity.this, ProfileActivity.class);
                intent.putExtra("UserId", UserId);
                intent.putExtra("UserName", UserName);
                intent.putExtra("Procedure", "Cliente");
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHistory();
            }
        });

    }

    private  void setRecentDateRecycler(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recentRecycler.setLayoutManager(layoutManager);

    }

    public void fetchRecentData(){
        RecentsInterface api = RetrofitClient.getClient().create(RecentsInterface.class);
        api.getRecentsDataModel().enqueue(new Callback<List<RecentsDataModel>>() {
            @Override
            public void onResponse(Call<List<RecentsDataModel>> call, Response<List<RecentsDataModel>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    recentsDataModelList.addAll(response.body());
                    recentsDataAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<RecentsDataModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainClientActivity.this, "Error"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onBackPressed(){
        Intent intent = new Intent(MainClientActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void reloadActivity(){
        Intent intent = new Intent(MainClientActivity.this, MainClientActivity.class);
        intent.putExtra("UserId", UserId);
        intent.putExtra("UserName", UserName);
        startActivity(intent);
        finish();
    }

    public void loadHistory(){
        TravelInterface travelInterface = RetrofitClient.getClient().create(TravelInterface.class);
        try {
            Call<List<TravelHistoryModel>> call = travelInterface.getHistory(UserId);
            call.enqueue(new Callback<List<TravelHistoryModel>>() {
                @Override
                public void onResponse(Call<List<TravelHistoryModel>> call, Response<List<TravelHistoryModel>> response) {
                    if(response.isSuccessful()){
                        List<TravelHistoryModel> travelHistoryModelList = response.body();
                        try {
                            if (travelHistoryModelList.size() > 0) {
                                List<RecentsDataModel> recentsDataModelList = new ArrayList<>();
                                for (int i = 0; i < travelHistoryModelList.size(); i++) {
                                    recentsDataModelList.add(travelHistoryModelList.get(i).getTravel());
                                }
                                recentsAdapter = new RecentsDataAdapter(recentsDataModelList, UserId, UserName);
                                recentRecycler.setAdapter(recentsAdapter);
                                Toast.makeText(MainClientActivity.this, "Historial obtenido", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainClientActivity.this, "No se encontraron viajes", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(MainClientActivity.this, "No se encontraron viajes", Toast.LENGTH_SHORT).show();
                            Log.e("Error",e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<TravelHistoryModel>> call, Throwable t) {
                    Toast.makeText(MainClientActivity.this, "No se encontraron viajes", Toast.LENGTH_SHORT).show();
                    Log.e("Error",t.getMessage());
                }
            });
        }catch (Exception e){
            Toast.makeText(MainClientActivity.this, "No se encontraron viajes", Toast.LENGTH_SHORT).show();
            Log.e("Error",e.getMessage());
            reloadActivity();
        }
    }
}
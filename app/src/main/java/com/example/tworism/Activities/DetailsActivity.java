package com.example.tworism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tworism.Models.TravelModel;
import com.example.tworism.Models.TravelRegister;
import com.example.tworism.R;
import com.example.tworism.Retrofit.TravelInterface;
import com.example.tworism.Retrofit.UserInterface;
import com.example.tworism.Retrofit.UserModel;
import com.example.tworism.RetrofitClient;
import com.example.tworism.Users.RegisterActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailsActivity extends AppCompatActivity {

    RatingBar ratingStars;
    float myRating=0;

    ImageView ImageViewReturn;
    GoogleMap googleMap;
    MapView mapaMP;
    Button btnReserve;
    String UserId,UserName,TravelId;
    TextView txtTravelOrigin,txtTravelDestination,txtTravelDate,txtTravelTime,txtTravelPrice,txtTravelRating;
    TravelInterface travelInterface = RetrofitClient.getClient().create(TravelInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle extras = getIntent().getExtras();
        UserId = extras.getString("UserId");
        UserName = extras.getString("UserName");
        TravelId = extras.getString("TravelId");

        ratingStars = findViewById(R.id.ratingBar2);


        ImageViewReturn = findViewById(R.id.imageViewReturn);
        txtTravelDestination = findViewById(R.id.textView8);
        txtTravelOrigin = findViewById(R.id.textView21);
        txtTravelDate = findViewById(R.id.textView14);
        txtTravelTime = findViewById(R.id.textView16);
        txtTravelPrice = findViewById(R.id.textView19);
        txtTravelRating = findViewById(R.id.textViewRating);
        btnReserve = findViewById(R.id.button);


        getData();

        mapaMP = findViewById(R.id.mapaView);
        mapaMP.onCreate(savedInstanceState);

        //
        Retrofit retrofit = RetrofitClient.getClient();
        UserInterface userInterface = retrofit.create(UserInterface.class);

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserve();
            }
        });

        ImageViewReturn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                onBackPressed();
            }

        });


        //Stars ratingBar
        ratingStars = findViewById(R.id.ratingBar2);
        txtTravelRating = findViewById(R.id.textViewRating);

        ratingStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                float rating = v;
                myRating = ratingBar.getRating();

                int rateCount = 0;
                int numRate = rateCount+1;

                //
                String rate = String.valueOf(rating);
                String numRateString = String.valueOf(numRate);

                Map<String, String> params = Map.of("UserCalification", rate, "UserCalifications", numRateString);
                try {
                    Call<UserModel> call = userInterface.register(params);
                    call.enqueue(new retrofit2.Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, retrofit2.Response<UserModel> response) {
                            UserModel userModel = response.body();
                                Toast.makeText(DetailsActivity.this, "Su califacion es: "+myRating, Toast.LENGTH_SHORT).show();
                                txtTravelRating.setText(rate);

                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            Toast.makeText(DetailsActivity.this, "Error al registrar calificacion!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(DetailsActivity.this, "Error en la calificacion! ", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapaMP.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapaMP.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapaMP.onDestroy();
    }

    public void getData(){
        try {
            Call<TravelModel> call = travelInterface.getTravel(TravelId);
            call.enqueue(new retrofit2.Callback<TravelModel>() {
                @Override
                public void onResponse(Call<TravelModel> call, retrofit2.Response<TravelModel> response) {
                    if (response.isSuccessful()) {
                        TravelModel travelModel = response.body();
                        txtTravelOrigin.setText(travelModel.getTravelOrigin());
                        txtTravelDestination.setText(travelModel.getTravelDestination());
                        txtTravelDate.setText(travelModel.getTravelDate());
                        txtTravelTime.setText(travelModel.getTravelTime());
                        txtTravelPrice.setText(travelModel.getTravelPrice());

                        try {
                            UserInterface userInterface = RetrofitClient.getClient().create(UserInterface.class);
                            Call<UserModel> call1 = userInterface.getUser(travelModel.getUserId());
                            call1.enqueue(new retrofit2.Callback<UserModel>() {
                                @Override
                                public void onResponse(Call<UserModel> call, retrofit2.Response<UserModel> response) {
                                    if (response.isSuccessful()) {
                                        UserModel userModel = response.body();
                                        float UserCalification = Float.parseFloat(userModel.getUserCalification());
                                        int UserCalifications = Integer.parseInt(userModel.getUserCalifications());
                                        if(UserCalification == 0) {
                                            txtTravelRating.setText("-");
                                        } else {
                                            txtTravelRating.setText(String.valueOf(UserCalification/UserCalifications));
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserModel> call, Throwable t) {
                                    Toast.makeText(DetailsActivity.this, "Error al obtener la calificación", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (Exception e){
                            txtTravelRating.setText("-");
                        }
                        List<String> latitudes = List.of(travelModel.getTravelLatitudes().split(","));
                        List<String> longitudes = List.of(travelModel.getTravelLongitudes().split(","));
                        setMap(latitudes,longitudes);

                    } else {
                        Toast.makeText(DetailsActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TravelModel> call, Throwable t) {
                    Toast.makeText(DetailsActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                }
            });


        }catch (Exception e){
            Toast.makeText(this, "Error al cargar el viaje", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.e("Error",e.getMessage());
            back();
        }
    }

    public void back(){
        Intent intent = new Intent(this, MainClientActivity.class);
        intent.putExtra("UserId",UserId);
        intent.putExtra("UserName",UserName);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        back();
    }

    public void setMap(List<String> latitudes, List<String> longitudes){
        mapaMP.getMapAsync(mapa -> {
            googleMap = mapa;
            LatLng marcador = new LatLng(Double.parseDouble(latitudes.get(0)), Double.parseDouble(longitudes.get(0)));
            googleMap.addMarker(new MarkerOptions().position(marcador).title("Partida").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(marcador));
            LatLng marcador2 = new LatLng(Double.parseDouble(latitudes.get(1)), Double.parseDouble(longitudes.get(1)));
            googleMap.addMarker(new MarkerOptions().position(marcador2).title("Destino").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(marcador2));
            googleMap.setMaxZoomPreference(14);
            googleMap.setMinZoomPreference(8);

        });
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
                            Toast.makeText(DetailsActivity.this, "Reservado", Toast.LENGTH_SHORT).show();
                            back();
                        } else {
                            Toast.makeText(DetailsActivity.this, "Error al reservar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(DetailsActivity.this, "Error al reservar", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TravelRegister> call, Throwable t) {
                    Toast.makeText(DetailsActivity.this, "Error al reservar", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Error al reservar", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.e("Error",e.getMessage());
        }
    }
}
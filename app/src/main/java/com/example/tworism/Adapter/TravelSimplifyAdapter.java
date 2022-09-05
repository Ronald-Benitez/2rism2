package com.example.tworism.Adapter;

import android.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tworism.R;
import com.example.tworism.Retrofit.RecentsDataModel;
import com.example.tworism.Retrofit.TravelInterface;
import com.example.tworism.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelSimplifyAdapter extends RecyclerView.Adapter<TravelSimplifyAdapter.TravelSimplifyViewHolder> {
    List<RecentsDataModel> recentsDataModelList;

    public TravelSimplifyAdapter(List<RecentsDataModel> recentsDataModelList) {
        this.recentsDataModelList = recentsDataModelList;
    }

    @NonNull
    @Override
    public TravelSimplifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_item,parent,false);
        return new TravelSimplifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelSimplifyViewHolder holder, int position) {
        holder.tvTravelDate.setText(recentsDataModelList.get(position).getTravelDate());
        holder.tvTravelDestination.setText(recentsDataModelList.get(position).getTravelDestination());
        holder.tvTravelOrigin.setText(recentsDataModelList.get(position).getTravelOrigin());
        holder.tvTravelId.setText(String.valueOf(recentsDataModelList.get(position).getTravelId()));
        holder.tvTravelPrice.setText(String.valueOf(recentsDataModelList.get(position).getTravelPrice()));
    }

    @Override
    public int getItemCount() {
        try {
            return recentsDataModelList.size();
        }catch (Exception e){
            return 0;
        }
    }

    public class TravelSimplifyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTravelId,tvTravelOrigin,tvTravelDestination,tvTravelDate,tvTravelPrice;
        Button btnDelete;
        public TravelSimplifyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTravelId = itemView.findViewById(R.id.tvTravelId);
            tvTravelOrigin = itemView.findViewById(R.id.tvTravelOrigin);
            tvTravelDestination = itemView.findViewById(R.id.tvTravelDestination);
            tvTravelDate = itemView.findViewById(R.id.tvTravelDate);
            tvTravelPrice = itemView.findViewById(R.id.tvTravelPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TravelInterface travelInterface = RetrofitClient.getClient().create(TravelInterface.class);

                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Eliminar viaje");
                    builder.setMessage("¿Está seguro que desea eliminar este viaje?");
                    builder.setPositiveButton("Si", (dialog, which) -> {
                        Call<List<String>> call = travelInterface.deleteTravel(tvTravelId.getText().toString());
                        call.enqueue(new Callback<List<String>>() {
                            @Override
                            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                                if (response.isSuccessful())
                                {
                                    Toast.makeText(itemView.getContext(), "Viaje eliminado", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<List<String>> call, Throwable t) {
                                Toast.makeText(itemView.getContext(), "Error al eliminar viaje", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                    builder.setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    builder.show();
                }
            });
        }
    }
}

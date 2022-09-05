package com.example.tworism.Retrofit;

import com.example.tworism.Models.TravelHistoryModel;
import com.example.tworism.Models.TravelModel;
import com.example.tworism.Models.TravelRegister;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TravelInterface {

    @FormUrlEncoded
    @POST("/api/travels/")
    Call<TravelModel> createTravel(@Field("UserId") String UserId,
                                   @Field("VehicleId") String VehicleId,
                                   @Field("TravelDate") String TravelDate,
                                   @Field("TravelTime") String TravelTime,
                                   @Field("TravelPrice") String TravelPrice,
                                   @Field("TravelCapacity") String TravelCapacity,
                                   @Field("TravelRegistered") String TravelRegistered,
                                   @Field("TravelOrigin") String TravelOrigin,
                                   @Field("TravelDestination") String TravelDestination,
                                   @Field("TravelLatitudes") String TravelLatitudes,
                                   @Field("TravelLongitudes") String TravelLongitudes,
                                   @Field("TravelTags") String TravelTags,
                                   @Field("TravelIncludes") String TravelIncludes,
                                   @Field("travelExcludes") String travelExcludes);

    @GET("/api/travels/{id}")
    Call<TravelModel> getTravel(@Path("id") String id);

    @FormUrlEncoded
    @POST("/api/travelRegister/")
    Call<TravelRegister> reserveTravel(@Field("TravelId") String TravelId,
                                       @Field("UserId") String UserId);

    @FormUrlEncoded
    @POST("/api/travels/filter")
    Call<List<RecentsDataModel>> filterTravel(@Field(("filter")) String filter,
                                              @Field(("value")) String value);

    @GET("/api/travelRegister/simplify/{id}")
    Call<List<TravelHistoryModel>> getHistory(@Path("id") String id);

}

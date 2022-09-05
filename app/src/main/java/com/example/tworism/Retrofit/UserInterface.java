package com.example.tworism.Retrofit;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserInterface {
    @FormUrlEncoded
    @POST("/api/users/login")
    Call<UserModel> login(@Field("UserEmail") String UserEmail, @Field("UserPassword") String UserPassword);

    @POST("/api/users/")
    Call<UserModel> register(@Body Map<String, String> body);

    @GET("/api/users/calification/{UserId}")
    Call<UserModel> getUser(@Path("UserId") String UserId);

    @FormUrlEncoded
    @POST("/api/users/calification")
    Call<UserModel> makeCalification(@Field("UserId") String UserId, @Field("UserCalification") String UserCalification);

    @PUT("/api/users/{UserId}")
    Call<List<Integer>> updateUser(@Path("UserId") String UserId, @Body Map<String, String> body);


}
package com.unlam.vacunartech.services;

import com.unlam.vacunartech.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    @FormUrlEncoded
    @POST("register")
    Call<LoginResponse> registerUser(@Field("env") String env, @Field("name") String name, @Field("lastname") String lastName, @Field("email") String email,
                                     @Field("dni") Long dni, @Field("password") String password, @Field("commission") Integer commission,
                                     @Field("group") Integer group);

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginUser(@Field("email") String email, @Field("password") String password);
}

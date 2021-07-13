package com.unlam.vacunartech.services;

import com.unlam.vacunartech.models.EventResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface EventService {


    @FormUrlEncoded
    @POST("event")
    Call<EventResponse> registerEvent(@Header("Authorization") String authHeader,
                                      @Field("env") String env, @Field("type_events") String type_events,
                                      @Field("description") String description);
}

package com.example.maze_bank.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

     @GET("weather?appid=45f6a44cfb20584b0a2535a67667a399&units=metric")
     Call<Example> getWeatherData(@Query("q") String name);


}

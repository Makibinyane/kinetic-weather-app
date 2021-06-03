package com.example.global_kinetic_weather_app.service

import com.example.global_kinetic_weather_app.model.WeatherResponse
import com.example.global_kinetic_weather_app.util.Constants
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "x-api-key: " + Constants.API_KEY
    )
    @GET("data/2.5/weather")
    suspend fun getWeatherAsync(@Query("lon") longitude: Double,
                        @Query("lat") latitude: Double): WeatherResponse
}
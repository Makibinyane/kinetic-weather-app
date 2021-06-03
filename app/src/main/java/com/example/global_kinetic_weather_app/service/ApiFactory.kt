package com.example.global_kinetic_weather_app.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {
    private const val DEFAULT_TIMEOUT = 15L
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    private val okhttpClient = OkHttpClient().newBuilder()
        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(logger)
        .build()


    private fun retrofit() : Retrofit = Retrofit.Builder()
        .client(okhttpClient)
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val weatherApi : WeatherApi = retrofit().create(WeatherApi::class.java)

}
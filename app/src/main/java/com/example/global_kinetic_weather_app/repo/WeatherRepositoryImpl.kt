package com.example.global_kinetic_weather_app.repo

import com.example.global_kinetic_weather_app.model.WeatherResponse
import com.example.global_kinetic_weather_app.service.WeatherApi

class WeatherRepositoryImpl(private var service: WeatherApi) : WeatherRepository {

    override suspend fun getWeatherData(longitude: Double, latitude: Double): WeatherResponse {
        return service.getWeatherAsync(longitude, latitude)
    }
}
package com.example.global_kinetic_weather_app.repo

import com.example.global_kinetic_weather_app.model.WeatherResponse
import com.example.global_kinetic_weather_app.service.WeatherApi

interface WeatherRepository {
    suspend fun getWeatherData(longitude: Double, latitude: Double): WeatherResponse?
}
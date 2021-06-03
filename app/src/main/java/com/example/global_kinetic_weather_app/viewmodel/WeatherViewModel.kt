package com.example.global_kinetic_weather_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.global_kinetic_weather_app.model.WeatherResponse
import com.example.global_kinetic_weather_app.repo.WeatherRepository
import com.example.global_kinetic_weather_app.repo.WeatherRepositoryImpl
import com.example.global_kinetic_weather_app.service.ApiFactory

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class WeatherViewModel (private val repository: WeatherRepositoryImpl = WeatherRepositoryImpl(ApiFactory.weatherApi)) : ViewModel() {

    val weatherLiveData = MutableLiveData<WeatherResponse?>()
    val showProgress = MutableLiveData<Boolean>()

    fun fetchWeatherReport(longitude: Double, latitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            showProgress.postValue(true)
            val response = repository.getWeatherData(longitude, latitude)
            weatherLiveData.postValue(response)
            showProgress.postValue(false)
        }
    }

    fun convertKelvinToCelsius(kelvinTemperature: Double?): Int? {
        return kelvinTemperature?.minus(273)?.roundToInt()
    }
}
package com.example.global_kinetic_weather_app.model

import com.google.gson.annotations.SerializedName

data class Coord( @SerializedName("lon") val lon: Double,
                  @SerializedName("lat") val lat: Double)

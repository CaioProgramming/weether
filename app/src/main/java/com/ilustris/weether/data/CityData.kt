package com.ilustris.weether.data

data class CityData(val name: String, val country: String, val weatherData: WeatherData)

data class WeatherData(
    val title: String,
    val description: String,
    val temperature: Int,
    val temperatureFeels: Int,
    val icon: String
)

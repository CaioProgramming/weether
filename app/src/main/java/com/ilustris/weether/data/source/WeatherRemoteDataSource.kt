package com.ilustris.weether.data.source

import com.ilustris.weether.data.dto.response.openweather.OpenWeatherResponse

interface WeatherRemoteDataSource {

    suspend fun queryCityWeather(cityName: String): OpenWeatherResponse
    suspend fun queryLocationWeather(latidude: Double, longitude: Double): OpenWeatherResponse
    suspend fun fetchCities(): List<String>

}
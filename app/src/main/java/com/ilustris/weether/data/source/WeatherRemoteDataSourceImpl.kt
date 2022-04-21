package com.ilustris.weether.data.source

import com.ilustris.weether.data.dto.response.openweather.OpenWeatherResponse
import com.ilustris.weether.network.LocalCities
import com.ilustris.weether.network.OpenWeatherService

class WeatherRemoteDataSourceImpl(private val openWeatherConfig: OpenWeatherService): WeatherRemoteDataSource {

    override suspend fun queryCityWeather(cityName: String) = openWeatherConfig.fetchCityWeather(cityName)
    override suspend fun queryLocationWeather(
        latidude: Double,
        longitude: Double
    ): OpenWeatherResponse = openWeatherConfig.fetchLocationWeather(latidude, longitude)

    override suspend fun fetchCities(): List<String> = LocalCities.cities
}
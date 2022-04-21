package com.ilustris.weether.domain.usecase

import com.ilustris.weether.data.CityData
import com.ilustris.weether.data.dto.response.openweather.OpenWeatherResponse
import com.ilustris.weether.network.Result
import java.lang.Exception

interface WeatherUseCase {

   suspend fun fetchCityWeather(city: String) : Result<Exception, CityData>
   suspend fun fetchLocationWeather(latitude: Double, longitude: Double) : Result<Exception, CityData>
   suspend fun fetchCities() : Result<Exception, List<String>>

}
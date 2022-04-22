package com.ilustris.weether.network

private const val BASEURL = "https://api.openweathermap.org/data/2.5/"
class OpenWeatherService : ApiConfig(BASEURL) {
   suspend fun fetchCityWeather(cityName: String) = retroFitService.create(OpenWeatherApi::class.java).queryCityWeather(cityName)
   suspend fun fetchLocationWeather( latidude: Double,
                              longitude: Double) = retroFitService.create(OpenWeatherApi::class.java).queryLocationWeather(latidude, longitude)
}

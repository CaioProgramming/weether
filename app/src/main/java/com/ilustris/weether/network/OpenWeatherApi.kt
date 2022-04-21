package com.ilustris.weether.network

import com.ilustris.weether.BuildConfig
import com.ilustris.weether.data.dto.response.openweather.OpenWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface OpenWeatherApi {

    @GET("weather")
   suspend fun queryCityWeather(
        @Query("q") cityName: String,
        @Query("units") unit : String = "metric",
        @Query("appid") key: String = BuildConfig.OPENWEATHER_KEY
        ): OpenWeatherResponse

    @GET("weather")
   suspend fun queryLocationWeather(
        @Query("lat") latidude: Double,
        @Query("long") longitude: Double,
        @Query("units") unit : String = "metric",
        @Query("appid") key: String = BuildConfig.OPENWEATHER_KEY,
        ) : OpenWeatherResponse

}
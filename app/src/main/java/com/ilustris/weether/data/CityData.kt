package com.ilustris.weether.data

import android.os.Parcelable
import com.ilustris.weether.data.mapper.WeatherMapper
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityData(val name: String = "", val country: String = "", val weatherData: WeatherData? = null) : Parcelable

const val UNKNOWN_LOCATION = "UNKNOWN"

@Parcelize
data class WeatherData(
    val title: String,
    val description: String,
    val temperature: Int,
    val temperatureFeels: Int,
    val maxTemperature: Int,
    val minTemperature: Int,
    val windSpeed: Double,
    val humidity: Int,
    val weatherType: WeatherMapper.WeatherType?
) : Parcelable

package com.ilustris.weether.data.mapper

import com.ilustris.weether.data.CityData
import com.ilustris.weether.data.WeatherData
import com.ilustris.weether.data.dto.response.openweather.Main
import com.ilustris.weether.data.dto.response.openweather.OpenWeatherResponse
import com.ilustris.weether.data.dto.response.openweather.Weather
import com.ilustris.weether.utils.OPEN_WEATHER_ICONS_SUFIX
import com.ilustris.weether.utils.OPEN_WEATHER_ICONS_URL

class WeatherMapper {

    fun mapResponseToData(response: OpenWeatherResponse): CityData =
        CityData(
            response.name,
            response.sys.country,
            response.weather.first().mapToWeatherData(response.main)
        )

    private fun Weather.mapToWeatherData(tempData: Main) = WeatherData(
        this.main, description, tempData.temp.toInt(), tempData.feels_like.toInt(), getWeatherIcon(icon)
    )

    private fun getWeatherIcon(iconUrl: String) = OPEN_WEATHER_ICONS_URL + iconUrl + OPEN_WEATHER_ICONS_SUFIX

}

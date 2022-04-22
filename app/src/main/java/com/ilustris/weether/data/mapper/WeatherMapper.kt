package com.ilustris.weether.data.mapper

import com.ilustris.weether.R
import com.ilustris.weether.data.CityData
import com.ilustris.weether.data.WeatherData
import com.ilustris.weether.data.dto.response.openweather.Main
import com.ilustris.weether.data.dto.response.openweather.OpenWeatherResponse
import com.ilustris.weether.data.dto.response.openweather.Weather

class WeatherMapper {

    fun mapResponseToData(response: OpenWeatherResponse): CityData =
        CityData(
            response.name,
            response.sys.country,
            response.weather.first().mapToWeatherData(response.main, response.wind.speed)
        )

    private fun Weather.mapToWeatherData(tempData: Main, windSpeed: Double) = WeatherData(
        this.main,
        description,
        tempData.temp.toInt(),
        tempData.feels_like.toInt(),
        tempData.temp_max.toInt(),
        tempData.temp_min.toInt(),
        windSpeed,
        tempData.humidity,
        getWeatherType(this)
    )

    private fun getWeatherType(weatherInfo: Weather): WeatherType? {
        val requiredId : String = if (weatherInfo.main == "Clouds") "80" else weatherInfo.id.toString()[0].toString()
        return  WeatherType.values().find { it.id == requiredId.toInt() }
    }

    enum class WeatherType(val id: Int, val animationUrl: String = "", val backColor: Int, val textColor: Int) {
        THUNDERSTORM(2, "https://assets7.lottiefiles.com/temp/lf20_XkF78Y.json", com.github.mcginty.R.color.material_grey700, R.color.white),
        DRIZZLE(3, "https://assets7.lottiefiles.com/temp/lf20_rpC1Rd.json", com.github.mcginty.R.color.material_grey200, R.color.black),
        RAIN(5, "https://assets7.lottiefiles.com/packages/lf20_yvyo106d.json", com.github.mcginty.R.color.material_bluegrey400, R.color.white),
        SNOW(6, "https://assets4.lottiefiles.com/private_files/lf30_w5u9xr3a.json", com.github.mcginty.R.color.material_blue200, R.color.white),
        ATMOSPHERE(7, "https://assets7.lottiefiles.com/private_files/lf30_iugenddu.json", com.github.mcginty.R.color.material_deeppurpleA400, R.color.white),
        CLEAR(8, "https://assets4.lottiefiles.com/private_files/lf30_moaf5wp5.json", com.github.mcginty.R.color.material_blue500, R.color.white),
        CLOUDS(80, "https://assets4.lottiefiles.com/private_files/lf30_j1g2rpsv.json", com.github.mcginty.R.color.material_grey100, R.color.black)
    }

}

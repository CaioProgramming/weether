package com.ilustris.weether.data.dto.response.openweather

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)
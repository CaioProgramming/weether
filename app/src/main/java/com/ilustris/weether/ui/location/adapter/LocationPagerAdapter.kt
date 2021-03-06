package com.ilustris.weether.ui.location.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.weether.R
import com.ilustris.weether.data.CityData
import com.ilustris.weether.data.mapper.WeatherMapper
import com.ilustris.weether.databinding.LocationPageBinding

class LocationPagerAdapter(val cities: List<CityData>) :
    RecyclerView.Adapter<LocationPagerAdapter.LocationPageViewHolder>() {

    inner class LocationPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            LocationPageBinding.bind(itemView).run {
                val cityData = cities[bindingAdapterPosition]
                weatherLocation.text = cityData.name
                weatherCurrentTemp.text = itemView.context.getString(R.string.temperature_placeholder, cityData.weatherData.temperature)
                weatherMaxTemp.text = itemView.context.getString(R.string.temperature_placeholder, cityData.weatherData.maxTemperature)
                weatherMinTemp.text = itemView.context.getString(R.string.temperature_placeholder, cityData.weatherData.minTemperature)
                weatherHumidity.text = "${cityData.weatherData.humidity}%"
                weatherWindSpeed.text = itemView.context.getString(R.string.wind_placeholder, cityData.weatherData.windSpeed)

                weatherDescription.text = cityData.weatherData.description
                cityData.weatherData.weatherType?.let { extractWeatherStyle(it) }
            }
        }

        fun LocationPageBinding.extractWeatherStyle(weatherType: WeatherMapper.WeatherType) {
            val textColor = ContextCompat.getColor(itemView.context, weatherType.textColor)
            val backColor = ContextCompat.getColor(itemView.context, weatherType.backColor)
            lottieWeatherAnimation.setAnimationFromUrl(weatherType.animationUrl)
            weatherCurrentTemp.setTextColor(textColor)
            weatherDescription.setTextColor(textColor)
            weatherLocation.setTextColor(textColor)
            weatherMaxTemp.setTextColor(textColor)
            weatherMinTemp.setTextColor(textColor)
            weatherWindSpeed.setTextColor(textColor)
            weatherHumidity.setTextColor(textColor)
            weatherHumiditySubtitle.setTextColor(textColor)
            weatherMaxTempSubtitle.setTextColor(textColor)
            weatherMinTempSubtitle.setTextColor(textColor)
            windSpeedSubtitle.setTextColor(textColor)
            itemView.setBackgroundColor(backColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationPageViewHolder =
        LocationPageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.location_page, parent, false)
        )


    override fun onBindViewHolder(holder: LocationPageViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = cities.size


}
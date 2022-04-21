package com.ilustris.weether.ui.location.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ilustris.weether.R
import com.ilustris.weether.data.CityData
import com.ilustris.weether.databinding.LocationPageBinding

class LocationPagerAdapter(val cities: List<CityData>) :
    RecyclerView.Adapter<LocationPagerAdapter.LocationPageViewHolder>() {


    inner class LocationPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            LocationPageBinding.bind(itemView).run {
                val cityData = cities[bindingAdapterPosition]
                weatherLocation.text = cityData.name
                weatherCurrentTemp.text = "${cityData.weatherData.temperature}°C"
                weatherMaxTemp.text = "${cityData.weatherData.maxTemperature}°C"
                weatherMinTemp.text = "${cityData.weatherData.minTemperature}°C"
                weatherHumidity.text = "${cityData.weatherData.humidity}%"
                weatherWindSpeed.text = "${cityData.weatherData.windSpeed} mph"
                weatherDescription.text = cityData.weatherData.description
                cityData.weatherData.weatherType?.let {
                    val textColor = ContextCompat.getColor(itemView.context, it.textColor)
                    val backColor = ContextCompat.getColor(itemView.context, it.backColor)
                    lottieWeatherAnimation.setAnimationFromUrl(it.animationUrl)
                    weatherCurrentTemp.setTextColor(textColor)
                    weatherDescription.setTextColor(textColor)
                    weatherLocation.setTextColor(textColor)
                    /*weatherMaxTemp.setTextColor(textColor)
                    weatherMinTemp.setTextColor(textColor)
                    weatherHumidity.setTextColor(textColor)
                    weatherWindSpeed.setTextColor(textColor)*/
                    itemView.setBackgroundColor(backColor)
                }
            }
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
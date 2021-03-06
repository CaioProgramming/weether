package com.ilustris.weether.ui.home.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.weether.R
import com.ilustris.weether.data.CityData
import com.ilustris.weether.databinding.HighlightCardBinding
import com.ilustris.weether.databinding.WeatherCardBinding

private const val HIGHLIGHTVIEW = 0
private const val SECONDARYVIEW = 1

class WeatherRecyclerviewAdapter(val citiesWeather: ArrayList<CityData> = ArrayList(),val onSelectCity: (Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    fun updateCities(cityData: CityData) {
        citiesWeather.add(cityData)
        notifyDataSetChanged()
    }

    fun refreshCities(cities: List<CityData>) {
        citiesWeather.addAll(cities)
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        citiesWeather.clear()
        notifyDataSetChanged()
    }

    inner class HighLightCityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind() {
            HighlightCardBinding.bind(itemView).run {
                val city = citiesWeather[bindingAdapterPosition]
                weatherLocation.text = Html.fromHtml("<b>${city.name}</b>, ${city.country}", HtmlCompat.FROM_HTML_MODE_LEGACY)
                weatherDescription.text = city.weatherData.description
                weatherStatus.text = city.weatherData.title
                temp.text = itemView.context.getString(R.string.temperature_placeholder, city.weatherData.temperature)
                tempFeels.text = itemView.context.getString(R.string.temperature_feel_placeholder, city.weatherData.temperatureFeels)
                city.weatherData.weatherType?.let {
                    weatherIcon.setAnimationFromUrl(it.animationUrl)
                    weatherIcon.playAnimation()
                    val textColor = ContextCompat.getColor(itemView.context, it.textColor)
                    val backColor = ContextCompat.getColor(itemView.context, it.backColor)
                    temp.setTextColor(textColor)
                    weatherLocation.setTextColor(textColor)
                    root.setCardBackgroundColor(backColor)
                    tempFeels.setTextColor(textColor)
                    weatherStatus.setTextColor(textColor)
                    weatherDescription.setTextColor(textColor)
                }
            }
        }
    }

    inner class CityWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            WeatherCardBinding.bind(itemView).run {
                val city = citiesWeather[bindingAdapterPosition]
                weatherLocation.text = Html.fromHtml("<b>${city.name}</b>, ${city.country}", HtmlCompat.FROM_HTML_MODE_LEGACY)
                temp.text = itemView.context.getString(R.string.temperature_placeholder, city.weatherData.temperature)
                weatherDescription.text = city.weatherData.description
                city.weatherData.weatherType?.let {
                    val textColor = ContextCompat.getColor(itemView.context, it.textColor)
                    val backColor = ContextCompat.getColor(itemView.context, it.backColor)
                    weatherIcon.setAnimationFromUrl(it.animationUrl)
                    temp.setTextColor(textColor)
                    weatherLocation.setTextColor(textColor)
                    weatherDescription.setTextColor(textColor)
                    root.setCardBackgroundColor(backColor)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType) {
            HIGHLIGHTVIEW -> {
                HighLightCityViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.highlight_card, parent, false))
            }
            SECONDARYVIEW -> {
                CityWeatherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_card, parent, false))
            }
           else -> {
               CityWeatherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_card, parent, false))
           }
        }
    }

    override fun getItemViewType(position: Int): Int {
       return if (position == 0) HIGHLIGHTVIEW else SECONDARYVIEW
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
           onSelectCity(position)
        }
        if (!holder.itemView.isVisible) {
            holder.itemView.visibility = View.VISIBLE
            val slideIn = AnimationUtils.loadAnimation(holder.itemView.context, org.koin.android.R.anim.abc_slide_in_bottom)
            holder.itemView.startAnimation(slideIn)
        }

        when(holder) {
            is HighLightCityViewHolder ->  {
                holder.bind()
            }
            is CityWeatherViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemCount(): Int = citiesWeather.size

}
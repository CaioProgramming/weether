package com.ilustris.weether.ui.home.adapter

import android.graphics.drawable.Drawable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ilustris.weether.R
import com.ilustris.weether.data.CityData
import com.ilustris.weether.databinding.HighlightCardBinding
import com.ilustris.weether.databinding.WeatherCardBinding

private const val HIGHLIGHTVIEW = 0
private const val SECONDARYVIEW = 1

class WeatherRecyclerviewAdapter(val citiesWeather: ArrayList<CityData> = ArrayList(),val onSelectCity: (CityData) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    fun updateCities(cityData: CityData) {
        citiesWeather.add(cityData)
        notifyDataSetChanged()
    }

    inner class HighLightCityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind() {
            HighlightCardBinding.bind(itemView).run {
                val city = citiesWeather[bindingAdapterPosition]
                weatherLocation.text = Html.fromHtml("<b>${city.name}</b>, ${city.country}")
                weatherDescription.text = city.weatherData.description
                weatherStatus.text = city.weatherData.title
                temp.text = city.weatherData.temperature.toString()
                tempFeels.text = city.weatherData.temperatureFeels.toString()
                Glide.with(itemView.context).load(weatherIcon)
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            weatherIcon.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            val anim =
                                AnimationUtils.loadAnimation(itemView.context, R.anim.fade_in)
                            weatherIcon.startAnimation(anim)
                            return false
                        }


                    }).into(weatherIcon)
                itemView.setOnClickListener {
                    onSelectCity(city)
                }
            }
        }

    }

    inner class CityWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            WeatherCardBinding.bind(itemView).run {
                val city = citiesWeather[bindingAdapterPosition]
                weatherLocation.text = Html.fromHtml("<b>${city.name}</b>, ${city.country}")
                temp.text = city.weatherData.temperature.toString()
                Glide.with(itemView.context).load(weatherIcon)
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            weatherIcon.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            val anim =
                                AnimationUtils.loadAnimation(itemView.context, R.anim.fade_in)
                            weatherIcon.startAnimation(anim)
                            return false
                        }


                    }).into(weatherIcon)
                itemView.setOnClickListener {
                    onSelectCity(city)
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
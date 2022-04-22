package com.ilustris.weether.ui.home

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.weether.data.CityData
import com.ilustris.weether.domain.usecase.WeatherUseCase
import com.ilustris.weether.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application, private val useCase: WeatherUseCase) :
    AndroidViewModel(application) {

    val homeState = MutableLiveData<HomeState>()

    sealed class HomeState {
        data class LocalWeatherRetrieved(val cityData: CityData): HomeState()
        data class CitiesWeatherRetrieved(val cities: List<CityData>) : HomeState()
        data class FetchError(val message: String) : HomeState()
        data class CityQueryError(val message: String): HomeState()
        object RequestLocationPermission : HomeState()
        object RequestActualLocation : HomeState()
    }

    fun fetchCities() {
        viewModelScope.launch(Dispatchers.IO) {
            val request = useCase.fetchCities()
            when (request) {
                is Result.Error -> postError(request.exception.message)
                is Result.Success -> {
                    val cities = ArrayList<CityData>()
                    request.data.forEach {
                        when(val cityWeather = useCase.fetchCityWeather(it)) {
                            is Result.Error -> {
                                homeState.postValue(HomeState.CityQueryError("Error fetching $it weather"))
                            }
                            is Result.Success -> {
                                cities.add(cityWeather.data)
                            }
                        }
                    }
                    homeState.postValue(HomeState.CitiesWeatherRetrieved(cities.sortedBy { it.name }))
                }
            }
        }
    }

    private fun postError(message: String?) {
        homeState.postValue(
            HomeState.FetchError(
                message ?: ""
            )
        )
    }

    fun fetchWeatherLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val request = useCase.fetchLocationWeather(latitude, longitude)) {
                is Result.Error -> postError(request.exception.message)
                is Result.Success -> homeState.postValue(HomeState.LocalWeatherRetrieved(request.data))
            }
        }
    }

    fun checkLocationPermission() {
        viewModelScope.launch(Dispatchers.IO) {
            val permissionGranted = ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if (!permissionGranted) {
                homeState.postValue(HomeState.RequestLocationPermission)
            } else {
                homeState.postValue(HomeState.RequestActualLocation)
            }
        }

    }

}
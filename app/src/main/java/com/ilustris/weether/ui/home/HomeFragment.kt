package com.ilustris.weether.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_LOW_POWER
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ilustris.weether.data.CityData
import com.ilustris.weether.databinding.HomeFragmentBinding
import com.ilustris.weether.ui.home.adapter.WeatherRecyclerviewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    lateinit var homeBinding: HomeFragmentBinding
    private val homeViewModel: HomeViewModel by viewModel()
    private val weatherAdapter = WeatherRecyclerviewAdapter(onSelectCity = {
        selectCity(it)
    })

    private fun selectCity(cityData: CityData) {

    }

    private val locationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }
    private var cancellationToken = CancellationTokenSource()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = HomeFragmentBinding.inflate(inflater)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViewModel()
    }

    private fun setupView() {
        homeBinding.weatherRecycler.adapter = weatherAdapter
       // homeViewModel.checkLocationPermission()
        homeViewModel.fetchCities()
    }

    private fun observeViewModel() {
        homeViewModel.homeState.observe(viewLifecycleOwner) {
            when (it) {
                is HomeViewModel.HomeState.CityWeatherRetrieved -> {
                    updateCities(it.cityData)
                }
                is HomeViewModel.HomeState.FetchError -> {
                    showError(it.message)
                }
                HomeViewModel.HomeState.RequestActualLocation -> fetchLocationWeather()
                HomeViewModel.HomeState.RequestLocationPermission -> requestLocationPermission()
            }
        }
    }

    private fun showError(message: String) {

        MaterialAlertDialogBuilder(requireContext()).setTitle("Attention").setMessage(message)
            .setNegativeButton(
                "Ok"
            ) { dialog, which -> dialog?.dismiss() }

    }

    private fun updateCities(cityData: CityData) {
        weatherAdapter.updateCities(cityData)
    }

    private fun requestLocationPermission() {
        activity?.run {
            val locationPermissionRequest =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        fetchLocationWeather()
                    } else {
                        showError("Current location unavailable")
                    }

                }

            locationPermissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocationWeather() {
        val locationTask =
            locationClient.getCurrentLocation(PRIORITY_LOW_POWER, cancellationToken.token)
        locationTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                homeViewModel.fetchWeatherLocation(task.result.latitude, task.result.longitude)
            } else {
                showError("Unable to fetch current location")
            }
        }
    }


}
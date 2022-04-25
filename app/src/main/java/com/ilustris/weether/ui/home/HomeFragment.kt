package com.ilustris.weether.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_LOW_POWER
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.ilustris.weether.R
import com.ilustris.weether.data.CityData
import com.ilustris.weether.data.UNKNOWN_LOCATION
import com.ilustris.weether.databinding.HomeFragmentBinding
import com.ilustris.weether.ui.home.adapter.WeatherRecyclerviewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var homeBinding: HomeFragmentBinding? = null
    private val homeViewModel: HomeViewModel by viewModel()
    private val weatherAdapter = WeatherRecyclerviewAdapter(onSelectCity = { index ->
        selectCity(index)
    })
    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>

    private fun selectCity(index: Int) {

        val city = weatherAdapter.citiesWeather[index]
        if (city.name == UNKNOWN_LOCATION) {
            requestLocationPermission()
        } else {
            val bundle = bundleOf("position" to index, "cities" to weatherAdapter.citiesWeather.toTypedArray())
            findNavController().navigate(R.id.action_homeFragment_to_locationDetailsFragment, bundle)
            weatherAdapter.clearAdapter()
        }

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
        locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    weatherAdapter.clearAdapter()
                    fetchLocationWeather()
                } else {
                    weatherAdapter.updateCities(CityData(name = UNKNOWN_LOCATION))
                    homeBinding?.showRecycler()
                    homeViewModel.fetchCities()
                }
            }
        homeBinding = HomeFragmentBinding.inflate(inflater)
        weatherAdapter.clearAdapter()
        return homeBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViewModel()
    }

    private fun setupView() {
        homeBinding?.weatherRecycler?.adapter = weatherAdapter
        homeViewModel.checkLocationPermission()
    }

    private fun observeViewModel() {
        homeViewModel.homeState.observe(viewLifecycleOwner) {
            when (it) {

                is HomeViewModel.HomeState.FetchError -> {
                    showError(it.message)
                }
                HomeViewModel.HomeState.RequestActualLocation -> fetchLocationWeather()
                HomeViewModel.HomeState.RequestLocationPermission -> requestLocationPermission()
                is HomeViewModel.HomeState.LocalWeatherRetrieved -> {
                    weatherAdapter.clearAdapter()
                    homeBinding?.weatherRecycler?.adapter = weatherAdapter
                    updateCities(it.cityData)
                    homeViewModel.fetchCities()
                }
                is HomeViewModel.HomeState.CitiesWeatherRetrieved -> addCities(it.cities)
                is HomeViewModel.HomeState.CityQueryError -> {
                    Snackbar.make(requireView(), it.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun addCities(cities: List<CityData>) {
        weatherAdapter.refreshCities(cities)
    }

    private fun showError(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED)
            .show()
    }

    private fun updateCities(cityData: CityData) {
        weatherAdapter.updateCities(cityData)
        homeBinding?.run {
            if (!weatherRecycler.isVisible) {
                showRecycler()
            }
        }
    }

    private fun  HomeFragmentBinding.showRecycler() {
        weatherRecycler.visibility = View.VISIBLE
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        weatherRecycler.startAnimation(fadeIn)
        val fadeOut = AnimationUtils.loadAnimation(
            requireContext(),
            com.airbnb.lottie.R.anim.abc_fade_out
        )
        loadingAnimation.startAnimation(fadeOut)
        loadingAnimation.cancelAnimation()
        loadingAnimation.visibility = View.GONE
    }

    private fun requestLocationPermission() {
        activity?.run {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Attention")
                .setMessage("To get your current location weather info please grant the permission for our app!")
                .setNegativeButton(
                    "Cancel"
                ) { dialog, _ -> dialog?.dismiss() }
                .setPositiveButton("Ok") { dialog, _ ->
                    locationPermissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                    dialog.dismiss()
                }
                .show()

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

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

}
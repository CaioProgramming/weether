package com.ilustris.weether.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ilustris.weether.databinding.LocationWeatherFragmentBinding
import com.ilustris.weether.ui.location.adapter.LocationPagerAdapter

class LocationDetailsFragment : Fragment() {

    private val args by navArgs<LocationDetailsFragmentArgs>()
    private lateinit var locationWeatherFragmentBinding: LocationWeatherFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationWeatherFragmentBinding = LocationWeatherFragmentBinding.inflate(inflater)
        return locationWeatherFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationWeatherFragmentBinding.locationsPager.run {
           adapter = LocationPagerAdapter(args.cities.toList())
            setCurrentItem(args.position, true)
        }

    }


}
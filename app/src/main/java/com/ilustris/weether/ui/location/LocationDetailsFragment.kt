package com.ilustris.weether.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.ilustris.weether.data.CityData
import com.ilustris.weether.databinding.LocationWeatherFragmentBinding
import com.ilustris.weether.ui.location.adapter.LocationPagerAdapter

class LocationDetailsFragment : Fragment() {

    private val args by navArgs<LocationDetailsFragmentArgs>()
    lateinit var locationWeatherFragmentBinding: LocationWeatherFragmentBinding

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
        locationWeatherFragmentBinding.locationsPager.adapter = LocationPagerAdapter(args.cities.toList())
        locationWeatherFragmentBinding.locationsPager.setCurrentItem(args.position, true)
    }


}
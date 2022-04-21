package com.ilustris.weether

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ilustris.weether.data.CityData
import com.ilustris.weether.domain.usecase.WeatherUseCase
import com.ilustris.weether.network.LocalCities
import com.ilustris.weether.network.Result
import com.ilustris.weether.ui.home.HomeViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.Exception
import kotlin.IllegalStateException
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    private lateinit var useCase: WeatherUseCase
    private lateinit var homeViewModel: HomeViewModel
    private val homeStateObserver = mockk<Observer<HomeViewModel.HomeState>>()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        useCase = mockk(relaxed = true)
        homeViewModel = HomeViewModel(mockk(relaxed = true), useCase)
    }

    @After
    fun clean() {
        Dispatchers.resetMain()
        homeViewModel.homeState.removeObserver(homeStateObserver)
    }

    @Test
    fun `fetch local weather`() {
        val lat = 0.0
        val long = 0.0
        val sucessResult = Result.Success(
            CityData(
                "test",
                "tst",
                mockk(relaxed = true)
            )
        )
        coEvery {
            useCase.fetchLocationWeather(lat, long)
        } returns sucessResult
        homeViewModel.fetchWeatherLocation(lat, long)
        homeViewModel.homeState.observeForever {
            assertEquals(it, HomeViewModel.HomeState.LocalWeatherRetrieved(sucessResult.data))
        }
    }

    @Test
    fun `fetch local weather error`() {
        val lat = 0.0
        val long = 0.0
        val exception = IllegalStateException()
        val errorResult = Result.Error<Exception>(exception)
        coEvery {
            useCase.fetchLocationWeather(lat, long)
        } returns errorResult
        homeViewModel.fetchWeatherLocation(lat, long)
        homeViewModel.homeState.observeForever {
            assertEquals(it, HomeViewModel.HomeState.FetchError(exception.message?: ""))
        }
    }

    @Test
    fun `fetch cities Weather`() {
        mockkObject(LocalCities)
        val successResult = Result.Success(
            CityData(
                "test city",
                "city tst",
                mockk(relaxed = true)
            )
        )
        val testCities = listOf("TestCity", "TestCity")
        val expectedResult = listOf(
            CityData(
                "test city",
                "city tst",
                mockk(relaxed = true)
            ), CityData(
                "test city",
                "city tst",
                mockk(relaxed = true)
            )
        )
        every {
            LocalCities.cities
        } returns testCities
        coEvery {
            useCase.fetchCityWeather(any())
        } returns successResult

        homeViewModel.fetchCities()

        homeViewModel.homeState.observeForever {
            assertEquals(it, HomeViewModel.HomeState.CitiesWeatherRetrieved(expectedResult))
        }

    }

    @Test
    fun `fetch cities Error`() {
        mockkObject(LocalCities)
        val exception = IllegalStateException()
        val errorResult = Result.Error<Exception>(exception)
        val testCities = listOf("TestCity", "TestCity")
        val expectedResult = listOf(
            CityData(
                "test city",
                "city tst",
                mockk(relaxed = true)
            ), CityData(
                "test city",
                "city tst",
                mockk(relaxed = true)
            )
        )
        every {
            LocalCities.cities
        } returns testCities
        coEvery {
            useCase.fetchCityWeather(any())
        } returns errorResult

        homeViewModel.fetchCities()

        homeViewModel.homeState.observeForever {
            assertEquals(it, HomeViewModel.HomeState.CityQuerryError("TestCity"))
        }
    }


}
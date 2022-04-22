package com.ilustris.weether

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ilustris.weether.data.CityData
import com.ilustris.weether.data.dto.response.openweather.OpenWeatherResponse
import com.ilustris.weether.data.mapper.WeatherMapper
import com.ilustris.weether.data.source.WeatherRemoteDataSource
import com.ilustris.weether.domain.usecase.WeatherUseCase
import com.ilustris.weether.domain.usecase.WeatherUseCaseImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class WeatherUseCaseTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()
    lateinit var weatherUseCase: WeatherUseCase
    lateinit var weatherMapper: WeatherMapper
    lateinit var remoteDataSource: WeatherRemoteDataSource

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        weatherMapper = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        weatherUseCase = WeatherUseCaseImpl(remoteDataSource, weatherMapper)
    }

    @Test
    fun `query city`() {
        val query = "test city"
        val response = mockk<OpenWeatherResponse>()
        val mockData = CityData("mocked city", "mock nation", mockk())
        every {
            weatherMapper.mapResponseToData(response)
        } returns mockData

        coEvery {
            remoteDataSource.queryCityWeather(query)
        } returns response

        val request = runBlocking { weatherUseCase.fetchCityWeather(query) }

        assertEquals(mockData, request.success.data)
    }

    @Test
    fun `query location`() {
        val lat = 0.0
        val long = 0.0
        val response = mockk<OpenWeatherResponse>()
        val mockData = CityData("mocked city", "mock nation", mockk())
        every {
            weatherMapper.mapResponseToData(response)
        } returns mockData

        coEvery {
            remoteDataSource.queryLocationWeather(lat, long)
        } returns response

        val request = runBlocking { weatherUseCase.fetchLocationWeather(lat, long) }

        assertEquals(mockData, request.success.data)
    }

    @After
    fun clean() {
        Dispatchers.resetMain()
    }

}
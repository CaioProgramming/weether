package com.ilustris.weether.domain.usecase

import com.ilustris.weether.data.CityData
import com.ilustris.weether.data.mapper.WeatherMapper
import com.ilustris.weether.data.source.WeatherRemoteDataSource
import com.ilustris.weether.network.Result

class WeatherUseCaseImpl(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherMapper: WeatherMapper
) : WeatherUseCase {

    override suspend fun fetchCityWeather(city: String): Result<Exception, CityData> {
        return try {
            val request = weatherRemoteDataSource.queryCityWeather(city)
            return Result.Success(weatherMapper.mapResponseToData(request))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }

    override suspend fun fetchLocationWeather(
        latitude: Double,
        longitude: Double
    ): Result<java.lang.Exception, CityData> {
        return try {
            val request = weatherRemoteDataSource.queryLocationWeather(latitude, longitude)
            return Result.Success(weatherMapper.mapResponseToData(request))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }

    override suspend fun fetchCities(): Result<java.lang.Exception, List<String>> {
       return try {
           val request = weatherRemoteDataSource.fetchCities()
           Result.Success(request)
       } catch (e: Exception) {
           e.printStackTrace()
           Result.Error(e)
       }
    }


}
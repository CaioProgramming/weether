package com.ilustris.weether.di

import com.ilustris.weether.data.mapper.WeatherMapper
import com.ilustris.weether.data.source.WeatherRemoteDataSource
import com.ilustris.weether.data.source.WeatherRemoteDataSourceImpl
import com.ilustris.weether.domain.usecase.WeatherUseCase
import com.ilustris.weether.domain.usecase.WeatherUseCaseImpl
import com.ilustris.weether.network.OpenWeatherService
import com.ilustris.weether.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory<OpenWeatherService> { OpenWeatherService() }
    single{ WeatherMapper() }
    factory<WeatherRemoteDataSource> { WeatherRemoteDataSourceImpl(get()) }
    factory<WeatherUseCase> { WeatherUseCaseImpl(get(), get()) }

    viewModel { HomeViewModel(androidApplication(), get()) }
}
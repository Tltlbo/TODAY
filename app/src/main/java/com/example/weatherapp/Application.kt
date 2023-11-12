package com.example.weatherapp

import android.app.Application
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.example.weatherapp.detailweather.DetailWeatherViewModel
import com.example.weatherapp.detailweather.WeatherListViewModel

class MyApplication  : Application(){

    private val viewModelStore = ViewModelStore()

    val mainViewModel : MainViewModel by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
    }


    val weatherListViewModel: WeatherListViewModel by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(WeatherListViewModel::class.java)
    }

    val detailWeatherViewModel: DetailWeatherViewModel by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(DetailWeatherViewModel::class.java)
    }


}
package com.example.weatherapp

import android.app.Application
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.example.weatherapp.detaildust.DetailDustViewModel
import com.example.weatherapp.detaildust.DustListViewModel
import com.example.weatherapp.detailuv.DetailUVViewModel
import com.example.weatherapp.detailuv.UVListViewModel
import com.example.weatherapp.detailweather.DetailWeatherViewModel
import com.example.weatherapp.detailweather.WeatherListViewModel
import com.example.weatherapp.user.UserInformationViewModel

class MyApplication  : Application(){

    private val viewModelStore = ViewModelStore()
    var weatherlistflag = false
    var dustlistflag = false
    var uvlistflag = false


    val mainViewModel : MainViewModel by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
    }


    val weatherListViewModel: WeatherListViewModel by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(WeatherListViewModel::class.java)
    }

    val detailWeatherViewModel: DetailWeatherViewModel by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(DetailWeatherViewModel::class.java)
    }

    val dustListVIewModel: DustListViewModel by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(DustListViewModel::class.java)
    }

    val detaildustVIewModel: DetailDustViewModel by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(DetailDustViewModel::class.java)
    }

    val uvListViewModel : UVListViewModel by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(UVListViewModel::class.java)
    }

    val detailuvViewModel : DetailUVViewModel by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(DetailUVViewModel::class.java)
    }

    val userInformationViewModel : UserInformationViewModel by lazy {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(UserInformationViewModel::class.java)
    }

}
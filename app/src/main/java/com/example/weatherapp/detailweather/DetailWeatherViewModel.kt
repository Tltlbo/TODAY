package com.example.weatherapp.detailweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.ModelWeather

class DetailWeatherViewModel : ViewModel() {
    var weather : ModelWeather = ModelWeather()
    var checkweather : MutableLiveData<Boolean> = MutableLiveData(false)

    fun deleteweatherInfo(weather : ModelWeather, locationlist : ArrayList<Pair<Int,Int>>, weatherlist : MutableList<ModelWeather>, oWeatherList : MutableLiveData<List<ModelWeather>>) {
        locationlist.remove(Pair(weather.nx,weather.ny))
        weatherlist.remove(weather)
        oWeatherList.value = weatherlist
    }

    fun favoriteWeather(weather : ModelWeather, locationlist : ArrayList<Pair<Int,Int>>, weatherlist : MutableList<ModelWeather>, oWeatherList : MutableLiveData<List<ModelWeather>>) {
        val index = locationlist.indexOf(Pair(weather.nx,weather.ny))
        val templocation = locationlist.get(index)
        locationlist.remove(templocation)
        locationlist.add(0,templocation)
        weatherlist.remove(weather)
        weatherlist.add(0,weather)
        oWeatherList.value = weatherlist
    }
}
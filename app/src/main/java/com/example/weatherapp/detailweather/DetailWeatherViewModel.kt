package com.example.weatherapp.detailweather

import DustItem
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.MyApplication
import com.example.weatherapp.data.ModelTemp
import com.example.weatherapp.data.ModelWeather
import com.example.weatherapp.data.UVItem

class DetailWeatherViewModel : ViewModel() {
    var weather : ModelWeather = ModelWeather()

    fun deleteweatherInfo(weather : ModelWeather, locationlist : ArrayList<Triple<Int,Int, String>>, weatherlist : MutableList<ModelWeather>, oWeatherList : MutableLiveData<List<ModelWeather>>, app : MyApplication) {
        locationlist.remove(Triple(weather.nx,weather.ny, weather.address))
        weatherlist.remove(weather)
        oWeatherList.value = weatherlist

        if(app.uvlistflag) {
            var uvindex = -1
            for(i in app.uvListViewModel.UVList) {
                if(weather.address.equals(i.address)) {
                    uvindex = app.uvListViewModel.UVList.indexOf(i)
                    break
                }
            }
            app.uvListViewModel.UVList.removeAt(uvindex)
            app.uvListViewModel._oUVList.value = app.uvListViewModel.UVList
        }

        if(app.dustlistflag) {
            var dustindex = -1
            for(i in app.dustListVIewModel.DustList) {
                if(weather.address.equals(i.address)) {
                    dustindex = app.dustListVIewModel.DustList.indexOf(i)
                    break
                }
            }
            app.dustListVIewModel.DustList.removeAt(dustindex)
            app.dustListVIewModel._oDustList.value = app.dustListVIewModel.DustList
        }

        var locationinddex = -1
        for(i in app.mainViewModel.userLocation) {
            if(weather.address.equals(i.third)) {
                locationinddex = app.mainViewModel.userLocation.indexOf(i)
            }
        }
        app.mainViewModel.userLocation.removeAt(locationinddex)

        var primlocationinddex = -1
        for(i in app.mainViewModel.primitiveLocation) {
            if(weather.address.equals(i.third)) {
                primlocationinddex = app.mainViewModel.primitiveLocation.indexOf(i)
            }
        }
        app.mainViewModel.primitiveLocation.removeAt(primlocationinddex)
    }

    fun favoriteWeather(weather : ModelWeather, locationlist : ArrayList<Triple<Int,Int, String>>, weatherlist : MutableList<ModelWeather>, oWeatherList : MutableLiveData<List<ModelWeather>>, app: MyApplication) {
        val index = locationlist.indexOf(Triple(weather.nx,weather.ny, weather.address))
        val templocation = locationlist.get(index)
        locationlist.remove(templocation)
        locationlist.add(0,templocation)
        weatherlist.remove(weather)
        weatherlist.add(0,weather)
        oWeatherList.value = weatherlist

        if(app.uvlistflag) {
            var uvModel : UVItem = UVItem()
            for(i in app.uvListViewModel.UVList) {
                if(weather.address.equals(i.address)) {
                    uvModel = i
                    break
                }
            }
            app.uvListViewModel.UVList.remove(uvModel)
            app.uvListViewModel.UVList.add(0, uvModel)
            app.uvListViewModel._oUVList.value = app.uvListViewModel.UVList
        }

        if(app.dustlistflag) {
            var dustModel : DustItem = DustItem()
            for(i in app.dustListVIewModel.DustList) {
                if(weather.address.equals(i.address)) {
                    dustModel = i
                    break
                }
            }
            app.dustListVIewModel.DustList.remove(dustModel)
            app.dustListVIewModel.DustList.add(0, dustModel)
            app.dustListVIewModel._oDustList.value = app.dustListVIewModel.DustList
        }
    }
}
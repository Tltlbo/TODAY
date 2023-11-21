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
        val parseaddr = weather.address.split(" ")
        locationlist.remove(Triple(weather.nx,weather.ny, parseaddr[1])) // 이거 안 지워지고 있었음
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
        val parseaddr = weather.address.split(" ")
        val index = locationlist.indexOf(Triple(weather.nx,weather.ny, parseaddr[1]))
        val templocation = locationlist.get(index)

        locationlist.remove(templocation)
        locationlist.add(0,templocation)
        weatherlist.remove(weather)
        weatherlist.add(0,weather)
        oWeatherList.value = weatherlist

        //지금 이거 다른 테이블에서 하나도 업데이트 안되고 있음
        // mainviewmodel에서도 안되는듯 옮긴 mainviewmodel이 업데이트 안되는 중

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

        var location : Triple<Int,Int,String> = Triple(0,0,"")
        for(i in app.mainViewModel.userLocation) {
            if(weather.address.equals(i.third)) {
                location = i
            }
        }
        Log.e("location", location.third)
        app.mainViewModel.userLocation.remove(location)
        app.mainViewModel.userLocation.add(0,location)
        Log.e("Count",app.mainViewModel.userLocation.count().toString())

        var primlocation : Triple<Double,Double,String> = Triple(0.0,0.0,"")
        for(i in app.mainViewModel.primitiveLocation) {
            if(weather.address.equals(i.third)) {
                primlocation = i
            }
        }
        app.mainViewModel.primitiveLocation.remove(primlocation)
        app.mainViewModel.primitiveLocation.add(0,primlocation)
        Log.e("Count",app.mainViewModel.primitiveLocation.count().toString())
    }
}
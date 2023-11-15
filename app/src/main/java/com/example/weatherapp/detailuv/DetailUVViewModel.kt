package com.example.weatherapp.detailuv

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.MyApplication
import com.example.weatherapp.R
import com.example.weatherapp.data.UVItem

class DetailUVViewModel : ViewModel() {

    var uv = UVItem()

    fun getUVImage(uv : Int) : Int {
        if(uv <= 2) { return R.drawable.bestuv }
        else if (uv <= 5) {return R.drawable.gooduv}
        else if (uv <= 7) {return R.drawable.betteruv}
        else if (uv <= 10) {return R.drawable.baduv }
        else {return R.drawable.terroruv}
    }

    fun deleteuvInfo(uv : UVItem, locationlist : ArrayList<Triple<Double,Double, String>>, uvlist : MutableList<UVItem>, ouvlist : MutableLiveData<List<UVItem>>, app : MyApplication) {

        var index = -1
        for( i in locationlist) {
            if(i.third.equals(uv.address)) {
                index = locationlist.indexOf(i)
            }
        }

        if(app.dustlistflag) {
            var dustindex = -1
            for(i in app.dustListVIewModel.DustList) {
                if(uv.address.equals(i.address)) {
                    dustindex = app.dustListVIewModel.DustList.indexOf(i)
                    break
                }
            }
            app.dustListVIewModel.DustList.removeAt(dustindex)
            app.dustListVIewModel._oDustList.value = app.dustListVIewModel.DustList
        }

        if(app.weatherlistflag) {
            var weatherindex = -1
            for(i in app.weatherListViewModel.WeatherList) {
                if(uv.address.equals(i.address)) {
                    weatherindex = app.weatherListViewModel.WeatherList.indexOf(i)
                    break
                }
            }
            app.weatherListViewModel.WeatherList.removeAt(weatherindex)
            app.weatherListViewModel._oWeatherList.value = app.weatherListViewModel.WeatherList
        }

        locationlist.removeAt(index)

        uvlist.remove(uv)
        ouvlist.value = uvlist

        var locationinddex = -1
        for(i in app.mainViewModel.userLocation) {
            if(uv.address.equals(i.third)) {
                locationinddex = app.mainViewModel.userLocation.indexOf(i)
            }
        }
        app.mainViewModel.userLocation.removeAt(locationinddex)

        var primlocationinddex = -1
        for(i in app.mainViewModel.primitiveLocation) {
            if(uv.address.equals(i.third)) {
                primlocationinddex = app.mainViewModel.primitiveLocation.indexOf(i)
            }
        }
        app.mainViewModel.primitiveLocation.removeAt(primlocationinddex)
    }
}
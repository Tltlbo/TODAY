package com.example.weatherapp.detaildust

import DustItem
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.MyApplication
import com.example.weatherapp.R
import com.example.weatherapp.data.ModelWeather

class DetailDustViewModel : ViewModel() {

    var dust = DustItem()


    fun getDustImage(pm10Value : Int) : Int {
        if(pm10Value <= 50) { return R.drawable.best }
        else if (pm10Value <= 100) {return R.drawable.good}
        else if (pm10Value <= 250) {return R.drawable.bad}
        else {return R.drawable.terror}
    }

    fun getTotalDustInfo(pm10Value : Int) : String {
        if(pm10Value.toInt() <= 50) { return "좋음" }
        else if (pm10Value.toInt() <= 100) { return "보통" }
        else if (pm10Value.toInt() <= 250) {return "나쁨" }
        else {return "매우 나쁨" }
    }

    fun deletedustInfo(dust : DustItem, locationlist : ArrayList<Triple<Double,Double, String>>, dustlist : MutableList<DustItem>, oDustList : MutableLiveData<List<DustItem>>, app : MyApplication) {

        var index = -1
        for(i in locationlist) {
            if(i.third.equals(dust.address)) {
                index = locationlist.indexOf(i)
            }
        }

        if(app.uvlistflag) {
            var uvindex = -1
            for(i in app.uvListViewModel.UVList) {
                if(dust.address.equals(i.address)) {
                    uvindex = app.uvListViewModel.UVList.indexOf(i)
                    break
                }
            }
            app.uvListViewModel.UVList.removeAt(uvindex)
            app.uvListViewModel._oUVList.value = app.uvListViewModel.UVList
        }

        if(app.weatherlistflag) {
            var weatherindex = -1
            for(i in app.weatherListViewModel.WeatherList) {
                if(dust.address.equals(i.address)) {
                    weatherindex = app.weatherListViewModel.WeatherList.indexOf(i)
                    break
                }
            }
            app.weatherListViewModel.WeatherList.removeAt(weatherindex)
            app.weatherListViewModel._oWeatherList.value = app.weatherListViewModel.WeatherList
        }

        locationlist.removeAt(index)

        dustlist.remove(dust)
        oDustList.value = dustlist

        var locationinddex = -1
        for(i in app.mainViewModel.userLocation) {
            if(dust.address.equals(i.third)) {
                locationinddex = app.mainViewModel.userLocation.indexOf(i)
            }
        }
        app.mainViewModel.userLocation.removeAt(locationinddex)

        var primlocationinddex = -1
        for(i in app.mainViewModel.primitiveLocation) {
            if(dust.address.equals(i.third)) {
                primlocationinddex = app.mainViewModel.primitiveLocation.indexOf(i)
            }
        }
        app.mainViewModel.primitiveLocation.removeAt(primlocationinddex)
    }



}
package com.example.weatherapp.detaildust

import DustItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    fun deletedustInfo(dust : DustItem, locationlist : ArrayList<Triple<Double,Double, String>>, dustlist : MutableList<DustItem>, oDustList : MutableLiveData<List<DustItem>>) {

        var index = -1
        for( i in locationlist) {
            if(i.third.equals(dust.address)) {
                index = locationlist.indexOf(i)
            }
        }

        locationlist.removeAt(index)

        dustlist.remove(dust)
        oDustList.value = dustlist
    }



}
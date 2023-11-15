package com.example.weatherapp.detailuv

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    fun deleteuvInfo(uv : UVItem, locationlist : ArrayList<Triple<Double,Double, String>>, uvlist : MutableList<UVItem>, ouvlist : MutableLiveData<List<UVItem>>) {

        var index = -1
        for( i in locationlist) {
            if(i.third.equals(uv.address)) {
                index = locationlist.indexOf(i)
            }
        }

        locationlist.removeAt(index)

        uvlist.remove(uv)
        ouvlist.value = uvlist
    }
}
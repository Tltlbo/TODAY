package com.example.weatherapp


import android.graphics.Point
import androidx.lifecycle.ViewModel
import com.example.weatherapp.component.Common
import com.example.weatherapp.data.ModelTemp

class MainViewModel : ViewModel() {

    var atemp = ModelTemp()

    var nx : Int = 0
    var ny : Int = 0
    var address : String = "주소"

    var userLocation : MutableList<Triple<Int, Int, String>> = mutableListOf(

    ) //요거는 ModelUser로

    var primitiveLocation : MutableList<Triple<Double,Double,String>> = mutableListOf(
        Triple(33.4110,126.4237, "제주"),
        Triple(35.2052,128.1298, "진주"),
        Triple(35.5537,129.2381, "울산"),
        Triple(36.5438,127.3275, "세종"),
        Triple(35.8356,128.6277, "대구"),
        Triple(38.1940,128.5735, "강원")
    )

    fun modifyConv() {
        for(i in primitiveLocation) {
            val point = Common().dfsXyConv(i.first,i.second)
            userLocation.add(Triple(point.x,point.y,i.third))
        }
    }


}
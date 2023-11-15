package com.example.weatherapp


import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weatherapp.component.Common
import com.example.weatherapp.data.ModelTemp
import com.example.weatherapp.data.TEMP
import com.example.weatherapp.data.TEMPITEM
import com.example.weatherapp.network.TempObject
import retrofit2.Call
import retrofit2.Response
import kotlin.math.min

class MainViewModel : ViewModel() {

    var atemp = ModelTemp(maxTemp = "0", minTemp = "0")
    var addflag = false

    var nx : Int = 0
    var ny : Int = 0
    var address : String = "주소"

    var userLocation : MutableList<Triple<Int, Int, String>> = mutableListOf(

    ) //요거는 ModelUser로

    var primitiveLocation : MutableList<Triple<Double,Double,String>> = mutableListOf(
        Triple(33.4110,126.4237, "대한민국 제주특별자치도"),
        Triple(35.2052,128.1298, "대한민국 경상남도"),
        Triple(35.5537,129.2381, "대한민국 울산광역시"),
        Triple(36.5438,127.3275, "대한민국 세종특별자치시"),
        Triple(35.8356,128.6277, "대한민국 대구광역시"),
        Triple(38.1940,128.5735, "대한민국 강원도")
    )

    fun modifyConv() {
        for(i in primitiveLocation) {
            val point = Common().dfsXyConv(i.first,i.second)
            userLocation.add(Triple(point.x,point.y,i.third))
        }
    }



}
package com.example.weatherapp


import android.graphics.Point
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.ModelTemp

class MainViewModel : ViewModel() {

    var atemp = ModelTemp()

    var nx : Int = 0
    var ny : Int = 0
    var address : String = "주소"

    var userLocation : MutableList<Triple<Int, Int, String>> = mutableListOf(
        Triple(60,127, "서울특별시"),
        Triple(68,100, "대전광역시"),
        Triple(60,121, "경기도 수원시 장안구"),
        Triple(53,120, "경기도 안산시단원구 대부동"),
        Triple(56,133, "경기도 파주시 문산읍"),
        Triple(71,106, "충청북도 청주시상당구 남일"),
        Triple(63,108, "충청남도 천안시동남구 풍세면"),
        Triple(56,87, "전라북도 부안군"),
        Triple(57,71, "전라남도 나주시 금천면")
    ) //요거는 ModelUser로



}
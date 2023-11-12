package com.example.weatherapp


import android.graphics.Point
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.ModelTemp

class MainViewModel : ViewModel() {

    var atemp = ModelTemp()

    var userLocation : MutableList<Pair<Int,Int>> = mutableListOf(
        Pair(60,127),
        Pair(68,100),
        Pair(60,121),
        Pair(53,120),
        Pair(56,133),
        Pair(71,106),
        Pair(63,108),
        Pair(56,91),
        Pair(56,87),
        Pair(57,71)
    ) //요거는 ModelUser로

}
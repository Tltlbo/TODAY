package com.example.weatherapp.data

data class ModelRecentWeather (
    var rainType: String = "",      // 강수 형태
    var humidity: String = "",      // 습도
    var sky: String = "",           // 하늘 상태
    var temp: String = "",          // 기온
    var address: String = "",      // 주소
)


package com.example.weatherapp.detailweather

import android.graphics.ColorSpace.Model
import android.util.Log
import android.view.Display.Mode
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.adapter.WeatherAdapter
import com.example.weatherapp.component.Common
import com.example.weatherapp.data.ModelWeather
import com.example.weatherapp.data.WEATHER
import com.example.weatherapp.data.WEATHERITEM
import com.example.weatherapp.network.WeatherObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeatherListViewModel : ViewModel() {
    var userLocationList : ArrayList<Triple<Int, Int, String>> = arrayListOf()
    var WeatherList : MutableList<ModelWeather> = mutableListOf() //이친구 순서가 뒤죽박죽되버림
    var index = 0



    var _oWeatherList = MutableLiveData<List<ModelWeather>>()
    var oWeatherList : LiveData<List<ModelWeather>> = _oWeatherList

    init {
        _oWeatherList.value = WeatherList
    }


    fun getWeather(weatherList : MutableList<ModelWeather>) {
        for (i in weatherList) {
            setWeather(i)
        }
    }

    fun callLocationList(locations : MutableList<Triple<Int,Int,String>>) {

        for (i in locations) {
            userLocationList.add(i)
            WeatherList.add(ModelWeather(nx = i.first, ny = i.second, address = i.third))
        }
    }



    private fun setWeather(weather : ModelWeather) {
        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
        var baseDate = "20230809"  // 발표 일자
        var baseTime = "1100"
        val cal = Calendar.getInstance()
        baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time) // 현재 날짜
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시각
        val timeM = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 분
        // API 가져오기 적당하게 변환
        baseTime = Common().getBaseTime(timeH, timeM)
        // 현재 시각이 00시이고 45분 이하여서 baseTime이 2330이면 어제 정보 받아오기
        if (timeH == "00" && baseTime == "2330") {
            cal.add(Calendar.DATE, -1).toString()
            baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }

        // 날씨 정보 가져오기
        // (한 페이지 결과 수 = 60, 페이지 번호 = 1, 응답 자료 형식-"JSON", 발표 날싸, 발표 시각, 예보지점 좌표)
        val call = WeatherObject.getRetrofitService().getWeather(40, 1, "JSON", baseDate, baseTime, weather.nx, weather.ny)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            // 응답 성공 시
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    // 날씨 정보 가져오기
                    val it: List<WEATHERITEM> = response.body()!!.response.body.items.item


                    // 배열 채우기
//                    val totalCount = response.body()!!.response.body.totalCount - 1
                    for (i in 0..39) {

                        when(it[i].category) {
                            "PTY" -> if(!weather.rainType.equals("None")) {continue} else {weather.rainType = it[i].fcstValue}     // 강수 형태
                            "REH" -> if(!weather.humidity.equals("None")) {continue}else {weather.humidity = it[i].fcstValue}     // 습도
                            "SKY" -> if(!weather.sky.equals("None")) {continue} else {weather.sky = it[i].fcstValue}          // 하늘 상태
                            "T1H" -> if(!weather.temp.equals("None")) {continue} else {weather.temp = it[i].fcstValue}         // 기온
                            else -> continue
                        }
                    }
                    _oWeatherList.value = WeatherList


                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

}
package com.example.weatherapp.detailweather

import android.app.Application
import android.graphics.ColorSpace.Model
import android.util.Log
import android.view.Display.Mode
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.MyApplication
import com.example.weatherapp.adapter.WeatherAdapter
import com.example.weatherapp.component.Common
import com.example.weatherapp.data.ModelWeather
import com.example.weatherapp.data.WEATHER
import com.example.weatherapp.data.WEATHERITEM
import com.example.weatherapp.network.WeatherObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class userlocationModel(
    var id : String? = null,
    var nx0 : Double? = null,
    var ny0 : Double? = null,
    var addr0 : String? = null,
    var nx1 : Double? = null,
    var ny1 : Double? = null,
    var addr1 : String? = null,
    var nx2 : Double? = null,
    var ny2 : Double? = null,
    var addr2 : String? = null,
    var nx3 : Double? = null,
    var ny3 : Double? = null,
    var addr3 : String? = null,
    var nx4 : Double? = null,
    var ny4 : Double? = null,
    var addr4 : String? = null,
    var nx5 : Double? = null,
    var ny5 : Double? = null,
    var addr5 : String? = null,
    var nx6 : Double? = null,
    var ny6 : Double? = null,
    var addr6 : String? = null,
    var nx7 : Double? = null,
    var ny7 : Double? = null,
    var addr7 : String? = null,
    var nx8 : Double? = null,
    var ny8 : Double? = null,
    var addr8 : String? = null,
    var nx9 : Double? = null,
    var ny9 : Double? = null,
    var addr9 : String? = null,

)
class WeatherListViewModel : ViewModel() {
    var userLocationList : ArrayList<Triple<Int, Int, String>> = arrayListOf()
    var WeatherList : MutableList<ModelWeather> = mutableListOf()

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
            val parseaddress = i.third.split(" ")
            val temp : Triple<Int, Int, String> = Triple(i.first,i.second,parseaddress[1])

            userLocationList.add(temp)
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

    fun saveuserInfo(app : MyApplication) {
        var auth = FirebaseAuth.getInstance()
        var firestore = FirebaseFirestore.getInstance()
        var userInfo = userlocationModel()
        userInfo.id = auth.currentUser?.email
        var index = 0
        for (i in app.mainViewModel.primitiveLocation) {
            if (index == 0) {userInfo.addr0 = i.third; userInfo.nx0 = i.first; userInfo.ny0 = i.second}
            else if (index == 1) {userInfo.addr1 = i.third; userInfo.nx1 = i.first; userInfo.ny1 = i.second}
            else if (index == 2) {userInfo.addr2 = i.third; userInfo.nx2 = i.first; userInfo.ny2 = i.second}
            else if (index == 3) {userInfo.addr3 = i.third; userInfo.nx3 = i.first; userInfo.ny3 = i.second}
            else if (index == 4) {userInfo.addr4 = i.third; userInfo.nx4 = i.first; userInfo.ny4 = i.second}
            else if (index == 5) {userInfo.addr5 = i.third; userInfo.nx5 = i.first; userInfo.ny5 = i.second}
            else if (index == 6) {userInfo.addr6 = i.third; userInfo.nx6 = i.first; userInfo.ny6 = i.second}
            else if (index == 7) {userInfo.addr7 = i.third; userInfo.nx7 = i.first; userInfo.ny7 = i.second}
            else if (index == 8) {userInfo.addr8 = i.third; userInfo.nx8 = i.first; userInfo.ny8 = i.second}
            else {userInfo.addr9 = i.third; userInfo.nx9 = i.first; userInfo.ny9 = i.second}
            index++
        }

        firestore.collection("addr").document(auth.currentUser?.email.toString()).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr0" ,userInfo.addr0)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx0" ,userInfo.nx0)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny0" ,userInfo.ny0)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr1" ,userInfo.addr1)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx1" ,userInfo.nx1)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny1" ,userInfo.ny1)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr2" ,userInfo.addr2)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx2" ,userInfo.nx2)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny2" ,userInfo.ny2)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr3" ,userInfo.addr3)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx3" ,userInfo.nx3)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny3" ,userInfo.ny3)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr4" ,userInfo.addr4)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx4" ,userInfo.nx4)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny4" ,userInfo.ny4)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr5" ,userInfo.addr5)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx5" ,userInfo.nx5)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny5" ,userInfo.ny5)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr6" ,userInfo.addr6)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx6" ,userInfo.nx6)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny6" ,userInfo.ny6)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr7" ,userInfo.addr7)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx7" ,userInfo.nx7)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny7" ,userInfo.ny7)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr8" ,userInfo.addr8)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx8" ,userInfo.nx8)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny8" ,userInfo.ny8)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr9" ,userInfo.addr9)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx9" ,userInfo.nx9)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny9" ,userInfo.ny9)
            } else {
                firestore.collection("addr").document(auth.currentUser?.email.toString()).set(userInfo)
            }
        }


    }

}
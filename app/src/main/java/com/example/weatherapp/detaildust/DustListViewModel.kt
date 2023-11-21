package com.example.weatherapp.detaildust

import DustItem
import ModelDust
import android.location.Address
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.Document
import com.example.weatherapp.data.KakaoMapModel
import com.example.weatherapp.data.ModelStation
import com.example.weatherapp.data.ModelWeather
import com.example.weatherapp.data.StationItem
import com.example.weatherapp.network.DustObject
import com.example.weatherapp.network.KakaoObject
import com.example.weatherapp.network.StationObject
import retrofit2.Call
import retrofit2.Response

class DustListViewModel : ViewModel() {

    var userLocationList : ArrayList<Triple<Double, Double, String>> = arrayListOf()
    var DustList : MutableList<DustItem> = mutableListOf()
    //메니페스트 연결, Application 클래스 연결 등등하고 xml파일 생성 해야 함

    var _oDustList = MutableLiveData<List<DustItem>>()
    var oDustList : LiveData<List<DustItem>> = _oDustList

    init {
        _oDustList.value = DustList
    }

    fun setDust() {
        for (i in DustList) {
            ConvCoord(i.y, i.x , i)
        }
    }

    fun callLocationList(locations : MutableList<Triple<Double,Double,String>>) {
        Log.e("Dust", locations.count().toString())
        for (i in locations) {
            val parseaddress = i.third.split(" ")
            val temp : Triple<Double, Double, String> = Triple(i.first,i.second,parseaddress[1])
            userLocationList.add(i)
            DustList.add(DustItem(address = i.third, x = i.first, y = i.second))
        }
    }

    private fun ConvCoord(nx: Double, ny: Double, ModelDust : DustItem) {
        val call = KakaoObject.getRetrofitService().getTmCoordinates(nx, ny)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<KakaoMapModel> {
            // 응답 성공 시
            override fun onResponse(call: Call<KakaoMapModel>, response: Response<KakaoMapModel>) {
                if (response.isSuccessful) {
                    val it: List<Document> = response.body()!!.documents

                    val totalCount = response.body()!!.documents.count() - 1
                    var tmx = 0.0
                    var tmy = 0.0
                    for (i in 0..totalCount) {
                        tmx = it[i].x
                        tmy = it[i].y
                    }

                    getStationName(tmx, tmy, ModelDust)

                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<KakaoMapModel>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    private fun getStationName(tmx: Double, tmy: Double, ModelDust: DustItem) {
        val call = StationObject.getRetrofitService().getStation("json", tmx, tmy)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<ModelStation> {
            // 응답 성공 시
            override fun onResponse(call: Call<ModelStation>, response: Response<ModelStation>) {
                if (response.isSuccessful) {
                    val it: List<StationItem> = response.body()!!.response.body.items

                    var stationaddr = ""


                    stationaddr = it[0].addr

                    ModelDust.stationName = stationaddr

                    getDust(it[0].stationName, ModelDust)

                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<ModelStation>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    private fun getDust(stationName : String, ModelDust: DustItem) {
        val call = DustObject.getRetrofitService().getDust("json", 100, 1, stationName)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<ModelDust> {
            // 응답 성공 시
            override fun onResponse(call: Call<ModelDust>, response: Response<ModelDust>) {
                if (response.isSuccessful) {
                    val it: List<DustItem> = response.body()!!.response.body.items

                    ModelDust.pm10Value = it[0].pm10Value
                    ModelDust.pm25Value = it[0].pm25Value
                    _oDustList.value = DustList
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<ModelDust>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }
}
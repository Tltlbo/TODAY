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



    fun callLocationList(locations : MutableList<Triple<Double,Double,String>>) {
        for (i in locations) {
            userLocationList.add(i)
            ConvCoord(i.second,i.first,i.third)
        }
    }

    private fun ConvCoord(nx: Double, ny: Double, address : String) {
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

                    Log.e("tmx", tmx.toString())
                    getStationName(tmx, tmy, address)

                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<KakaoMapModel>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    private fun getStationName(tmx: Double, tmy: Double, address: String) {
        val call = StationObject.getRetrofitService().getStation("json", tmx, tmy)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<ModelStation> {
            // 응답 성공 시
            override fun onResponse(call: Call<ModelStation>, response: Response<ModelStation>) {
                if (response.isSuccessful) {
                    val it: List<StationItem> = response.body()!!.response.body.items

                    var stationname = ""
                    var stationaddress = ""

                    stationname = it[0].stationName
                    stationaddress = it[0].addr
                    getDust(stationname, address, stationaddress)

                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<ModelStation>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    private fun getDust(stationName : String, address: String, stationAddress: String) {
        val call = DustObject.getRetrofitService().getDust("json", 100, 1, stationName)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<ModelDust> {
            // 응답 성공 시
            override fun onResponse(call: Call<ModelDust>, response: Response<ModelDust>) {
                if (response.isSuccessful) {
                    val it: List<DustItem> = response.body()!!.response.body.items


                    val totalCount = response.body()!!.response.body.items.count() - 1

                    DustList.add(DustItem(pm10Value = it[0].pm10Value, pm25Value = it[0].pm25Value, address = address, stationName = stationAddress))
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
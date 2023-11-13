package com.example.weatherapp

import DustItem
import ModelDust
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.data.Document
import com.example.weatherapp.data.KakaoMapModel
import com.example.weatherapp.data.ModelStation
import com.example.weatherapp.data.StationItem
import com.example.weatherapp.databinding.FragmentFinedustBinding
import com.example.weatherapp.network.DustObject
import com.example.weatherapp.network.KakaoObject
import com.example.weatherapp.network.StationObject
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class FineDustFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    private lateinit var viewBinding: FragmentFinedustBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val app = requireActivity().application as MyApplication
        viewBinding = FragmentFinedustBinding.inflate(layoutInflater)
        viewModel = app.mainViewModel

        requestLocation()
        return viewBinding.root
    }

    private fun getDust(stationName : String) {
        val call = DustObject.getRetrofitService().getDust("json", 100, 1, stationName)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<ModelDust> {
            // 응답 성공 시
            override fun onResponse(call: Call<ModelDust>, response: Response<ModelDust>) {
                if (response.isSuccessful) {
                    val it: List<DustItem> = response.body()!!.response.body.items


                    val totalCount = response.body()!!.response.body.items.count() - 1

                    viewBinding.dustinfoPm10.text = "미세먼지: " + it[0].pm10Value + "㎍/㎥"
                    viewBinding.dustinfoPm25.text = "초미세먼지: " + it[0].pm25Value + "㎍/㎥"

                    if(it[0].pm10Value.toInt() <= 50) { viewBinding.totalDustResult.text = "좋음" }
                    else if (it[0].pm10Value.toInt() <= 100) {viewBinding.totalDustResult.text = "보통"}
                    else if (it[0].pm10Value.toInt() <= 250) {viewBinding.totalDustResult.text = "나쁨"}
                    else {viewBinding.totalDustResult.text = "매우 나쁨"}

                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<ModelDust>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        val locationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

        try {
            // 나의 현재 위치 요청
            val locationRequest = LocationRequest.create()
            locationRequest.run {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 60 * 1000    // 요청 간격(1초)
            }
            val locationCallback = object : LocationCallback() {
                // 요청 결과
                override fun onLocationResult(p0: LocationResult) {
                    p0.let {
                        for (location in it.locations) {
                            ConvCoord(location.longitude, location.latitude)
                        }
                    }
                }
            }

            // 내 위치 실시간으로 감지
            Looper.myLooper()?.let {
                locationClient.requestLocationUpdates(
                    locationRequest, locationCallback,
                    it
                )
            }


        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun ConvCoord(nx: Double, ny: Double) {
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
                    getStationName(tmx, tmy)

                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<KakaoMapModel>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    private fun getStationName(tmx: Double, tmy: Double) {
        val call = StationObject.getRetrofitService().getStation("json", tmx, tmy)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<ModelStation> {
            // 응답 성공 시
            override fun onResponse(call: Call<ModelStation>, response: Response<ModelStation>) {
                if (response.isSuccessful) {
                    val it: List<StationItem> = response.body()!!.response.body.items

                    val totalCount = response.body()!!.response.body.items.count() - 1
                    var stationname = ""
                    var stationAddr = ""

                    stationAddr = it[0].addr
                    stationname = it[0].stationName
                    viewBinding.measuringStation.text = "측정소: " + stationAddr
                    getDust(stationname)

                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<ModelStation>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }
}
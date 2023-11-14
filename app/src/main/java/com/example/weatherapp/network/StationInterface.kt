package com.example.weatherapp.network

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.ModelStation
import com.example.weatherapp.data.ModelUV
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StationInterface {

    @GET("getNearbyMsrstnList?serviceKey=${BuildConfig.API_KEY}")
    fun getStation(
        @Query("returnType") returnType : String,
        @Query("tmX") tmx: Double,   // 한 페이지 경과 수
        @Query("tmY") tmy: Double,          // 페이지 번호
        @Query("ver") ver : Double = 1.1
    ): Call<ModelStation>
}

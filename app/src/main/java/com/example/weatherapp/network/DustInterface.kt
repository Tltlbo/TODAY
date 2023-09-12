package com.example.weatherapp.network

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.modeldustX
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DustInterface {
    @GET("getMinuDustFrcstDspth?serviceKey=${BuildConfig.API_KEY}")
    fun getDust(
        @Query("returnType") returnTyoe : String,
        @Query("numOfRows") num_of_rows: Int,   // 한 페이지 경과 수
        @Query("pageNo") page_no: Int,          // 페이지 번호
        @Query("searchDate") searchDate : String,
        @Query("InformCode") InformCode : String
    ): Call<modeldustX>

}
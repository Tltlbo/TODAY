package com.example.weatherapp.network

import ModelDust
import com.example.weatherapp.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DustInterface {
    @GET("getMsrstnAcctoRltmMesureDnsty?serviceKey=${BuildConfig.API_KEY}")
    fun getDust(
        @Query("returnType") returnType : String,
        @Query("numOfRows") num_of_rows: Int,   // 한 페이지 경과 수
        @Query("pageNo") page_no: Int,          // 페이지 번호
        @Query("stationName") stationName : String,
        @Query("dataTerm") dateTerm : String = "DAILY",
        @Query("ver") ver : String = "1.0"
    ): Call<ModelDust>

}
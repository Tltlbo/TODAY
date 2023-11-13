package com.example.weatherapp.network

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.ModelUV
import com.example.weatherapp.data.modeldustX
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UVInterface {

    @GET("getUVIdxV4?serviceKey=${BuildConfig.API_KEY}")
    fun getUV(
        @Query("pageNo") returnType : Int,
        @Query("numOfRows") num_of_rows: Int,   // 한 페이지 경과 수
        @Query("dataType") dataType: String,          // 페이지 번호
        @Query("areaNo") areaNo : String,
        @Query("time") time : String
    ): Call<ModelUV>
}
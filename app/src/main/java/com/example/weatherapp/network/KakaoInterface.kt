package com.example.weatherapp.network

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.KakaoMapModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoInterface {
    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_KEY}") // API_KEY 전달
    @GET("v2/local/geo/transcoord.json?output_coord=TM")
    fun getTmCoordinates(
        @Query("x") longitude: Double,
        @Query("y") latitude: Double
    ): Call<KakaoMapModel>
}
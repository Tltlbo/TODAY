package com.example.weatherapp.network

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.ModelUser
import com.example.weatherapp.data.WEATHER
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserInterface {

    @POST("information?")
    fun createUserInfo(
        @Query("accountId") accountId: String,
        @Query("endX") endX: Double,
        @Query("endY") endY: Double,
        @Query("introduction") introduction: String,
        @Query("startX") startX: Double,
        @Query("startY") startY: Double,
        @Query("stepCount") stepCount: Int,
        @Query("userName") userName : String
    ): Call<String>

    @PUT("information?")
    fun modifyUserInfo(
        @Query("accountId") accountId: String,
        @Query("endX") endX: Double,
        @Query("endY") endY: Double,
        @Query("introduction") introduction: String,
        @Query("startX") startX: Double,
        @Query("startY") startY: Double,
        @Query("stepCount") stepCount: Int,
        @Query("userName") userName : String
    ): Call<Void>

    @GET("information?")
    fun getUserInfo(
        @Query("userAccountId") AccountId : String
    ) : Call<ModelUser>
}
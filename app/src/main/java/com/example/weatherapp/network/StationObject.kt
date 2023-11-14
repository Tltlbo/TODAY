package com.example.weatherapp.network

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.StationItem
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object StationObject {
    private fun getRetrofit(): Retrofit {

        var interceptor =  HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        var client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(interceptor).build()


        var gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson)) // Json데이터를 사용자가 정의한 Java 객채로 변환해주는 라이브러리
            .client(client)
            .baseUrl(BuildConfig.URL_STATION)
            .build()
    }
    fun getRetrofitService(): StationInterface{
        return  getRetrofit().create(StationInterface::class.java) //retrofit객체 만듦!
    }
}
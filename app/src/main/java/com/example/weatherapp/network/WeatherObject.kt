package com.example.weatherapp.network

import com.google.gson.GsonBuilder
import com.example.weatherapp.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


object WeatherObject {


    private fun getRetrofit(): Retrofit{

        var interceptor =  HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        var client = OkHttpClient.Builder()
            .connectTimeout(1,TimeUnit.MINUTES)
            .readTimeout(1,TimeUnit.MINUTES)
            .addInterceptor(interceptor).build()


        var gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson)) // Json데이터를 사용자가 정의한 Java 객채로 변환해주는 라이브러리
            .client(client)
            .baseUrl(BuildConfig.URL_WEATHER)
            .build()
    }
    fun getRetrofitService(): WeatherInterface{
        return  getRetrofit().create(WeatherInterface::class.java) //retrofit객체 만듦!
    }
}
package com.example.weatherapp.detailweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp.R

class DetailWeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_weather)

        supportActionBar?.title = "마지막 위치의 날씨"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
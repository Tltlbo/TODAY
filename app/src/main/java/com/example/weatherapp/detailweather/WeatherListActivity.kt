package com.example.weatherapp.detailweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.adapter.WeatherCellAdapter
import com.example.weatherapp.data.ModelRecentWeather
import com.example.weatherapp.databinding.ActivityWeatherListBinding
import com.example.weatherapp.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class WeatherListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWeatherListBinding

    var WeatherList = mutableListOf (
        ModelRecentWeather("0","20", "1", "25", "Daegu")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherListBinding.inflate(layoutInflater)


        supportActionBar?.title = "마지막 위치의 날씨"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)

        binding.listView.adapter = WeatherCellAdapter(this, WeatherList)
    }
}
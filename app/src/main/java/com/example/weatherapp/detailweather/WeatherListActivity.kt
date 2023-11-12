package com.example.weatherapp.detailweather

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.ColorSpace.Model
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.MainActivity
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.MyApplication
import com.example.weatherapp.adapter.exWeatherAdapter
import com.example.weatherapp.data.ModelWeather
import com.example.weatherapp.databinding.ActivityWeatherListBinding
import com.example.weatherapp.network.WeatherObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class WeatherListActivity : AppCompatActivity() {
    private var baseDate = "20230809"  // 발표 일자
    private var baseTime = "1100"

    private lateinit var binding : ActivityWeatherListBinding
    lateinit var viewModel : WeatherListViewModel
    lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherListBinding.inflate(layoutInflater)
        val app = application as MyApplication

        supportActionBar?.title = "마지막 위치의 날씨"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = app.weatherListViewModel
        mainViewModel = app.mainViewModel
        setContentView(binding.root)


        if(viewModel.userLocationList.count() == 0) {
            viewModel.callLocationList(mainViewModel.userLocation)
            viewModel.getWeather(viewModel.WeatherList)
        }


        val listview = binding.weatherlist
        val weatherAdapter = exWeatherAdapter(this ,viewModel.WeatherList)
        weatherAdapter.itemClickListener = object : exWeatherAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val weather = viewModel.WeatherList[position]
                val intent = Intent(this@WeatherListActivity, DetailWeatherActivity::class.java)
                intent.putExtra("weather", weather)
                startActivity(intent)
            }
        }


        viewModel.oWeatherList.observe(this, Observer {weatherAdapter.updateList()})
        listview.adapter = weatherAdapter
        listview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

//        binding.listView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
//
//
//            val recentweather : ModelWeather = (binding.listView.adapter.getItem(position) as ModelWeather)
//
//            // new Intent(현재 Activity의 Context, 시작할 Activity 클래스)
//            val intent = Intent(this@WeatherListActivity, DetailWeatherActivity::class.java)
//            // putExtra(key, value)
//            intent.putExtra("recentweather", recentweather)
//            startActivity(intent)
//            // 이거 굳이 ModelRecentWeather 만들어서 해야함? 그냥 저거 가져와서 필요한거만 넣으면 되는거 아님?
//        })


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



}
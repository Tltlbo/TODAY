package com.example.weatherapp.detailweather

import android.content.Intent
import android.graphics.ColorSpace.Model
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.adapter.WeatherCellAdapter
import com.example.weatherapp.component.Common
import com.example.weatherapp.data.ModelWeather
import com.example.weatherapp.data.WEATHER
import com.example.weatherapp.data.WEATHERITEM
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

    var WeatherList : MutableList<ModelWeather> = mutableListOf(

        ModelWeather("1","20", "1", "20", "2300", "Daegu")
    )

    var userLocation : MutableList<Pair<Int,Int>> = mutableListOf(
        Pair(63,89)
    ) //여기다가 DB로 부터 불러온 유저의 위치 값을 받은 후, WeatherList에 Api호출을 통해 얻은 정보를 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherListBinding.inflate(layoutInflater)




        supportActionBar?.title = "마지막 위치의 날씨"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)

        binding.listView.adapter = WeatherCellAdapter(this, WeatherList)

        binding.listView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->


            val recentweather : ModelWeather = (binding.listView.adapter.getItem(position) as ModelWeather)

            // new Intent(현재 Activity의 Context, 시작할 Activity 클래스)
            val intent = Intent(this@WeatherListActivity, DetailWeatherActivity::class.java)
            // putExtra(key, value)
            intent.putExtra("recentweather", recentweather)
            startActivity(intent)
            // 이거 굳이 ModelRecentWeather 만들어서 해야함? 그냥 저거 가져와서 필요한거만 넣으면 되는거 아님?
        })


    }

}
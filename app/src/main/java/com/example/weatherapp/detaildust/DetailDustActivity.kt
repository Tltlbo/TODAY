package com.example.weatherapp.detaildust

import DustItem
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp.MyApplication
import com.example.weatherapp.R
import com.example.weatherapp.data.ModelWeather
import com.example.weatherapp.databinding.ActivityDetailDustBinding
import com.example.weatherapp.databinding.ActivityDetailWeatherBinding
import com.example.weatherapp.detailweather.DetailWeatherViewModel
import com.example.weatherapp.detailweather.WeatherListViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailDustActivity : AppCompatActivity() {

    private lateinit var viewBinding : ActivityDetailDustBinding
    lateinit var viewModel : DetailDustViewModel
    lateinit var listViewModel: DustListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as MyApplication
        viewBinding = ActivityDetailDustBinding.inflate(layoutInflater)
        viewModel = app.detaildustVIewModel
        listViewModel = app.dustListVIewModel
        viewModel.dust = intent.getParcelableExtra("dust") ?: DustItem()

        // uv에 삭제 기능 추가하고 각 리스트들 최신화 하는 거 해야 함
        supportActionBar?.title = "마지막 위치의 미세먼지"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewBinding.address.text = viewModel.dust.address
        viewBinding.measuringStation.text = viewModel.dust.stationName
        viewBinding.imgDust.setImageResource(viewModel.getDustImage(viewModel.dust.pm10Value.toInt()))
        viewBinding.totalDustResult.text = viewModel.getTotalDustInfo(viewModel.dust.pm10Value.toInt())
        viewBinding.dustinfoPm10.text = "미세먼지: " + viewModel.dust.pm10Value + "㎍/㎥"
        viewBinding.dustinfoPm25.text = "초미세먼지: " + viewModel.dust.pm25Value + "㎍/㎥"


        viewBinding.deletebtn.setOnClickListener {
            viewModel.deletedustInfo(viewModel.dust,listViewModel.userLocationList,listViewModel.DustList, listViewModel._oDustList)
            finish()
        }
        setContentView(viewBinding.root)
    }
}
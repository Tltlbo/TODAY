package com.example.weatherapp.detaildust

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.MyApplication
import com.example.weatherapp.R
import com.example.weatherapp.adapter.DustAdapter
import com.example.weatherapp.databinding.ActivityDustListBinding
import com.example.weatherapp.detailweather.DetailWeatherActivity

class DustListActivity : AppCompatActivity() {

    private var baseDate = "20230809"  // 발표 일자
    private var baseTime = "1100"

    private lateinit var binding : ActivityDustListBinding
    lateinit var viewModel : DustListViewModel
    lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDustListBinding.inflate(layoutInflater)
        val app = application as MyApplication

        supportActionBar?.title = "마지막 위치의 미세먼지"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = app.dustListVIewModel
        mainViewModel = app.mainViewModel
        setContentView(binding.root)


        if(viewModel.userLocationList.count() == 0) {
            viewModel.callLocationList(mainViewModel.primitiveLocation)
            viewModel.setDust()
        }


        val listview = binding.dustlist
        val dustAdapter = DustAdapter(this, viewModel.DustList)
        dustAdapter.itemClickListener = object : DustAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val dust = viewModel.DustList[position]
                val intent = Intent(this@DustListActivity, DetailDustActivity::class.java)
                intent.putExtra("dust", dust)
                startActivity(intent)
            }
        }


        viewModel.oDustList.observe(this, Observer {dustAdapter.updateList()})
        listview.adapter = dustAdapter
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


}
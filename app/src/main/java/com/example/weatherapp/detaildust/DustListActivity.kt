package com.example.weatherapp.detaildust

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        app.dustlistflag = true
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

        Toast.makeText(
            this,
            "정보를 불러오는 중입니다.",
            Toast.LENGTH_LONG
        ).show()


        viewModel.oDustList.observe(this, Observer {dustAdapter.updateList()})
        listview.adapter = dustAdapter
        listview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    override fun onPause() {
        super.onPause()
        val app = application as MyApplication
        viewModel.saveuserInfo(app)
        Toast.makeText(
            this,
            "마지막 위치 정보가 성공적으로 저장되었습니다.",
            Toast.LENGTH_LONG
        ).show()
    }


}
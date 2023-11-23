package com.example.weatherapp.detailuv

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
import com.example.weatherapp.adapter.UVAdapter
import com.example.weatherapp.databinding.ActivityDetailDustBinding
import com.example.weatherapp.databinding.ActivityUvlistBinding
import com.example.weatherapp.databinding.ActivityWeatherListBinding
import com.example.weatherapp.detaildust.DetailDustActivity
import com.example.weatherapp.detaildust.DetailDustViewModel
import com.example.weatherapp.detaildust.DustListViewModel

class UVListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUvlistBinding
    lateinit var viewModel : UVListViewModel
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUvlistBinding.inflate(layoutInflater)
        val app = application as MyApplication
        supportActionBar?.title = "마지막 위치의 자외선지수"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = app.uvListViewModel
        mainViewModel = app.mainViewModel
        app.uvlistflag = true
        setContentView(binding.root)

        if(viewModel.userLocationList.count() == 0) {
            viewModel.callLocationList(mainViewModel.primitiveLocation)
            viewModel.setUV(viewModel.UVList)
        }



        val listview = binding.uvlist
        val uvAdapter = UVAdapter(this, viewModel.UVList)
        uvAdapter.itemClickListener = object : UVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val uv = viewModel.UVList[position]
                val intent = Intent(this@UVListActivity, DetailuvActivity::class.java)
                intent.putExtra("uv", uv)
                startActivity(intent)
            }
        }


        viewModel.oUVList.observe(this, Observer {uvAdapter.updateList()})
        listview.adapter = uvAdapter
        listview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onPause() {
        super.onPause()
        val app = application as MyApplication
        viewModel.saveuserInfo(app)
    }
}
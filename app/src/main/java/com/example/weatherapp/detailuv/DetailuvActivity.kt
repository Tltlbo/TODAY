package com.example.weatherapp.detailuv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp.MyApplication
import com.example.weatherapp.R
import com.example.weatherapp.data.UVItem
import com.example.weatherapp.databinding.ActivityDetailDustBinding
import com.example.weatherapp.databinding.ActivityDetailuvBinding
import com.example.weatherapp.detaildust.DetailDustViewModel
import com.example.weatherapp.detaildust.DustListViewModel

class DetailuvActivity : AppCompatActivity() {

    private lateinit var viewBinding : ActivityDetailuvBinding
    lateinit var viewModel : DetailUVViewModel
    lateinit var listViewModel: UVListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val app = application as MyApplication
        viewBinding = ActivityDetailuvBinding.inflate(layoutInflater)
        viewModel = app.detailuvViewModel
        listViewModel = app.uvListViewModel
        viewModel.uv = intent.getParcelableExtra("uv") ?: UVItem()

        viewBinding.todayUVindex.text = viewModel.uv.maxUV.toString()
        viewBinding.UVImg.setImageResource(viewModel.getUVImage(viewModel.uv.maxUV))

        viewBinding.deletebtn.setOnClickListener {
            viewModel.deleteuvInfo(viewModel.uv,listViewModel.userLocationList,listViewModel.UVList, listViewModel._oUVList)
            finish()
        }

        setContentView(viewBinding.root)
    }
}
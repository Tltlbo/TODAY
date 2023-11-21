package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.data.ModelUV
import com.example.weatherapp.data.UVItem
import com.example.weatherapp.data.UVResponse
import com.example.weatherapp.databinding.FragmentUVBinding
import com.example.weatherapp.detaildust.DustListActivity
import com.example.weatherapp.detailuv.UVListActivity
import com.example.weatherapp.network.UVObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class UVFragment : Fragment() {

    private lateinit var viewBinding: FragmentUVBinding
    lateinit var viewModel : MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val app = requireActivity().application as MyApplication
        viewBinding = FragmentUVBinding.inflate(layoutInflater)
        viewModel = app.mainViewModel
        Log.e("checkUV", viewModel.userLocation.count().toString())


        selectaddresscode(viewModel.address)

        viewBinding.btnGotoList.setOnClickListener {
            startActivity(Intent(requireActivity(), UVListActivity::class.java))
        }

        return viewBinding.root
    }


    private fun getUV(areaNo : String) {
        val cal = Calendar.getInstance()
        var date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        date += "0000"
        val call = UVObject.getRetrofitService().getUV(1,10, "JSON", areaNo , date)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<ModelUV> {
            // 응답 성공 시
            override fun onResponse(call: Call<ModelUV>, response: Response<ModelUV>) {
                if (response.isSuccessful) {
                    val it : List<UVItem> = response.body()!!.response.body.items.item

                    val totalCount = response.body()!!.response.body.items.item.count() - 1
                    var maxUV = 0
                    for (i in 0..totalCount) {
                        val arr =   arrayOf(it[i].h0.toInt(),it[i].h3.toInt(),it[i].h6.toInt(),it[i].h9.toInt(),it[i].h12.toInt(),it[i].h15.toInt(),it[i].h18.toInt(),it[i].h21.toInt())
                        maxUV = arr.max()
                    }

                    viewBinding.todayUVindex.text = maxUV.toString()
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<ModelUV>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    private fun selectaddresscode(address : String) {
        Log.e("address", address)
        if(address.equals("서울특별시")) {getUV("1100000000")}
        else if(address.equals("부산광역시")) {getUV("2600000000")}
        else if(address.equals("대구광역시")) {getUV("2700000000")}
        else if(address.equals("인천광역시")) {getUV("2800000000")}
        else if(address.equals("광주광역시")) {getUV("2900000000")}
        else if(address.equals("대전광역시")) {getUV("3000000000")}
        else if(address.equals("울산광역시")) {getUV("3100000000")}
        else if(address.equals("세종특별자치시")) {getUV("3600000000")}
        else if(address.equals("경기도")) {getUV("4100000000")}
        else if(address.equals("충청북도")) {getUV("4300000000")}
        else if(address.equals("충청남도")) {getUV("4400000000")}
        else if(address.equals("전라북도")) {getUV("4500000000")}
        else if(address.equals("전라남도")) {getUV("4600000000")}
        else if(address.equals("경상북도")) {getUV("4700000000")}
        else if(address.equals("경상남도")) {getUV("4800000000")}
        else if(address.equals("제주특별자치도")) {getUV("5000000000")}
        else if(address.equals("이어도")) {getUV("5019000000")}
        else if(address.equals("강원도")) {getUV("5100000000")}
    }
}
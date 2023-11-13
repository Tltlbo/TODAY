package com.example.weatherapp

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

        getUV("1100000000")

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

//                viewBinding.tvError.text = "api fail : " +  t.message.toString() + "\n 다시 시도해주세요."
//                viewBinding.tvError.visibility = View.VISIBLE
                Log.d("api fail", t.message.toString())
            }
        })
    }
}
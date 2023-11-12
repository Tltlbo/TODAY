package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.Item
import com.example.weatherapp.data.modeldustX
import com.example.weatherapp.databinding.FragmentFinedustBinding
import com.example.weatherapp.network.DustObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class FineDustFragment : Fragment() {

    lateinit var viewModel : MainViewModel

    private lateinit var viewBinding : FragmentFinedustBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val app = requireActivity().application as MyApplication
        viewBinding = FragmentFinedustBinding.inflate(layoutInflater)
        viewModel = app.mainViewModel

        getDust()

        return viewBinding.root
    }

    private fun getDust() {
        val cal = Calendar.getInstance()
        var date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
        val call = DustObject.getRetrofitService().getDust("json",10, 1, date, "PM10")

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<modeldustX> {
            // 응답 성공 시
            override fun onResponse(call: Call<modeldustX>, response: Response<modeldustX>) {
                if (response.isSuccessful) {
                    val it : List<Item> = response.body()!!.response.body.items





                    val totalCount = response.body()!!.response.body.items.count() - 1

                    for (i in 0..totalCount) {
                        if(it[i].informData == date) {
                            when(it[i].informCode) {
                                "PM10" -> viewBinding.dustinfoPm10.text = it[i].informGrade
                                "PM25" -> viewBinding.dustinfoPm25.text = it[i].informGrade
                                else -> continue
                            }
                        }
                    }

                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<modeldustX>, t: Throwable) {

//                viewBinding.tvError.text = "api fail : " +  t.message.toString() + "\n 다시 시도해주세요."
//                viewBinding.tvError.visibility = View.VISIBLE
                Log.d("api fail", t.message.toString())
            }
        })
    }
}
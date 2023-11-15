package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.TEMP
import com.example.weatherapp.data.TEMPITEM
import com.example.weatherapp.databinding.FragmentRecommendBinding
import com.example.weatherapp.network.TempObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class RecommendFragment : Fragment() {

    private lateinit var viewBinding: FragmentRecommendBinding
    lateinit var viewModel : MainViewModel
    val cal = Calendar.getInstance()
    var baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val app = requireActivity().application as MyApplication
        viewBinding = FragmentRecommendBinding.inflate(layoutInflater)
        viewModel = app.mainViewModel

        return viewBinding.root
    }

    override fun onStart() {

        viewBinding.maxTemp.text = viewModel.atemp.maxTemp + "°C"
        viewBinding.minTemp.text = viewModel.atemp.minTemp + "°C"

        getTemp(viewModel.nx, viewModel.ny)
        super.onStart()
    }
    private fun updateWeatherImage() {
        val imageView = viewBinding.weatherImageView

        val maxTemp = viewModel.atemp.maxTemp
        val minTemp = viewModel.atemp.minTemp
        val averageTemp = (maxTemp.toDouble() + minTemp.toDouble()) / 2

        val imageResourceId = if (averageTemp > 27) {
            R.drawable.sleeveless
        } else if (averageTemp >= 23 && averageTemp <= 27) {
            R.drawable.t_shirt
        } else if (averageTemp >= 20 && averageTemp < 23) {
            R.drawable.shirts
        } else if (averageTemp >= 17 && averageTemp < 20) {
            R.drawable.hoodie
        } else if (averageTemp >= 12 && averageTemp < 17) {
            R.drawable.knit
        } else if (averageTemp >= 9 && averageTemp < 12) {
            R.drawable.jacket
        } else if (averageTemp >= 5 && averageTemp < 9) {
            R.drawable.coat
        } else {
            R.drawable.downjacket
        }

        imageView.setImageResource(imageResourceId)
    }

    private fun getTemp(nx : Int, ny : Int) {
        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기

        // 날씨 정보 가져오기
        // (한 페이지 결과 수 = 200, 페이지 번호 = 1, 응답 자료 형식-"JSON", 발표 날싸, 발표 시각, 예보지점 좌표)
        val call = TempObject.getRetrofitService().getTemp(200, 1, "JSON", (baseDate.toInt() - 1).toString(), "2300", nx, ny)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<TEMP> {
            // 응답 성공 시
            override fun onResponse(call: Call<TEMP>, response: Response<TEMP>) {
                if (response.isSuccessful) {
                    // 날씨 정보 가져오기
                    val it: List<TEMPITEM> = response.body()!!.response.body.items.item

                    var maxtemp: String = ""
                    var mintemp: String = ""



                    val totalCount = response.body()!!.response.body.items.item.count() - 1
                    for (i in 0..totalCount) {
                        when(it[i].category) {
                            "TMX" -> maxtemp = it[i].fcstValue
                            "TMN" -> mintemp = it[i].fcstValue
                            else -> continue
                        }
                    }
                    viewBinding.maxTemp.text = maxtemp + " °C"
                    viewBinding.minTemp.text = mintemp + " °C"
                    viewModel.atemp.maxTemp = maxtemp
                    viewModel.atemp.minTemp = mintemp
                    updateWeatherImage()
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<TEMP>, t: Throwable) {

                Log.d("api fail", t.message.toString())
            }
        })
    }
}
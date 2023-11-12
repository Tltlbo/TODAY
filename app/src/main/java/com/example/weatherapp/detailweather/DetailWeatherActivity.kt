package com.example.weatherapp.detailweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.MyApplication
import com.example.weatherapp.R
import com.example.weatherapp.adapter.DetailWeatherAdapter
import com.example.weatherapp.adapter.WeatherAdapter
import com.example.weatherapp.component.Common
import com.example.weatherapp.data.ModelWeather
import com.example.weatherapp.data.WEATHER
import com.example.weatherapp.data.WEATHERITEM
import com.example.weatherapp.databinding.ActivityDetailWeatherBinding
import com.example.weatherapp.network.WeatherObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailWeatherActivity : AppCompatActivity() {

    private lateinit var viewBinding : ActivityDetailWeatherBinding
    lateinit var viewModel : DetailWeatherViewModel
    lateinit var listViewModel: WeatherListViewModel
    private var baseDate = "20230809"  // 발표 일자
    private var baseTime = "1100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as MyApplication
        setContentView(R.layout.activity_detail_weather)
        viewBinding = ActivityDetailWeatherBinding.inflate(layoutInflater)
        viewBinding.tvDate.text = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Calendar.getInstance().time) + "날씨"
        viewModel = app.detailWeatherViewModel
        listViewModel = app.weatherListViewModel
        viewModel.weather = intent.getParcelableExtra("weather") ?: ModelWeather()


        supportActionBar?.title = "마지막 위치의 날씨"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val nx = viewModel.weather.nx
        val ny = viewModel.weather.ny
        setWeather(nx,ny)

        //버튼 관련

        viewBinding.deletebtn.setOnClickListener {
            viewModel.deleteweatherInfo(viewModel.weather,listViewModel.userLocationList,listViewModel.WeatherList)
            finish()
        }

        viewBinding.favoritebtn.setOnClickListener {
            viewModel.favoriteWeather(viewModel.weather, listViewModel.userLocationList,listViewModel.WeatherList, listViewModel._oWeatherList)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setWeather(nx : Int, ny : Int) {
        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
        val cal = Calendar.getInstance()
        baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time) // 현재 날짜
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시각
        val timeM = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 분
        // API 가져오기 적당하게 변환
        baseTime = Common().getBaseTime(timeH, timeM)
        // 현재 시각이 00시이고 45분 이하여서 baseTime이 2330이면 어제 정보 받아오기
        if (timeH == "00" && baseTime == "2330") {
            cal.add(Calendar.DATE, -1).toString()
            baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }

        // 날씨 정보 가져오기
        // (한 페이지 결과 수 = 60, 페이지 번호 = 1, 응답 자료 형식-"JSON", 발표 날싸, 발표 시각, 예보지점 좌표)
        val call = WeatherObject.getRetrofitService().getWeather(60, 1, "JSON", baseDate, baseTime, nx, ny)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            // 응답 성공 시
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    // 날씨 정보 가져오기
                    val it: List<WEATHERITEM> = response.body()!!.response.body.items.item

                    // 현재 시각부터 1시간 뒤의 날씨 6개를 담을 배열
                    val weatherArr = arrayOf(ModelWeather(), ModelWeather(), ModelWeather(), ModelWeather(), ModelWeather(), ModelWeather())

                    // 배열 채우기
                    var index = 0
                    val totalCount = response.body()!!.response.body.totalCount - 1
                    for (i in 0..totalCount) {
                        index %= 6
                        when(it[i].category) {
                            "PTY" -> weatherArr[index].rainType = it[i].fcstValue     // 강수 형태
                            "REH" -> weatherArr[index].humidity = it[i].fcstValue     // 습도
                            "SKY" -> weatherArr[index].sky = it[i].fcstValue          // 하늘 상태
                            "T1H" -> weatherArr[index].temp = it[i].fcstValue         // 기온
                            else -> continue
                        }
                        index++
                    }


                    weatherArr[0].fcstTime = "지금"
                    // 각 날짜 배열 시간 설정
                    for (i in 1..5) weatherArr[i].fcstTime = it[i].fcstTime

                    // 리사이클러 뷰에 데이터 연결

                    val adapter = DetailWeatherAdapter(weatherArr)
                    viewBinding.weatherRecyclerView.adapter = adapter
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {

                viewBinding.tvError.text = "api fail : " +  t.message.toString() + "\n 다시 시도해주세요."
                viewBinding.tvError.visibility = View.VISIBLE
                Log.d("api fail", t.message.toString())
            }
        })
    }
}
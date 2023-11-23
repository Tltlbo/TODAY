package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Point
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.adapter.WeatherAdapter
import com.example.weatherapp.component.Common
import com.example.weatherapp.data.ModelTemp
import com.example.weatherapp.data.ModelWeather
import com.example.weatherapp.data.TEMP
import com.example.weatherapp.data.TEMPITEM
import com.example.weatherapp.data.WEATHER
import com.example.weatherapp.data.WEATHERITEM
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.detailweather.WeatherListActivity
import com.example.weatherapp.login.InputNumberActivity
import com.example.weatherapp.network.TempObject
import com.example.weatherapp.network.WeatherObject
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class HomeFragment :  Fragment() {

    private var baseDate = "20230809"  // 발표 일자
    private var baseTime = "1100"      // 발표 시각
    private var curPoint: Point? = null    // 현재 위치의 격자 좌표를 저장할 포인트
    var checkactivityattatched = false

    lateinit var viewModel: MainViewModel

    private lateinit var viewBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val app = requireActivity().application as MyApplication
        viewBinding = FragmentHomeBinding.inflate(layoutInflater)
        viewBinding.tvDate.text = SimpleDateFormat(
            "MM월 dd일",
            Locale.getDefault()
        ).format(Calendar.getInstance().time) + "날씨"
        viewModel = app.mainViewModel
        viewModel.modifyConv()

        return viewBinding.root
    }

    override fun onStart() {

        checkactivityattatched = true
        requestLocation() // 생명 주기 문제 에러
        viewBinding.btnRefresh.setOnClickListener {
            requestLocation()
        }


        viewBinding.btnGotoList.setOnClickListener {
            startActivity(Intent(requireActivity(), WeatherListActivity::class.java))
        }
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        checkactivityattatched = false
    }


    // 날씨 가져와서 설정하기
    private fun setWeather(nx: Int, ny: Int) {
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
        val call =
            WeatherObject.getRetrofitService().getWeather(60, 1, "JSON", baseDate, baseTime, nx, ny)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            // 응답 성공 시
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    // 날씨 정보 가져오기
                    val it: List<WEATHERITEM> = response.body()!!.response.body.items.item

                    // 현재 시각부터 1시간 뒤의 날씨 6개를 담을 배열
                    val weatherArr = arrayOf(
                        ModelWeather(),
                        ModelWeather(),
                        ModelWeather(),
                        ModelWeather(),
                        ModelWeather(),
                        ModelWeather()
                    )

                    // 배열 채우기
                    var index = 0
                    val totalCount = response.body()!!.response.body.totalCount - 1
                    for (i in 0..totalCount) {
                        index %= 6
                        when (it[i].category) {
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
                    viewBinding.weatherRecyclerView.adapter = WeatherAdapter(weatherArr)

                    // 토스트 띄우기
                    //Toast.makeText(applicationContext, it[0].fcstDate + ", " + it[0].fcstTime + "의 날씨 정보입니다.", Toast.LENGTH_SHORT).show()
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {

                viewBinding.tvError.text = "api fail : " + t.message.toString() + "\n 다시 시도해주세요."
                viewBinding.tvError.visibility = View.VISIBLE
                Log.d("api fail", t.message.toString())
            }
        })
    }


    // 내 현재 위치의 위경도를 격자 좌표로 변환하여 해당 위치의 날씨정보 설정하기
    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        val locationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

        try {
            // 나의 현재 위치 요청
            val locationRequest = LocationRequest.create()
            locationRequest.run {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 60 * 1000    // 요청 간격(1초)
            }
            val locationCallback = object : LocationCallback() {
                // 요청 결과
                override fun onLocationResult(p0: LocationResult) {
                    p0.let {
                        for (location in it.locations) {
                            // 현재 위치의 위경도를 격자 좌표로 변환
                            curPoint = Common().dfsXyConv(location.latitude, location.longitude)
                            viewModel.nx = curPoint!!.x
                            viewModel.ny = curPoint!!.y
                            // 오늘 날짜 텍스트뷰 설정
                            viewBinding.tvDate.text =
                                SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(
                                    Calendar.getInstance().time
                                ) + " 날씨"
                            // nx, ny지점의 날씨 가져와서 설정하기
                            setWeather(curPoint!!.x, curPoint!!.y)
                            val address = getLocationName(location.latitude, location.longitude)

                            if (viewModel.primitiveLocation.count() == 10 && !viewModel.addflag) {
                                viewModel.addflag = true
                                viewModel.primitiveLocation.removeAt(0)
                                viewModel.userLocation.removeAt(0)
                                viewModel.primitiveLocation.add(
                                    Triple(
                                        location.latitude,
                                        location.longitude,
                                        address
                                    )
                                )
                                viewModel.userLocation.add(Triple(curPoint!!.x,curPoint!!.y, address))
                            } else if (!viewModel.addflag) {
                                viewModel.addflag = true
                                viewModel.primitiveLocation.add(
                                    Triple(
                                        location.latitude,
                                        location.longitude,
                                        address
                                    )
                                )
                                viewModel.userLocation.add(Triple(curPoint!!.x,curPoint!!.y, address))
                            }

                        }
                    }
                }
            }

            // 내 위치 실시간으로 감지
            Looper.myLooper()?.let {
                locationClient.requestLocationUpdates(
                    locationRequest, locationCallback,
                    it
                )
            }


        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun getLocationName(x: Double, y: Double): String {

        if (!checkactivityattatched) {
            return ""
        }

        val geocoder = Geocoder(requireActivity(), Locale.KOREA)

        var address: List<Address>? = null
        val str_Addr: String

        try {
            address = geocoder.getFromLocation(x, y, 7)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (address != null) {
            if (address.size != 0) {
                str_Addr = address[3].getAddressLine(0) // 3으로 하면 깔끔
                println(str_Addr) // 예상한대로 광역시는 도 출력 x
                val splitaddr = str_Addr.split(".")
                val addr = splitaddr[0].split(" ")
                viewBinding.address.text = splitaddr[0]
                viewModel.address = addr[1]
                return splitaddr[0]
            }
        }
        return ""
    }
}
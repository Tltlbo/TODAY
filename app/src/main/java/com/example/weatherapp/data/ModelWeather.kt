package com.example.weatherapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

// 날씨 정보를 담는 데이터 클래스
data class ModelWeather (
    @SerializedName("rainType") var rainType: String = "",      // 강수 형태
    @SerializedName("humidity") var humidity: String = "",      // 습도
    @SerializedName("sky") var sky: String = "",           // 하늘 상태
    @SerializedName("temp") var temp: String = "",          // 기온
    @SerializedName("fcstTime") var fcstTime: String = "",      // 예보시각
    @SerializedName("address") var address : String = "",
) : Serializable

// xml 파일 형식을 data class로 구현
data class WEATHER (val response : WEATHERRESPONSE)
data class WEATHERRESPONSE(val header : WEATHERHEADER, val body : WEATHERBODY)
data class WEATHERHEADER(val resultCode : Int, val resultMsg : String)
data class WEATHERBODY(val dataType : String, val items : WEATHERITEMS, val totalCount : Int)
data class WEATHERITEMS(val item : List<WEATHERITEM>)

// category : 자료 구분 코드, fcstDate : 예측 날짜, fcstTime : 예측 시간, fcstValue : 예보 값
data class WEATHERITEM(val category : String, val fcstDate : String, val fcstTime : String, val fcstValue : String)


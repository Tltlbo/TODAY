package com.example.weatherapp.data

import com.google.gson.annotations.SerializedName

// 온도 정보를 담는 데이터 클래스
data class ModelTemp (
    @SerializedName("maxTemp") var maxTemp: String = "",          // 최고기온
    @SerializedName("minTemp") var mintemp: String = "",          // 최저기온
    @SerializedName("fcstTime") var fcstTime: String = ""
)

// xml 파일 형식을 data class로 구현
data class TEMP (val response : TEMPRESPONSE)
data class TEMPRESPONSE(val header : TEMPHEADER, val body : TEMPBODY)
data class TEMPHEADER(val resultCode : Int, val resultMsg : String)
data class TEMPBODY(val dataType : String, val items : TEMPITEMS, val totalCount : Int)
data class TEMPITEMS(val item : List<TEMPITEM>)

// category : 자료 구분 코드, fcstDate : 예측 날짜, fcstTime : 예측 시간, fcstValue : 예보 값
data class TEMPITEM(val category : String, val fcstDate : String, val fcstTime : String, val fcstValue : String)


package com.example.weatherapp.detailuv

import DustItem
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.ModelUV
import com.example.weatherapp.data.UVItem
import com.example.weatherapp.network.UVObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UVListViewModel : ViewModel() {

    var userLocationList : ArrayList<Triple<Double, Double, String>> = arrayListOf()
    var UVList : MutableList<UVItem> = mutableListOf()

    var _oUVList = MutableLiveData<List<UVItem>>()
    var oUVList : LiveData<List<UVItem>> = _oUVList

    init {
        _oUVList.value = UVList
    }

    fun setUV(uvlist : MutableList<UVItem>) {
        for(i in uvlist) {
            selectaddresscode(i.address,i)
        }
    }


    fun callLocationList(locations : MutableList<Triple<Double,Double,String>>) {
        for (i in locations) {
            userLocationList.add(i)
            UVList.add(UVItem(address = i.third))
            _oUVList.value = UVList
        }
    }

    private fun getUV(areaNo : String, address: String, ModelUV: UVItem) {
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
                    ModelUV.maxUV = maxUV
                    ModelUV.address = address
                    _oUVList.value = UVList
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<ModelUV>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    fun selectaddresscode(address : String, ModelUV : UVItem) {
        if(address.equals("서울특별시")) {getUV("1100000000", address, ModelUV)}
        else if(address.equals("부산광역시")) {getUV("2600000000", address, ModelUV)}
        else if(address.equals("대구광역시")) {getUV("2700000000", address, ModelUV)}
        else if(address.equals("인천광역시")) {getUV("2800000000", address, ModelUV)}
        else if(address.equals("광주광역시")) {getUV("2900000000", address, ModelUV)}
        else if(address.equals("대전광역시")) {getUV("3000000000", address, ModelUV)}
        else if(address.equals("울산광역시")) {getUV("3100000000", address, ModelUV)}
        else if(address.equals("세종특별자치시")) {getUV("3600000000", address, ModelUV)}
        else if(address.equals("경기도")) {getUV("4100000000", address, ModelUV)}
        else if(address.equals("충청북도")) {getUV("4300000000", address, ModelUV)}
        else if(address.equals("충청남도")) {getUV("4400000000", address, ModelUV)}
        else if(address.equals("전라북도")) {getUV("4500000000", address, ModelUV)}
        else if(address.equals("전라남도")) {getUV("4600000000", address, ModelUV)}
        else if(address.equals("경상북도")) {getUV("4700000000", address, ModelUV)}
        else if(address.equals("경상남도")) {getUV("4800000000", address, ModelUV)}
        else if(address.equals("제주특별자치도")) {getUV("5000000000", address, ModelUV)}
        else if(address.equals("이어도")) {getUV("5019000000", address, ModelUV)}
        else if(address.equals("강원도")) {getUV("5100000000", address, ModelUV)}
    }
}
package com.example.weatherapp.detailuv

import DustItem
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.MyApplication
import com.example.weatherapp.data.ModelUV
import com.example.weatherapp.data.UVItem
import com.example.weatherapp.detailweather.userlocationModel
import com.example.weatherapp.network.UVObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
            val parseaddr = i.address.split(" ")
            selectaddresscode(parseaddr[1],i.address,i)
        }
    }


    fun callLocationList(locations : MutableList<Triple<Double,Double,String>>) {
        for (i in locations) {
            val parseaddress = i.third.split(" ")
            val temp : Triple<Double, Double, String> = Triple(i.first,i.second,parseaddress[1])
            userLocationList.add(temp)
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

    fun selectaddresscode(address : String, primaddr : String ,ModelUV : UVItem) {
        if(address.equals("서울특별시")) {getUV("1100000000", primaddr, ModelUV)}
        else if(address.equals("부산광역시")) {getUV("2600000000", primaddr ,ModelUV)}
        else if(address.equals("대구광역시")) {getUV("2700000000", primaddr, ModelUV)}
        else if(address.equals("인천광역시")) {getUV("2800000000", primaddr, ModelUV)}
        else if(address.equals("광주광역시")) {getUV("2900000000", primaddr, ModelUV)}
        else if(address.equals("대전광역시")) {getUV("3000000000", primaddr, ModelUV)}
        else if(address.equals("울산광역시")) {getUV("3100000000", primaddr, ModelUV)}
        else if(address.equals("세종특별자치시")) {getUV("3600000000", primaddr, ModelUV)}
        else if(address.equals("경기도")) {getUV("4100000000",primaddr, ModelUV)}
        else if(address.equals("충청북도")) {getUV("4300000000", primaddr, ModelUV)}
        else if(address.equals("충청남도")) {getUV("4400000000", primaddr, ModelUV)}
        else if(address.equals("전라북도")) {getUV("4500000000", primaddr, ModelUV)}
        else if(address.equals("전라남도")) {getUV("4600000000", primaddr, ModelUV)}
        else if(address.equals("경상북도")) {getUV("4700000000", primaddr, ModelUV)}
        else if(address.equals("경상남도")) {getUV("4800000000", primaddr, ModelUV)}
        else if(address.equals("제주특별자치도")) {getUV("5000000000", primaddr, ModelUV)}
        else if(address.equals("이어도")) {getUV("5019000000",primaddr, ModelUV)}
        else if(address.equals("강원도")) {getUV("5100000000", primaddr, ModelUV)}
    }

    fun saveuserInfo(app : MyApplication) {
        var auth = FirebaseAuth.getInstance()
        var firestore = FirebaseFirestore.getInstance()
        var userInfo = userlocationModel()
        userInfo.id = auth.currentUser?.email
        var index = 0
        for (i in app.mainViewModel.primitiveLocation) {
            if (index == 0) {userInfo.addr0 = i.third; userInfo.nx0 = i.first; userInfo.ny0 = i.second}
            else if (index == 1) {userInfo.addr1 = i.third; userInfo.nx1 = i.first; userInfo.ny1 = i.second}
            else if (index == 2) {userInfo.addr2 = i.third; userInfo.nx2 = i.first; userInfo.ny2 = i.second}
            else if (index == 3) {userInfo.addr3 = i.third; userInfo.nx3 = i.first; userInfo.ny3 = i.second}
            else if (index == 4) {userInfo.addr4 = i.third; userInfo.nx4 = i.first; userInfo.ny4 = i.second}
            else if (index == 5) {userInfo.addr5 = i.third; userInfo.nx5 = i.first; userInfo.ny5 = i.second}
            else if (index == 6) {userInfo.addr6 = i.third; userInfo.nx6 = i.first; userInfo.ny6 = i.second}
            else if (index == 7) {userInfo.addr7 = i.third; userInfo.nx7 = i.first; userInfo.ny7 = i.second}
            else if (index == 8) {userInfo.addr8 = i.third; userInfo.nx8 = i.first; userInfo.ny8 = i.second}
            else {userInfo.addr9 = i.third; userInfo.nx9 = i.first; userInfo.ny9 = i.second}
            index++
        }

        firestore.collection("addr").document(auth.currentUser?.email.toString()).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr0" ,userInfo.addr0)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx0" ,userInfo.nx0)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny0" ,userInfo.ny0)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr1" ,userInfo.addr1)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx1" ,userInfo.nx1)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny1" ,userInfo.ny1)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr2" ,userInfo.addr2)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx2" ,userInfo.nx2)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny2" ,userInfo.ny2)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr3" ,userInfo.addr3)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx3" ,userInfo.nx3)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny3" ,userInfo.ny3)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr4" ,userInfo.addr4)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx4" ,userInfo.nx4)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny4" ,userInfo.ny4)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr5" ,userInfo.addr5)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx5" ,userInfo.nx5)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny5" ,userInfo.ny5)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr6" ,userInfo.addr6)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx6" ,userInfo.nx6)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny6" ,userInfo.ny6)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr7" ,userInfo.addr7)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx7" ,userInfo.nx7)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny7" ,userInfo.ny7)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr8" ,userInfo.addr8)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx8" ,userInfo.nx8)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny8" ,userInfo.ny8)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("addr9" ,userInfo.addr9)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("nx9" ,userInfo.nx9)
                firestore.collection("addr").document(auth.currentUser?.email.toString()).update("ny9" ,userInfo.ny9)
            } else {
                firestore.collection("addr").document(auth.currentUser?.email.toString()).set(userInfo)
            }
        }


    }
}
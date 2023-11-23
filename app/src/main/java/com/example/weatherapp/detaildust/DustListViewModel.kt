package com.example.weatherapp.detaildust

import DustItem
import ModelDust
import android.location.Address
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.MyApplication
import com.example.weatherapp.data.Document
import com.example.weatherapp.data.KakaoMapModel
import com.example.weatherapp.data.ModelStation
import com.example.weatherapp.data.ModelWeather
import com.example.weatherapp.data.StationItem
import com.example.weatherapp.detailweather.userlocationModel
import com.example.weatherapp.network.DustObject
import com.example.weatherapp.network.KakaoObject
import com.example.weatherapp.network.StationObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Response

class DustListViewModel : ViewModel() {

    var userLocationList : ArrayList<Triple<Double, Double, String>> = arrayListOf()
    var DustList : MutableList<DustItem> = mutableListOf()
    //메니페스트 연결, Application 클래스 연결 등등하고 xml파일 생성 해야 함

    var _oDustList = MutableLiveData<List<DustItem>>()
    var oDustList : LiveData<List<DustItem>> = _oDustList

    init {
        _oDustList.value = DustList
    }

    fun setDust() {
        for (i in DustList) {
            ConvCoord(i.y, i.x , i)
        }
    }

    fun callLocationList(locations : MutableList<Triple<Double,Double,String>>) {
        Log.e("Dust", locations.count().toString())
        for (i in locations) {
            val parseaddress = i.third.split(" ")
            val temp : Triple<Double, Double, String> = Triple(i.first,i.second,parseaddress[1])
            userLocationList.add(i)
            DustList.add(DustItem(address = i.third, x = i.first, y = i.second))
        }
    }

    private fun ConvCoord(nx: Double, ny: Double, ModelDust : DustItem) {
        val call = KakaoObject.getRetrofitService().getTmCoordinates(nx, ny)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<KakaoMapModel> {
            // 응답 성공 시
            override fun onResponse(call: Call<KakaoMapModel>, response: Response<KakaoMapModel>) {
                if (response.isSuccessful) {
                    val it: List<Document> = response.body()!!.documents

                    val totalCount = response.body()!!.documents.count() - 1
                    var tmx = 0.0
                    var tmy = 0.0
                    for (i in 0..totalCount) {
                        tmx = it[i].x
                        tmy = it[i].y
                    }

                    getStationName(tmx, tmy, ModelDust)

                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<KakaoMapModel>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    private fun getStationName(tmx: Double, tmy: Double, ModelDust: DustItem) {
        val call = StationObject.getRetrofitService().getStation("json", tmx, tmy)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<ModelStation> {
            // 응답 성공 시
            override fun onResponse(call: Call<ModelStation>, response: Response<ModelStation>) {
                if (response.isSuccessful) {
                    val it: List<StationItem> = response.body()!!.response.body.items

                    var stationaddr = ""


                    stationaddr = it[0].addr

                    ModelDust.stationName = stationaddr

                    getDust(it[0].stationName, ModelDust)

                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<ModelStation>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    private fun getDust(stationName : String, ModelDust: DustItem) {
        val call = DustObject.getRetrofitService().getDust("json", 100, 1, stationName)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<ModelDust> {
            // 응답 성공 시
            override fun onResponse(call: Call<ModelDust>, response: Response<ModelDust>) {
                if (response.isSuccessful) {
                    val it: List<DustItem> = response.body()!!.response.body.items

                    ModelDust.pm10Value = it[0].pm10Value
                    ModelDust.pm25Value = it[0].pm25Value
                    _oDustList.value = DustList
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<ModelDust>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
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
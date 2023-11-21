package com.example.weatherapp


import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weatherapp.component.Common
import com.example.weatherapp.data.Document
import com.example.weatherapp.data.ModelTemp
import com.example.weatherapp.data.TEMP
import com.example.weatherapp.data.TEMPITEM
import com.example.weatherapp.network.TempObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import kotlin.math.min

data class tempuserlocation(
    var addr: String = "NULL",
    var nx: Double? = null,
    var ny: Double? = null,
)

class MainViewModel : ViewModel() {

    var atemp = ModelTemp(maxTemp = "0", minTemp = "0")
    var addflag = false
    var addlocflag = false
    var modifyConvFlag = false

    var nx: Int = 0
    var ny: Int = 0
    var address: String = "주소"

    var userLocation: MutableList<Triple<Int, Int, String>> = mutableListOf(

    )

    var primitiveLocation: MutableList<Triple<Double, Double, String>> = mutableListOf(
    )

    var templocation: MutableList<tempuserlocation> = mutableListOf(
        tempuserlocation(),
        tempuserlocation(),
        tempuserlocation(),
        tempuserlocation(),
        tempuserlocation(),
        tempuserlocation(),
        tempuserlocation(),
        tempuserlocation(),
        tempuserlocation(),
        tempuserlocation(),
    )

    fun modifyConv() {
        if(modifyConvFlag) {return}
        for (i in primitiveLocation) {
            val point = Common().dfsXyConv(i.first, i.second)
            userLocation.add(Triple(point.x, point.y, i.third))
        }
        modifyConvFlag = true
    }

    fun loadlocationInfo() = runBlocking {
        var auth = FirebaseAuth.getInstance()
        var firestore = FirebaseFirestore.getInstance()


        val documentref: DocumentReference =
            firestore.document("addr/" + auth.currentUser?.email.toString())

        launch(Dispatchers.IO) {
            try {
                val documentSnapshot = documentref.get().await()
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data

                    if (data != null) {
                        for ((key, value) in data) {
                            if (key.equals("nx0")) {
                                templocation[0].nx = value as Double
                            } else if (key.equals("ny0")) {
                                templocation[0].ny = value as Double
                            } else if (key.equals("addr0")) {
                                templocation[0].addr = value.toString()
                            } else if (key.equals("nx1")) {
                                if (value != null) templocation[1].nx = value as Double
                            } else if (key.equals("ny1")) {
                                if (value != null) templocation[1].ny = value as Double
                            } else if (key.equals("addr1")) {
                                if (value != null) templocation[1].addr = value.toString()
                            } else if (key.equals("nx2")) {
                                if (value != null) templocation[2].nx = value as Double
                            } else if (key.equals("ny2")) {
                                if (value != null) templocation[2].ny = value as Double
                            } else if (key.equals("addr2")) {
                                if (value != null) templocation[2].addr = value.toString()
                            } else if (key.equals("nx3")) {
                                if (value != null) templocation[3].nx = value as Double
                            } else if (key.equals("ny3")) {
                                if (value != null) templocation[3].ny = value as Double
                            } else if (key.equals("addr3")) {
                                if (value != null) templocation[3].addr = value.toString()
                            } else if (key.equals("nx4")) {
                                if (value != null) templocation[4].nx = value as Double
                            } else if (key.equals("ny4")) {
                                if (value != null) templocation[4].ny = value as Double
                            } else if (key.equals("addr4")) {
                                if (value != null) templocation[4].addr = value.toString()
                            } else if (key.equals("nx5")) {
                                if (value != null) templocation[5].nx = value as Double
                            } else if (key.equals("ny5")) {
                                if (value != null) templocation[5].ny = value as Double
                            } else if (key.equals("addr5")) {
                                if (value != null) templocation[5].addr = value.toString()
                            } else if (key.equals("nx6")) {
                                if (value != null) templocation[6].nx = value as Double
                            } else if (key.equals("ny6")) {
                                if (value != null) templocation[6].ny = value as Double
                            } else if (key.equals("addr6")) {
                                if (value != null) templocation[6].addr = value.toString()
                            } else if (key.equals("nx7")) {
                                if (value != null) templocation[7].nx = value as Double
                            } else if (key.equals("ny7")) {
                                if (value != null) templocation[7].ny = value as Double
                            } else if (key.equals("addr7")) {
                                if (value != null) templocation[7].addr = value.toString()
                            } else if (key.equals("nx8")) {
                                if (value != null) templocation[8].nx = value as Double
                            } else if (key.equals("ny8")) {
                                if (value != null) templocation[8].ny = value as Double
                            } else if (key.equals("addr8")) {
                                if (value != null) templocation[8].addr = value.toString()
                            } else if (key.equals("nx9")) {
                                if (value != null) templocation[9].nx = value as Double
                            } else if (key.equals("ny9")) {
                                if (value != null) templocation[9].ny = value as Double
                            } else if (key.equals("addr9")) {
                                if (value != null) templocation[9].addr = value.toString()
                            }
                        }
                        addloc(templocation)
                    }
                }
            } catch (e : Exception) {

            }
        }
    }

    fun addloc(templocation : MutableList<tempuserlocation>) {
        if (addlocflag) {return}
        for (i in templocation) {
            if(i.addr.equals("NULL")){break}
            primitiveLocation.add(Triple(i.nx!!,i.ny!!,i.addr))
        }
        addlocflag = true
    }

}

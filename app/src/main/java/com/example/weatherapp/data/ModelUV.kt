package com.example.weatherapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ModelUV(
    val response: UVResponse
)

data class UVResponse(
    val body: UVBody,
    val header: UVHeader
)

data class UVBody(
    val dataType: String,
    val items: UVItems,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

data class UVHeader(
    val resultCode: String,
    val resultMsg: String
)

@Parcelize
data class UVItem(
    val h0: String= "",
    val h12: String = "",
    val h15: String = "",
    val h18: String = "",
    val h21: String = "",
    val h3: String = "",
    val h6: String = "",
    val h9: String = "",
    var address : String = "",
    var maxUV : Int = 0
) : Parcelable

data class UVItems(
    val item: List<UVItem>
)
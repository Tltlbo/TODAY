package com.example.weatherapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import retrofit2.http.Query


@Parcelize
data class ModelUser(
    var accountId: String,
    var endX: Double,
    var endY: Double,
    var introduction: String,
    var startX: Double,
    var startY: Double,
    var stepCount: Int,
    var userName : String
) : Parcelable
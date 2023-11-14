package com.example.weatherapp.data

data class ModelStation(
    val response: StationResponse
)

data class StationResponse(
    val body: StationBody,
    val header: StationHeader
)

data class StationItem(
    val addr: String,
    val stationCode: String,
    val stationName: String,
    val tm: Double
)

data class StationHeader(
    val resultCode: String,
    val resultMsg: String
)

data class StationBody(
    val items: List<StationItem>,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)
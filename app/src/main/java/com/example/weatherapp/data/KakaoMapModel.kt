package com.example.weatherapp.data

data class KakaoMapModel(
    val documents: List<Document>,
    val meta: Meta
)

data class Document(
    val x: Double,
    val y: Double
)

data class Meta(
    val total_count: Int
)
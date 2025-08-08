package com.example.egypthousingprices

data class PredictionRequest(
    val region: String,
    val city: String,
    val size: Double,
    val bedrooms: Int,
    val bathrooms: Int,
    val floor: Int
)

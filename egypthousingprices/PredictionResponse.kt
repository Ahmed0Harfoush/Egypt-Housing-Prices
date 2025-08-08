package com.example.egypthousingprices

data class PredictionResponse(
    val success: Boolean,
    val region: String?,
    val city: String?,
    val prediction: Double?,
    val message: String? = null
)

package com.natureclean.api.model

data class PollutionPoint(
    val id: Int? = null,
    val name: String,
    val description: String? = null,
    val longitude: Double? = 0.0,
    val latitude: Double? = 0.0,
    val rating: Int,
    val type: Int,
)

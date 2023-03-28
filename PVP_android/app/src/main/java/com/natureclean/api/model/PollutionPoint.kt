package com.natureclean.api.model

data class PollutionPoint(
    val id: String? = null,
    val name: String,
    val description: String? = null,
    val longitude: Double? = 0.0,
    val latitude: Double? = 0.0,
    val rating: Int,
    val type: Int,
    val isActive: Int = 1,
    val reportCount: Int = 0,
)

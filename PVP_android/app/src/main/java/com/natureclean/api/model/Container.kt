package com.natureclean.api.model

data class Container(
    val name: String,
    val description: String,
    val longitude: Double? = 0.0,
    val latitude: Double? = 0.0,
    val type: String,
    val size: String
)

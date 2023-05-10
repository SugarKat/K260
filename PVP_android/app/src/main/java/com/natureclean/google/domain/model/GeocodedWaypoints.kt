package com.natureclean.google.domain.model

data class GeocodedWaypoints(
    val geocoder_status: String,
    val place_id: String,
    val types: List<String>
)
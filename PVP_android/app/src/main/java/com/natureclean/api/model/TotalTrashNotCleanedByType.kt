package com.natureclean.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TotalTrashNotCleanedByType(
    @Json(name = "1")
    val x1: List<Any>,
    @Json(name = "2")
    val x2: List<Any>,
    @Json(name = "3")
    val x3: List<Any>,
    @Json(name = "4")
    val x4: List<Any>,
    @Json(name = "5")
    val x5: List<Any>
)
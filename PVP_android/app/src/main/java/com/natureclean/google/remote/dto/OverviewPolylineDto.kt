package com.natureclean.google.remote.dto

import com.natureclean.google.domain.model.OverviewPolyline

data class OverviewPolylineDto(
    val points: String
){
    fun toOverviewPolyline(): OverviewPolyline {
        return OverviewPolyline(
            points = points
        )
    }
}

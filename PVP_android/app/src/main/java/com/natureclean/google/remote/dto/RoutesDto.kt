package com.natureclean.google.remote.dto

import com.natureclean.google.domain.model.Routes

data class RoutesDto(
    val summary: String,
    val overview_polyline: OverviewPolylineDto,
    val legs: List<LegsDto>
)
{
    fun toRoutes(): Routes{
        return Routes(
            summary = summary,
            overview_polyline = overview_polyline.toOverviewPolyline(),
            legs = legs.map { it.toLegs() }
        )
    }
}

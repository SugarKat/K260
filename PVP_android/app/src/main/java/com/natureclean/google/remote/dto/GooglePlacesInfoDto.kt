package com.natureclean.google.remote.dto

import com.natureclean.google.domain.model.GooglePlacesInfo

data class GooglePlacesInfoDto(
    val geocoded_waypoints: List<GeocodedWaypointsDto>,
    val routes: List<RoutesDto>,
    val status: String
){
    fun toGooglePlacesInfo(): GooglePlacesInfo {
        return GooglePlacesInfo(
            geocoded_waypoints = geocoded_waypoints.map { it.toGeocodedWaypoints() },
            routes = routes.map { it.toRoutes() },
            status = status
        )
    }
}

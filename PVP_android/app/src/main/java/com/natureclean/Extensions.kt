package com.natureclean

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import java.util.PriorityQueue
import java.util.Queue
import kotlin.math.*


fun checkMapPermissions(context: Context): MutableList<String> {
    val permissionsToRequest = mutableListOf<String>().apply {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
    return permissionsToRequest
}


fun calculateDistance(myDistance: LatLng, containerDistance: LatLng?): String? {
    var distanceTo: String? = null
    /*
    Calculate the great circle distance between two points
    on the earth (specified in decimal degrees)
    */
    if (containerDistance != null) {

        val radius = 6371.0 // Earth's radius in kilometers

        // Convert decimal degrees to radians
        val R = 6371 // Radius of the earth in km
        val latDistance = Math.toRadians(containerDistance.latitude - myDistance.latitude)
        val lonDistance = Math.toRadians(containerDistance.longitude - myDistance.longitude)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(myDistance.latitude)) * Math.cos(
            Math.toRadians(
                containerDistance.latitude
            )
        ) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = (R * c) // convert to km
        distanceTo = String.format("%.2f", distance) // format to 2 decimal places

    }
    return distanceTo
}

fun calculateDistance(point1: LatLng, point2: LatLng): Double {
    val lat1 = point1.latitude
    val lat2 = point2.latitude
    val lon1 = point1.longitude
    val lon2 = point2.longitude
    val r = 6371e3 // Earth radius in meters
    val phi1 = lat1.toRadians()
    val phi2 = lat2.toRadians()
    val deltaPhi = (lat2 - lat1).toRadians()
    val deltaLambda = (lon2 - lon1).toRadians()

    val a = sin(deltaPhi / 2).pow(2) + cos(phi1) * cos(phi2) * sin(deltaLambda / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return r * c
}

fun Double.toRadians(): Double {
    return this * PI / 180
}


fun getOptimalHike(
    coordinates: List<LatLng>,
    maxRange: Double,
    startingPosition: LatLng
): List<LatLng> {
    val remainingCoordinates = coordinates.toMutableList()

    // Sort the coordinates by distance from starting position
    remainingCoordinates.sortBy { it.distanceTo(startingPosition).toDouble() }

    val hikeCoordinates = mutableListOf(startingPosition)
    var currentLocation = startingPosition

    while (remainingCoordinates.isNotEmpty()) {
        // Find the closest remaining coordinate to the current location
        val nextCoordinate = remainingCoordinates.minByOrNull {
            it.distanceTo(currentLocation).toDouble()
        } ?: break // No more nearby coordinates

        // Check if the next coordinate is within the maximum range
        val distanceToNext = currentLocation.distanceTo(nextCoordinate).toDouble()
        if (distanceToNext > maxRange) {
            // Next coordinate is too far away, remove it from the list of remaining coordinates
            remainingCoordinates.remove(nextCoordinate)
            continue // Skip to the next coordinate
        }

        // Add the next coordinate to the hike
        hikeCoordinates.add(nextCoordinate)

        // Remove the next coordinate from the list of remaining coordinates
        remainingCoordinates.remove(nextCoordinate)

        // Update the current location
        currentLocation = nextCoordinate
    }

    return hikeCoordinates
}


private const val EARTH_RADIUS = 6371 // Radius of the earth in kilometers

fun LatLng.distanceTo(other: LatLng): Double {
    val lat1 = Math.toRadians(latitude)
    val lat2 = Math.toRadians(other.latitude)
    val lng1 = Math.toRadians(longitude)
    val lng2 = Math.toRadians(other.longitude)

    val dLat = lat2 - lat1
    val dLng = lng2 - lng1

    val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLng / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return EARTH_RADIUS * c
}
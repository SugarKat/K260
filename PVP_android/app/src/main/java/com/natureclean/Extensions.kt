package com.natureclean

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.natureclean.viewmodels.sizeBinValues
import com.natureclean.viewmodels.sizePollutionValues
import com.natureclean.viewmodels.typeBinValues
import com.natureclean.viewmodels.typePollutionValues
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


fun calculateDistance(myDistance: LatLng, containerDistance: LatLng?): Double? {
    var distanceTo: Double? = null
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
        distanceTo = (R * c) // convert to km
        //distanceTo = String.format("%.2f", distance) // format to 2 decimal places

    }
    return distanceTo
}


fun calculateDistance(point1: LatLng, point2: LatLng): Double {
    if(point1.latitude == point2.latitude && point1.longitude == point2.longitude){
        return 0.0
    }
    val lat1 = point1.latitude
    val lat2 = point2.latitude
    val lon1 = point1.longitude
    val lon2 = point2.longitude
    val r = 6371.0 // Earth radius in kilometers
    val phi1 = Math.toRadians(lat1)
    val phi2 = Math.toRadians(lat2)
    val deltaPhi = Math.toRadians(lat2 - lat1)
    val deltaLambda = Math.toRadians(lon2 - lon1)

    val a = sin(deltaPhi / 2).pow(2) + cos(phi1) * cos(phi2) * sin(deltaLambda / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return r * c
}

fun getOptimalHike(
    coordinates: List<LatLng>,
    maxRange: Double,
    startingPosition: LatLng
): List<LatLng> {
    val remainingCoordinates = coordinates.toMutableList()

    // Sort the coordinates by distance from starting position
    remainingCoordinates.sortBy { it.distanceTo(startingPosition)}

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

fun List<LatLng>.formatGoogleWaypoints(): String {
    if (this.isEmpty()) {
        return ""
    }

    val sb = StringBuilder()

    for (waypoint in this) {
        sb.append("via:${waypoint.latitude},${waypoint.longitude}|")
    }

    sb.deleteCharAt(sb.length - 1) // Remove the last '|' character

    return sb.toString()
}

fun Int.toWasteType(): String {
    return typePollutionValues.getOrDefault(this, "Nenurodyta")
}
fun Int.toWasteSize(): String {
    return sizePollutionValues.getOrDefault(this, "Nenurodyta")
}
fun Int.toBinType(): String{
    return typeBinValues.getOrDefault(this, "Nenurodyta")
}
fun Int.toBinSize(): String{
    return sizeBinValues.getOrDefault(this, "Nenurodyta")
}

fun getDistance(latlng1: LatLng, latlng2: LatLng): Double {
    val R = 6371 // Earth's radius in km
    val dLat = (latlng2.latitude - latlng1.latitude) * PI / 180
    val dLng = (latlng2.longitude - latlng1.longitude) * PI / 180
    val a = sin(dLat/2) * sin(dLat/2) + cos(latlng1.latitude * PI / 180) * cos(latlng2.latitude * PI / 180) * sin(dLng/2) * sin(dLng/2)
    val c = 2 * atan2(sqrt(a), sqrt(1-a))
    return R * c
}

fun NavController.navigateAndClearStack(route: String){
    this.navigate(route){
        popUpTo(this@navigateAndClearStack.graph.id)
    }

}
package com.natureclean

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
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
    if(containerDistance != null) {

        val radius = 6371.0 // Earth's radius in kilometers

        // Convert decimal degrees to radians
        val R = 6371 // Radius of the earth in km
        val latDistance = Math.toRadians(containerDistance.latitude - myDistance.latitude)
        val lonDistance = Math.toRadians(containerDistance.longitude - myDistance.longitude)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(myDistance.latitude)) * Math.cos(Math.toRadians(containerDistance.latitude)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = (R * c).toInt() // convert to km
        distanceTo = distance.toString()

    }
    return distanceTo
}




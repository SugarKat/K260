package com.natureclean.api.google

import android.util.Log
import com.natureclean.api.BackendInterface
import com.natureclean.api.model.Resource
import com.natureclean.api.model.User
import com.natureclean.api.model.UserCredentials
import com.natureclean.google.domain.model.GeocodedWaypoints
import com.natureclean.google.domain.model.GooglePlacesInfo
import com.natureclean.google.presentation.MAP_KEY
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class GoogleBackend @Inject constructor(
    private val api: GoogleBackendInterface
) {

    suspend fun getDirections(
        destination: String,
        origin: String,
        waypoints: String?,
        mode: String,
    ): Resource<GooglePlacesInfo> {
        val response = try {
            api.getDirection(
                destination = destination,
                origin = origin,
                waypoints = waypoints,
                mode = mode,
                key = MAP_KEY
            )
        } catch (e: Exception) {
            Log.i("error", e.toString())
            return Resource.Error(e.message ?: "Error")
        }
        return Resource.Success(response)
    }
}
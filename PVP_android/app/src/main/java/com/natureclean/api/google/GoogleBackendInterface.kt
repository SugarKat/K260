package com.natureclean.api.google

import com.natureclean.google.domain.model.GooglePlacesInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBackendInterface {

    @GET("/maps/api/directions/json")
    suspend fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("waypoints") waypoints: String?, // Optional parameter for intermediate points
        @Query("mode") mode: String, // Optional parameter for mode of transportation
        @Query("key") key: String
    ): GooglePlacesInfo

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/"
    }
}
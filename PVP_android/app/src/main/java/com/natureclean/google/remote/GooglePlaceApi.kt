package com.natureclean.google.remote

import com.natureclean.google.domain.model.GooglePlacesInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {

    @GET("/maps/api/direction/json")
    suspend fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): GooglePlacesInfo

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/"
    }
}
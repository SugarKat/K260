package com.natureclean.api

import com.natureclean.api.model.PollutionPoint
import com.natureclean.api.model.User
import com.natureclean.api.model.UserCredentials
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BackendInterface {

    @POST("api/register")
    suspend fun registerUser(
        @Body userRegObj: UserCredentials
    ) : User

    @POST("api/login")
    suspend fun loginUser(
        @Body userCred: UserCredentials
    ) : User


    @GET("api/pollution-point")
    suspend fun getPoints(): List<PollutionPoint>
    @POST("api/pollution-point")
    suspend fun registerPoint(
        @Body pollutionPoint: PollutionPoint
    ) : PollutionPoint



}
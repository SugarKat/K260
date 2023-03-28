package com.natureclean.api

import com.natureclean.api.model.*
import retrofit2.http.*

interface BackendInterface {

    @POST("api/register")
    suspend fun registerUser(
        @Body userRegObj: UserCredentials
    ): User

    @POST("api/login")
    suspend fun loginUser(
        @Body userCred: UserCredentials
    ): User

    @PUT("api/user/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body user: UserObj
    ): UserObj


    @GET("api/pollution-point")
    suspend fun getPoints(): List<PollutionPoint>

    @POST("api/pollution-point")
    suspend fun registerPoint(
        @Body pollutionPoint: PollutionPoint
    ): PollutionPoint

    @PUT("api/pollution-point/{id}")
    suspend fun updatePoint(
        @Path("id") id: String,
        @Body pollutionPoint: PollutionPoint
    ): PollutionPoint

    @POST("api/garbage-disposal-point")
    suspend fun addContainer(
        @Body container: Container
    ): Container

    @GET("api/garbage-disposal-point")
    suspend fun getContainers(): List<Container>


}
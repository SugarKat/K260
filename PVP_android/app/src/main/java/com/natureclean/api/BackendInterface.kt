package com.natureclean.api

import com.natureclean.api.model.User
import com.natureclean.api.model.UserCredentials
import retrofit2.http.Body
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




}
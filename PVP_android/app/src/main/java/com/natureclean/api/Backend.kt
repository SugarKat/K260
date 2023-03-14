package com.natureclean.api

import android.util.Log
import com.natureclean.api.model.Resource
import com.natureclean.api.model.User
import com.natureclean.api.model.UserCredentials
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Inject

@ActivityScoped
class Backend @Inject constructor(
    private val api: BackendInterface
) {

    suspend fun registerUser(user: UserCredentials): Resource<User> {
        val response = try {
            api.registerUser(user)
        } catch (e: Exception) {
            Log.i("error", e.toString())
            return Resource.Error(e.message ?: "Error")
        }
        return Resource.Success(response)
    }

    suspend fun loginUser(user: UserCredentials): Resource<User> {
        val response = try {
            api.loginUser(user)
        } catch (e: Exception) {
            Log.i("error", e.toString())
            return Resource.Error(e.message ?: "Error")
        }
        return Resource.Success(response)
    }




}
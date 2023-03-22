package com.natureclean.api

import android.util.Log
import com.natureclean.api.model.PollutionPoint
import com.natureclean.api.model.Resource
import com.natureclean.api.model.User
import com.natureclean.api.model.UserCredentials
import dagger.hilt.android.scopes.ActivityScoped
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

    suspend fun getPoints(): Resource<List<PollutionPoint>>{
        val response = try {
            api.getPoints()
        } catch (e: Exception) {
            Log.i("error", e.toString())
            return Resource.Error(e.message ?: "Error")
        }
        return Resource.Success(response)
    }
    suspend fun registerPoint(point: PollutionPoint): Resource<PollutionPoint> {
        val response = try {
            api.registerPoint(point)
        } catch (e: Exception) {
            Log.i("error", e.toString())
            return Resource.Error(e.message ?: "Error")
        }
        return Resource.Success(response)
    }




}
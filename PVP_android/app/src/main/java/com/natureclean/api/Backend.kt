package com.natureclean.api

import android.util.Log
import com.natureclean.api.model.*
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

    suspend fun updateUser(user: UserObj): Resource<UserObj> {
        val response = try {
            api.updateUser(id = user.id, user = user)
        } catch (e: Exception) {
            Log.i("error", e.toString())
            return Resource.Error(e.message ?: "Error")
        }
        return Resource.Success(response)
    }
    suspend fun updatePoint(point: PollutionPoint): Resource<PollutionPoint>{
        val response = try {
            api.updatePoint(id = point.id!!, pollutionPoint = point)
        } catch (e: Exception) {
            Log.i("error", e.toString())
            return Resource.Error(e.message ?: "Error")
        }
        return Resource.Success(response)
    }

    suspend fun addContainer(container: Container): Resource<Container>{
        val response = try {
            api.addContainer(container)
        } catch (e: Exception) {
            Log.i("error", e.toString())
            return Resource.Error(e.message ?: "Error")
        }
        return Resource.Success(response)
    }

    suspend fun getContainers(): Resource<List<Container>>{
        val response = try {
            api.getContainers()
        } catch (e: Exception) {
            Log.i("error", e.toString())
            return Resource.Error(e.message ?: "Error")
        }
        return Resource.Success(response)
    }

    suspend fun getUsers(): Resource<List<UserObj>>{
        val response = try {
            api.users()
        } catch (e: Exception) {
            Log.i("error", e.toString())
            return Resource.Error(e.message ?: "Error")
        }
        return Resource.Success(response)
    }

}
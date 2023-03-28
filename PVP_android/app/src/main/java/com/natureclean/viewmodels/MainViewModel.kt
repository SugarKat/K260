package com.natureclean.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.natureclean.api.Backend
import com.natureclean.api.model.*
import com.natureclean.navigation.Tabs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: Backend,
) : ViewModel() {

    var errorMessage = mutableStateOf("")
    var userLocation = mutableStateOf<LatLng?>(null)

    var pollutionPoints = mutableStateOf<List<PollutionPoint>>(listOf())
    var currentPollutionPoint = mutableStateOf<PollutionPoint?>(null)

    var user = mutableStateOf<User?>(null)
    var containers = mutableStateOf<List<Container>>(listOf())

    var showDialog = mutableStateOf(false)
    fun topBarAction(route: String?){
        when(route){
            Tabs.Map.route ->{
                showDialogStatus(true)
            }
            Tabs.Containers.route ->{
                showDialogStatus(true)

            }
            else ->{

            }
        }
    }

    fun setPollutionPoint(point: PollutionPoint){
        currentPollutionPoint.value = point
    }
    fun updateLocation(newLoc: LatLng) {
        userLocation.value = newLoc
    }

    fun showDialogStatus(status: Boolean) {
        showDialog.value = status
    }

    fun registerUser(userCreds: UserCredentials, callback: () -> Unit = {}) {
        viewModelScope.launch {
            when (val response =
                api.registerUser(
                    UserCredentials(
                        name = userCreds.name,
                        email = userCreds.email,
                        password = userCreds.password,
                        password_confirmation = userCreds.password_confirmation
                    )
                )) {
                is Resource.Success -> {
                    callback()
                }
                is Resource.Error -> {
                    errorMessage.value = response.error?.message ?: "Could not login"
                }
                else -> {}
            }
        }
    }

    fun loginUser(userCreds: UserCredentials, callback: () -> Unit) {
        viewModelScope.launch {
            when (val response =
                api.loginUser(
                    UserCredentials(
                        name = userCreds.name,
                        email = userCreds.email,
                        password = userCreds.password,
                        password_confirmation = userCreds.password_confirmation
                    )
                )) {
                is Resource.Success -> {
                    user.value = response.data
                    callback()
                    Log.i("SUCCESS", response.toString())
                }
                is Resource.Error -> {
                    errorMessage.value = response.error?.message ?: "Could not login"
                }
                else -> {}
            }
        }
    }

    fun getPoints() {
        viewModelScope.launch {
            when (val response =
                api.getPoints()) {
                is Resource.Success -> {
                    pollutionPoints.value = response.data!!
                }
                is Resource.Error -> {
                    errorMessage.value = response.error?.message ?: "Could not fetch"
                }
                else -> {}
            }
        }
    }

    fun registerPoint(name: String, description: String, type: Int, callback: () -> Unit) {
        Log.e("IN FUNC", "IN FUNC")
        viewModelScope.launch {
            when (val response =
                api.registerPoint(
                    point = PollutionPoint(
                        name = name,
                        description = description,
                        longitude = userLocation.value?.longitude,
                        latitude = userLocation.value?.latitude,
                        rating = 1,
                        type = type
                    )
                )) {
                is Resource.Success -> {
                    Log.i("SUCCESS", "SUCES")
                    callback()
                }
                is Resource.Error -> {
                    Log.e("ERR", "ERR")

                    errorMessage.value = response.error?.message ?: "Could not fetch"
                }
                else -> {}
            }
        }
    }
    fun cleanPoint(point: PollutionPoint, callback: () -> Unit){
        viewModelScope.launch {
            when (val response =
                api.updatePoint(
                    point = point.copy(isActive = 0)
                )) {
                is Resource.Success -> {
                    Log.e("SUCCES", "SUCCESS")
                    user.value?.let{
                        api.updateUser(user = user.value!!.user, points = point.rating)
                    }
                    callback()
                }
                is Resource.Error -> {
                    errorMessage.value = response.error?.message ?: "Could not fetch"
                }
                else -> {}
            }
        }
    }

    fun addContainer(container: Container, callback: () -> Unit){
        viewModelScope.launch {
            when (val response =
                api.addContainer(
                    container = container
                )) {
                is Resource.Success -> {
                    callback()
                }
                is Resource.Error -> {
                    errorMessage.value = response.error?.message ?: "Could not fetch"
                }
                else -> {}
            }
        }
    }
    fun getContainers(){
        viewModelScope.launch {
            when (val response =
                api.getContainers(
                )) {
                is Resource.Success -> {
                    containers.value = response.data!!
                }
                is Resource.Error -> {
                    errorMessage.value = response.error?.message ?: "Could not fetch"
                }
                else -> {}
            }
        }
    }



}

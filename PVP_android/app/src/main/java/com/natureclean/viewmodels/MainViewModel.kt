package com.natureclean.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.natureclean.api.Backend
import com.natureclean.api.model.PollutionPoint
import com.natureclean.api.model.Resource
import com.natureclean.api.model.UserCredentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: Backend,
) : ViewModel() {

    var errorMessage = mutableStateOf("")
    var showDialog = mutableStateOf(false)

    var userLocation = mutableStateOf<LatLng?>(null)
    var pollutionPoints = mutableStateOf<List<PollutionPoint>>(listOf())


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

    fun updatePoints(newData: List<PollutionPoint>){
        pollutionPoints.value = newData
    }
    fun getPoints(callback: (List<PollutionPoint>?) -> Unit) {
        viewModelScope.launch {
            when (val response =
                api.getPoints()) {
                is Resource.Success -> {
                    updatePoints(response.data!!)
                    //callback(response.data)
                }
                is Resource.Error -> {
                    errorMessage.value = response.error?.message ?: "Could not fetch"
                }
                else -> {}
            }
        }
    }

    fun registerPoint(name: String, description: String, type: Int, callback: () -> Unit) {
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
                    callback()
                }
                is Resource.Error -> {
                    errorMessage.value = response.error?.message ?: "Could not fetch"
                }
                else -> {}
            }
        }
    }


}

package com.natureclean.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natureclean.api.Backend
import com.natureclean.api.model.Resource
import com.natureclean.api.model.User
import com.natureclean.api.model.UserCredentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: Backend,
) : ViewModel() {

    var errorMessage = mutableStateOf("")


    fun registerUser(userCreds: UserCredentials) {
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
                    Log.i("SUCCESS", response.toString())
                }
                is Resource.Error -> {
                    Log.i("FAIL", response.error?.toString()?: "FAIL")

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
                    errorMessage.value = response.error?.message?: "Could not login"
                }
                else -> {}
            }
        }
    }



}

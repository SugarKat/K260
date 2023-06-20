package com.natureclean.viewmodels

import android.util.Log
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codelab.android.datastore.User
import com.codelab.android.datastore.UserObj
import com.google.android.gms.maps.model.LatLng
import com.natureclean.api.Backend
import com.natureclean.api.model.*
import com.natureclean.navigation.Tabs
import com.natureclean.navigation.screens.HikeResults
import com.natureclean.navigation.screens.HikeResultsState
import com.natureclean.proto.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


val typeBinValues = mutableMapOf(
    1 to "Plastic",
    2 to "Paper",
    3 to "Glass",
    4 to "Mixed"
)
val sizeBinValues = mutableMapOf(
    1 to "Small bin",
    2 to "Average bin",
    3 to "Large bin"
)

val typePollutionValues = mutableMapOf(
    1 to "Plastic",
    2 to "Paper",
    3 to "Glass",
    4 to "Large-scale",
    5 to "Mixed"
)
val sizePollutionValues = mutableMapOf(
    1 to "Small pollution",
    2 to "Average pollution",
    3 to "Extreme pollution",
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStoreRepository,
    private val api: Backend,
) : ViewModel() {



    var hikeResults = mutableStateOf(HikeResultsState())

    var distanceTravelled = mutableStateOf(0)

    var adminData = mutableStateOf<AdminData?>(null)

    var errorMessage = mutableStateOf("")
    var userLocation = mutableStateOf<LatLng?>(null)

    var pollutionPoints = mutableStateOf<List<PollutionPoint>>(listOf())
    var currentPollutionPoint = mutableStateOf<PollutionPoint?>(null)

    var user = mutableStateOf<User?>(null)
    var containers = mutableStateOf<List<Container>>(listOf())
    var users = mutableStateOf<List<UserObj>>(listOf())

    var showPollutionAdd = mutableStateOf(false)
    var showBinAdd = mutableStateOf(false)


    var maxRange = mutableStateOf(Int.MAX_VALUE)
    var pathPoints = mutableStateOf<List<PollutionPoint>>(listOf())

    @OptIn(ExperimentalMaterialApi::class)
    var globalSheetState: BottomSheetScaffoldState? = null

    val userStorage = dataStore.userFlow

    init {
        viewModelScope.launch {
            user.value = dataStore.userFlow.first()
        }
    }


    fun setHikeResults(newHikeResults: HikeResultsState, callback: () -> Unit){
        hikeResults.value = newHikeResults
        callback()
    }
    @OptIn(ExperimentalMaterialApi::class)
    fun setSheetState(sheetState: BottomSheetScaffoldState) {
        globalSheetState = sheetState
    }

    fun resetError() {
        errorMessage.value = ""
    }

    fun setMaxRange(value: Int) {
        maxRange.value = value
    }

    fun autoHikeMode() {
        pathPoints.value = pollutionPoints.value
    }


    fun resetPath(){
        pathPoints.value = emptyList()
    }
    fun setPathCoordinates(coordinates: List<PollutionPoint>) {
        pathPoints.value = emptyList()
        pathPoints.value = coordinates
    }


    fun showPointAdd() {
        showBinAdd.value = false
        showPollutionAdd.value = true
    }

    fun showBinAdd() {
        showPollutionAdd.value = false
        showBinAdd.value = true
    }

    fun setPollutionPoint(point: PollutionPoint) {
        currentPollutionPoint.value = point
    }

    fun updateLocation(newLoc: LatLng) {
        userLocation.value = newLoc
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
                    response.data?.let {
                        dataStore.updateUser(it)
                        callback()
                        Log.i("SUCCESS", response.toString())
                    }
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

    fun updateUserDistance(distance: Double) {
        viewModelScope.launch {
        val updatedUser = user.value?.user?.id?.let { api.getUser(it) }
            updatedUser.let {
                when (val response =
                    it.let { it1 ->
                        val distanceTravelled = it1?.data?.distance_travelled?.plus(distance)
                        it1?.data?.let { it2 ->
                            distanceTravelled?.let { it3 -> it2.copy(distance_travelled = it3) }
                                ?.let { it4 ->
                                    api.updateUser(
                                        it4
                                    )
                                }
                        }
                    }
                ) {
                    is Resource.Success -> {
                        Log.i("SUCCESS", "SUCES")

                    }

                    is Resource.Error -> {
                        Log.e("ERR", "ERR")
                        errorMessage.value = response.error?.message ?: "Could not fetch"
                    }

                    else -> {}
                }
            }
        }
    }

    fun registerPoint(
        name: String,
        description: String,
        type: Int,
        size: Int,
        callback: () -> Unit
    ) {
        Log.e("IN FUNC", "IN FUNC")
        viewModelScope.launch {
            when (val response =
                api.registerPoint(
                    point = PollutionPoint(
                        name = name,
                        description = description,
                        longitude = userLocation.value?.longitude,
                        latitude = userLocation.value?.latitude,
                        rating = size,
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

    fun cleanPoint(point: PollutionPoint, callback: () -> Unit) {
        viewModelScope.launch {
            when (val response =
                api.updatePoint(
                    point = point.copy(isActive = 0)
                )) {
                is Resource.Success -> {
                    val updatedUser = user.value?.user?.id?.let { api.getUser(it) }
                    updatedUser.let{ updUsr->
                        updUsr.let { it1 ->
                            it1?.data?.copy(points = updUsr?.data?.points?.plus(
                                point.rating
                            ) ?: point.rating)?.let { api.updateUser(user = it) }
                        }
                    }


//                    user.value?.let {
//                        val points = it.user?.points
//                        user.value!!.user?.let { it1 -> api.updateUser(user = it1.copy(points = points?.plus(
//                            point.rating
//                        ) ?: point.rating)) }
//                    }
                    callback()
                }

                is Resource.Error -> {
                    errorMessage.value = response.error?.message ?: "Could not fetch"
                }

                else -> {}
            }
        }
    }

    fun addContainer(container: Container, callback: () -> Unit) {
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

    fun getContainers() {
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

    fun getUsers() {
        viewModelScope.launch {
            when (val response =
                api.getUsers()) {
                is Resource.Success -> {
                    response.data?.let {
                        users.value = it.sortedBy { user ->
                            user.points
                        }
                    }
                }

                is Resource.Error -> {
                    errorMessage.value = response.error?.message ?: "Could not fetch"
                }

                else -> {}
            }
        }
    }

    fun logOffUser(navigate: () -> Unit) {
        viewModelScope.launch {
            dataStore.updateUser(null)
            navigate()
        }
    }

    fun getAdminData() {
        viewModelScope.launch {
            when (val response =
                api.getAdminData()) {
                is Resource.Success -> {
                    response.data?.let {
                        adminData.value = it
                    }
                }

                is Resource.Error -> {
                    errorMessage.value = response.error?.message ?: "Could not fetch"
                }

                else -> {}
            }
        }
    }

}

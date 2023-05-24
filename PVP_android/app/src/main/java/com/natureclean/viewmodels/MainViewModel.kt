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
import com.natureclean.proto.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


val typeBinValues = mutableMapOf(
    1 to "Plastikui",
    2 to "Popieriui",
    3 to "Stiklui",
    4 to "Mišriom atliekom"
)
val sizeBinValues = mutableMapOf(
    1 to "Maža šiukšliadėžė",
    2 to "Vidutinė šiukšliadėžė",
    3 to "Didelė šiukšliadėžė"
)

val typePollutionValues = mutableMapOf(
    1 to "Plastikas",
    2 to "Popierius",
    3 to "Stiklas",
    4 to "Stambiagabaritės",
    5 to "Mišrios"
)
val sizePollutionValues = mutableMapOf(
    1 to "Mažas užterštumas",
    2 to "Didelis užterštumas",
    3 to "Labai didelis užterštumas",
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStoreRepository,
    private val api: Backend,
) : ViewModel() {


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

    fun setPathCoordinates(coordinates: List<PollutionPoint>) {

        pathPoints.value = coordinates
    }

    fun topBarAction(route: String?) {
        when (route) {
//            Tabs.Map.route ->{
//                showBinAdd.value = false
//                showPollutionAdd.value = true
//            }
////            Tabs.Containers.route ->{
////                showPollutionAdd.value = false
////                showBinAdd.value = true
////            }
//            else ->{
//
//            }
        }
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

    fun updateUserDistance(distance: Int) {
        user.value?.let {
            viewModelScope.launch {
                when (val response =
                    it.user?.let { it1 ->
                        val distanceTravelled = it1.distance_travelled + distance
                        api.updateUser(
                            it1.copy(distance_travelled = distanceTravelled)
                        )
                    }) {
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
                    Log.e("SUCCES", "SUCCESS")
                    user.value?.let {
                        user.value!!.user?.let { it1 -> api.updateUser(user = it1.copy(points = point.rating)) }
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

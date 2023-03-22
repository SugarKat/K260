package com.natureclean.navigation.tabs


import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.natureclean.api.model.PollutionPoint
import com.natureclean.checkMapPermissions
import com.natureclean.ui.components.PollutionAdd
import com.natureclean.viewmodels.MainViewModel


@SuppressLint("MissingPermission")
@Composable
fun Map(mainViewModel: MainViewModel) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    var myLocationEnabled by remember { mutableStateOf(false) } //permissions granted

    val startingCoordinates = LatLng(55.2484, 24.1205)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startingCoordinates, 7.5f)
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = true)
        )
    }


    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
                Log.e("location", location.latitude.toString())
                mainViewModel.updateLocation(LatLng(location.latitude, location.longitude))
            }
        }


    val pollutionPoints = remember { mainViewModel.pollutionPoints }
    val showDialog by remember { mainViewModel.showDialog }

    val requestMultiplePermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.isNotEmpty()) {
                myLocationEnabled = permissions.all {
                    it.value
                }
            }
        }

    LaunchedEffect(Unit) {
        mainViewModel.getPoints { }
        checkMapPermissions(context).apply {
            if (this.isNotEmpty()) {
                requestMultiplePermissions.launch(checkMapPermissions(context).toTypedArray())
            } else {
                myLocationEnabled = true
            }
        }
    }
    if (showDialog) {
        PollutionAdd(
            closeDialog = { mainViewModel.showDialogStatus(false) },
            function = { name, description, type ->
                mainViewModel.registerPoint(
                    name = name,
                    description = description,
                    type = type.toInt()
                ) {
                    mainViewModel.getPoints { }
                }
            })
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = myLocationEnabled),
        uiSettings = uiSettings,
    ) {
        pollutionPoints.value.forEach {
            if (it.latitude != null && it.longitude != null) {
                Marker(
                    state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                    onInfoWindowClick = {

                    }
                )
            }
        }
    }
}
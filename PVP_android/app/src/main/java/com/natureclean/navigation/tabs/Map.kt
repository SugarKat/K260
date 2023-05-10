package com.natureclean.navigation.tabs


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.natureclean.R
import com.natureclean.api.model.PollutionPoint
import com.natureclean.checkMapPermissions
import com.natureclean.ui.components.PollutionAdd
import com.natureclean.ui.components.PollutionClean
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
    val currentPollutionPoint by remember { mainViewModel.currentPollutionPoint }

    val containers = remember { mainViewModel.containers }
    var cleanDialog by remember { mutableStateOf(false) }

    val requestMultiplePermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.isNotEmpty()) {
                myLocationEnabled = permissions.all {
                    it.value
                }
            }
        }

    LaunchedEffect(Unit) {
        mainViewModel.getContainers()
        mainViewModel.getPoints()
        checkMapPermissions(context).apply {
            if (this.isNotEmpty()) {
                requestMultiplePermissions.launch(checkMapPermissions(context).toTypedArray())
            } else {
                myLocationEnabled = true
            }
        }
    }



    if (cleanDialog) {
        PollutionClean(
            point = currentPollutionPoint!!,//never null,
            closeDialog = { cleanDialog = false },
            onClick = {
                mainViewModel.cleanPoint(currentPollutionPoint!!) {
                    mainViewModel.getPoints()
                    cleanDialog = false
                }
            }
        )
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = myLocationEnabled),
        uiSettings = uiSettings,
    ) {

        pollutionPoints.value.forEach { point ->
            if (point.latitude != null && point.longitude != null && point.isActive == 1) {
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            point.latitude,
                            point.longitude
                        )
                    ),
                    icon = bitmapDescriptorFromVector(context, R.drawable.litter_map),
                    onClick = {
                        mainViewModel.setPollutionPoint(point)
                        cleanDialog = true
                        true
                    },
                    anchor = Offset(0.5F, 0.5F)
                )
            }
        }
        containers.value.forEach { container ->
            if (container.latitude != null && container.longitude != null) {
                MarkerInfoWindow(
                    icon = bitmapDescriptorFromVector(context, R.drawable.container_map),
                    state = MarkerState(
                        position = LatLng(
                            container.latitude,
                            container.longitude
                        )
                    ),
                    anchor = Offset(0.5F, 0.5F),
                    content = {
                        Column(
                            modifier = Modifier.padding(16.dp).background(Color.White),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(container.name)
                            Text(container.size)
                            Text(container.type)
                        }
                    }
                )
            }
        }
    }
}


fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    vectorDrawable!!.setBounds(
        0,
        0,
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight
    )
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

//Marker(
//state = MarkerState(it),
//icon = bitmapDescriptorFromVector(
//context,
//if (index != activeIndex) R.drawable.red_marker else R.drawable.ic_red_active_marker
//),
//anchor = Offset(0.5F, 0.5F)
//)
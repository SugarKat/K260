package com.natureclean.navigation.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.natureclean.R
import com.natureclean.api.model.PollutionPoint
import com.natureclean.checkMapPermissions
import com.natureclean.getOptimalHike
import com.natureclean.navigation.tabs.bitmapDescriptorFromVector
import com.natureclean.ui.components.ContainerAdd
import com.natureclean.ui.components.DARK_GREEN
import com.natureclean.ui.components.MainTopAppBar
import com.natureclean.ui.components.PollutionAdd
import com.natureclean.ui.components.PollutionInfo
import com.natureclean.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HikeMap(mainViewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()

    val pointList = mainViewModel.pathPoints.value
    val coordinates = pointList.map { LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0) }


    val maxHikeRange = mainViewModel.maxRange.value
    val userLocation = mainViewModel.userLocation.value!!
    Log.e("userlocation", userLocation.toString())

//    val coordinates = listOf(
//        LatLng(37.7749, -122.4194),
//        LatLng(37.7748, -122.4193),
//        LatLng(37.7747, -122.4192),
//        LatLng(37.7746, -122.4191),
//        LatLng(37.7745, -122.4190),
//        LatLng(37.7744, -122.4189),
//        LatLng(37.7743, -122.4188),
//        LatLng(37.7742, -122.4187),
//        LatLng(37.7741, -122.4186),
//        LatLng(37.7740, -122.4185),
//        LatLng(37.7739, -122.4184),
//        LatLng(37.7738, -122.4183),
//        LatLng(37.7737, -122.4182),
//        LatLng(37.7736, -122.4181),
//        LatLng(37.7735, -122.4180),
//        LatLng(37.7734, -122.4179),
//        LatLng(37.7733, -122.4178),
//        LatLng(37.7732, -122.4177),
//        LatLng(37.7731, -122.4176),
//        LatLng(37.7730, -122.4175),
//        LatLng(37.7729, -122.4174),
//        LatLng(37.7728, -122.4173),
//        LatLng(37.7727, -122.4172),
//        LatLng(37.7726, -122.4171),
//        LatLng(37.7725, -122.4170),
//        LatLng(37.7724, -122.4169),
//        LatLng(37.7723, -122.4168),
//        LatLng(37.7722, -122.4167),
//        LatLng(37.7721, -122.4166),
//        LatLng(37.7720, -122.4165),
//        LatLng(37.7719, -122.4164))

    val points = remember {
        getOptimalHike(
            coordinates,
            maxHikeRange.toDouble(),
            userLocation
        ).toMutableStateList()
    }

    val latLngToPointMap = coordinates.zip(pointList).toMap()

    LaunchedEffect(key1 = points) {
        Log.e("points:", points.toString())
    }
    var myLocationEnabled by remember { mutableStateOf(false) } //permissions granted

    val startingCoordinates = points[0]

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startingCoordinates, 15F)
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = true,
                compassEnabled = false
            )
        )
    }
    val requestMultiplePermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.isNotEmpty()) {
                myLocationEnabled = permissions.all {
                    it.value
                }
            }
        }

    var chosenLitter by remember {
        mutableStateOf<PollutionPoint?>(
            latLngToPointMap.getValue(
                points[1]// [0] is always user location
            )
        )
    }

    val addState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val sheetState =
        rememberBottomSheetScaffoldState(bottomSheetState = addState)

    fun openSheet() {
        coroutine.launch {
            addState.expand()
        }
    }

    LaunchedEffect(Unit) {
        checkMapPermissions(context).apply {
            if (this.isNotEmpty()) {
                requestMultiplePermissions.launch(checkMapPermissions(context).toTypedArray())
            } else {
                myLocationEnabled = true
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            chosenLitter?.let { point ->
                PollutionInfo(
                    point = point,
                    myLocation = userLocation,
                    remove = {
                        Log.e("size before", points.size.toString())
                        var chosenLitterIndex = points.indexOf(point.latitude?.let {
                            point.longitude?.let { it1 ->
                                LatLng(
                                    it,
                                    it1
                                )
                            }
                        })
                        points.removeAt(chosenLitterIndex)

                        val nextPoint = if (chosenLitterIndex < points.size) {
                            points[chosenLitterIndex]
                        } else if (points.isNotEmpty()) {
                            points.last()
                        } else {
                            null
                        }
                        chosenLitter = latLngToPointMap[nextPoint]
                        coroutine.launch {
                            animateCamera(
                                cameraPositionState,
                                nextPoint ?: LatLng(0.0, 0.0)
                            )
                        }
                    },
                    clean = {
                        mainViewModel.cleanPoint(point) {}
                    }
                )
            }
        },
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {

        if (points.isNotEmpty()) {
            if (points.size > 1) {
                Column {
                    MainTopAppBar()
                    GoogleMap(
                        modifier = Modifier.weight(1f),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(isMyLocationEnabled = myLocationEnabled),
                        uiSettings = uiSettings,
                    ) {
                        Polyline(points = points.toList(), color = DARK_GREEN)
                        points.drop(1).forEach { point ->
                            val pollutionPoint = latLngToPointMap[point]
                            Marker(
                                state = MarkerState(
                                    position = LatLng(
                                        point.latitude,
                                        point.longitude
                                    )
                                ),
                                icon = bitmapDescriptorFromVector(context, R.drawable.litter),
                                onClick = {
                                    coroutine.launch {
                                        animateCamera(
                                            cameraPositionState,
                                            LatLng(point.latitude, point.longitude)
                                        )
                                    }
                                    chosenLitter = pollutionPoint
                                    openSheet()
                                    true
                                },
                                anchor = Offset(0.5F, 0.5F)
                            )
                        }

                    }
                }
            } else {
                Text("NO HIKE AVAILABLE")
            }
        } else {
            CircularProgressIndicator()
        }
    }
}

suspend fun animateCamera(
    cameraPosition: CameraPositionState,
    coordinates: LatLng,
    zoom: Float = 15F
) {
    cameraPosition.animate(
        CameraUpdateFactory.newLatLngZoom(
            coordinates,
            zoom
        )
    )
}
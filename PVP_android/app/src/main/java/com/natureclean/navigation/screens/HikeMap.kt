package com.natureclean.navigation.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PatternItem
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.natureclean.R
import com.natureclean.api.model.PollutionPoint
import com.natureclean.checkMapPermissions
import com.natureclean.getOptimalHike
import com.natureclean.google.presentation.GooglePlacesInfoViewModel
import com.natureclean.navigation.tabs.bitmapDescriptorFromVector
import com.natureclean.ui.components.MainTopAppBar
import com.natureclean.ui.components.PollutionInfo
import com.natureclean.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import java.util.Arrays


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HikeMap(mainViewModel: MainViewModel, navController: NavController) {

    val glaces: GooglePlacesInfoViewModel = hiltViewModel()

    var routeMode by remember { mutableStateOf("walking") }
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()

    val pointList = mainViewModel.pathPoints.value
    val coordinates = pointList.map { LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0) }

    val maxHikeRange = mainViewModel.maxRange.value
    val userLocation = mainViewModel.userLocation.value!!

    val points = remember {
        getOptimalHike(
            coordinates,
            maxHikeRange.toDouble(),
            userLocation
        ).toMutableStateList()
    }

    val latLngToPointMap = coordinates.zip(pointList).toMap()

    LaunchedEffect(key1 = points) {
        val waypoints = if (points.size > 1) {
            points.subList(1, points.size - 1)
        } else {
            null
        }
        glaces.getDirection(
            origin = "${userLocation.latitude}, ${userLocation.longitude}",
            waypoints = waypoints,
            mode = routeMode,
            destination = "${points.last().latitude}, ${points.last().longitude}"
        )

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
                        val chosenLitterIndex = points.indexOf(point.latitude?.let {
                            point.longitude?.let { it1 ->
                                LatLng(
                                    it,
                                    it1
                                )
                            }
                        })
                        Log.e("POINTS BEFORE RE", points.toString())
                        Log.e("POINTS SIZE B4 RE", points.size.toString())

                        points.removeAt(chosenLitterIndex)
                        Log.e("POINTS AFTER RE", points.toString())
                        Log.e("POINTS SIZE AFTER RE", points.size.toString())
                        val nextPoint = if (chosenLitterIndex < points.size) {
                            points[chosenLitterIndex]
                        } else if (points.isNotEmpty()) {
                            points.last()
                        } else {
                            null
                        }
                        val waypoints = if (points.size > 1) {
                            points.subList(1, points.size - 1)
                        } else {
                            null
                        }
                        chosenLitter = latLngToPointMap[nextPoint]

                        glaces.getDirection(
                            origin = "${userLocation.latitude}, ${userLocation.longitude}",
                            waypoints = waypoints,
                            destination = "${points.last().latitude}, ${points.last().longitude}"
                        )

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
                val dot: PatternItem = Dot()
                val gap: PatternItem = Gap(10f)
                val pattern = listOf(gap, dot)
                Column {
                    MainTopAppBar()
                    Box {
                        GoogleMap(
                            cameraPositionState = cameraPositionState,
                            properties = MapProperties(isMyLocationEnabled = myLocationEnabled),
                            uiSettings = uiSettings,
                        ) {
                            // Drawing on the map is accomplished with a child-based API
                            val markerClick: (Marker) -> Boolean = {
                                false
                            }

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
                            Polyline(
                                points = glaces.polylines.value,
                                onClick = {
                                    Log.i(
                                        "size ",
                                        glaces.polylines.value.size.toString()
                                    )
                                },
                                color = Color.Blue,
                                pattern = pattern,
                                width = 15f

                            )
                        }
                        Icon(
                            imageVector = if (routeMode == "walking") Icons.Filled.DirectionsBike else Icons.Filled.DirectionsRun,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(start = 16.dp, top = 16.dp)
                                .align(Alignment.TopStart)
                                .background(Color.White, shape = CircleShape)
                                .clip(CircleShape)
                                .clickable {
                                    routeMode = if (routeMode == "bicycling") {
                                        "walking"
                                    } else {
                                        "bicycling"
                                    }
                                    val waypoints = if (points.size > 1) {
                                        points.subList(1, points.size - 1)
                                    } else {
                                        null
                                    }
                                    glaces.getDirection(
                                        origin = "${userLocation.latitude}, ${userLocation.longitude}",
                                        waypoints = waypoints,
                                        mode = routeMode,
                                        destination = "${points.last().latitude}, ${points.last().longitude}"
                                    )
                                }
                                .padding(8.dp)
                        )
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

//@SuppressLint("StateFlowValueCalledInComposition")
//@Composable
//fun GoogleMapView(
//    modifier: Modifier,
//    onMapLoaded: () -> Unit,
//    googlePlacesInfoViewModel: GooglePlacesInfoViewModel
//) {
//
//
//
//
//    val dot: PatternItem = Dot()
//    val gap: PatternItem = Gap(10f)
//    val pattern = listOf(gap, dot)
//
//
//    val context = LocalContext.current
//    val singapore = LatLng(1.35, 103.87)
//    val singapore2 = LatLng(1.40, 103.77)
//
//    var pos by remember {
//        mutableStateOf(LatLng(singapore.latitude, singapore.longitude))
//    }
//
//
//    var poi by remember {
//        mutableStateOf("")
//    }
//    val _makerList: MutableList<LatLng> = mutableListOf<LatLng>()
//
//    _makerList.add(singapore)
//    _makerList.add(singapore2)
//
//    var pos2 by remember {
//        mutableStateOf(_makerList)
//    }
//
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(singapore, 11f)
//    }
//
//    var mapProperties by remember {
//        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
//    }
//    var uiSettings by remember {
//        mutableStateOf(
//            MapUiSettings(compassEnabled = false)
//        )
//    }
//
//    GoogleMap(
//        modifier = modifier,
//        cameraPositionState = cameraPositionState,
//        properties = mapProperties,
//        uiSettings = uiSettings,
//        onMapLoaded = onMapLoaded,
//        googleMapOptionsFactory = {
//            GoogleMapOptions().camera(
//                CameraPosition.fromLatLngZoom(
//                    singapore,
//                    11f
//                )
//            )
//        },
//        onMapClick = {
//
//            pos2.add(it)
//            pos = it
//        },
//        onPOIClick = {
//            poi = it.name
//
//            Log.i("asdasd", "asdasd")
//            googlePlacesInfoViewModel.getDirection(
//                origin = "${singapore.latitude}, ${singapore.longitude}",
//                destination = "${it.latLng.latitude}, ${it.latLng.longitude}",
//                waypoints =
//            )
//
//
//        }
//    ) {
//        // Drawing on the map is accomplished with a child-based API
//        val markerClick: (Marker) -> Boolean = {
//
//            false
//        }
//        pos2.forEach { posistion ->
//            Marker(
//                state = MarkerState(posistion),
//                title = "Singapore ",
//                snippet = "Marker in Singapore ${posistion.latitude}, ${posistion.longitude}",
//                onClick = {
//                    Log.i("size ", googlePlacesInfoViewModel.polyLinesPoints.value.size.toString())
//                    true
//                },
//                icon = bitmapDescriptorFromVector(context, R.drawable.litter),
//            )
//        }
//        Polyline(
//            points = googlePlacesInfoViewModel.polyLinesPoints.value,
//            onClick = {
//                Log.i("size ", googlePlacesInfoViewModel.polyLinesPoints.value.size.toString())
//            },
//            color = Color.Blue,
//            pattern = pattern,
//            width = 15f
//
//        )
//    }
//}


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
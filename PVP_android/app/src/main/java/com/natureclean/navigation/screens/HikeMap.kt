package com.natureclean.navigation.screens

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
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
import com.natureclean.calculateDistance
import com.natureclean.checkMapPermissions
import com.natureclean.distanceTo
import com.natureclean.getOptimalHike

import com.natureclean.google.presentation.GooglePlacesInfoViewModel
import com.natureclean.navigateAndClearStack
import com.natureclean.navigation.Screen
import com.natureclean.navigation.Tabs
import com.natureclean.navigation.tabs.bitmapDescriptorFromVector
import com.natureclean.toWasteSize
import com.natureclean.toWasteType
import com.natureclean.ui.components.MainTopAppBar
import com.natureclean.ui.components.PollutionInfo
import com.natureclean.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import java.lang.Math.PI
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt
import java.util.Arrays
import java.util.Timer
import java.util.TimerTask



@SuppressLint("StateFlowValueCalledInComposition", "MissingPermission", "RememberReturnType")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HikeMap(mainViewModel: MainViewModel, navController: NavController) {

    var hikeFinished by remember {mutableStateOf(false)}

    var cleanedPoints by remember { mutableStateOf(0) }
    var pointsEarned by remember { mutableStateOf(0) }
    var distanceTravelled by remember { mutableStateOf(0.0) }

    var finishHike by remember { mutableStateOf(false) }
    val glaces: GooglePlacesInfoViewModel = hiltViewModel()

    var routeMode by remember { mutableStateOf("walking") }
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()

    val pointList = mainViewModel.pathPoints.value.filter { it.isActive != 0 }
    val coordinates = pointList.map { LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0) }

    val maxHikeRange = mainViewModel.maxRange.value
    val userLocation = mainViewModel.userLocation.value!!

    var totalDistance by rememberSaveable { mutableStateOf("") }
    var totalTime by rememberSaveable { mutableStateOf("") }

    var previousTravelled = 0.0

    var finishedHike by remember {mutableStateOf(false)}

    val points = remember {
        getOptimalHike(
            coordinates,
            maxHikeRange.toDouble(),
            userLocation
        ).toMutableStateList()
    }

    var startingCoordinates = points[0]

    val timer = remember { Timer() }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(
            context
        )
    }

    val polyline = glaces.polylines.value
    var (greySegment, blueSegment) = splitPolyline(polyline, userLocation)

    remember {
        timer.schedule(object : TimerTask() {
            override fun run() {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            Log.e("location in hike map", location.latitude.toString())
                            mainViewModel.updateLocation(
                                LatLng(
                                    location.latitude,
                                    location.longitude
                                )
                            )
                            val (newGreySegment, newBlueSegment) = splitPolyline(
                                polyline,
                                userLocation
                            )
                            greySegment = newGreySegment
                            blueSegment = newBlueSegment
                            distanceTravelled += calculateDistance(
                                startingCoordinates,
                                LatLng(location.latitude, location.longitude)
                            )

                            startingCoordinates = LatLng(location.latitude, location.longitude)

                            if(distanceTravelled != previousTravelled) {
                                mainViewModel.updateUserDistance(distanceTravelled)
                            }
                            previousTravelled = distanceTravelled
                        }
                    }
            }
        }, 0, 60 * 100)
    } // 6s


    DisposableEffect(Unit) {
        onDispose {
            mainViewModel.resetPath()
            timer.cancel()
        }
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
            destination = "${points.last().latitude}, ${points.last().longitude}",
        )
    }

    var myLocationEnabled by remember { mutableStateOf(false) } //permissions granted


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
        mutableStateOf(
            if(latLngToPointMap.size > 1 && points.size > 1){
            latLngToPointMap.getValue(
                points[1]// [0] is always user location
            )} else null
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

    if (finishHike) {
        FinishDialog(onAgree = {
            mainViewModel.setHikeResults(
                HikeResultsState(
                    pointsCleared = cleanedPoints,
                    pointsEarned = pointsEarned,
                    distanceTravelled = distanceTravelled
                )
            ) {
                navController.navigateAndClearStack(Screen.HikeResults.route)
            }
        }
        ) {

            finishHike = false

        }
    }
    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            chosenLitter?.let { point ->
                PollutionInfo(
                    point = point,
                    myLocation = userLocation,
                    arrowDown = sheetState.bottomSheetState.currentValue == BottomSheetValue.Expanded,
                    onArrowClick = {
                        if (sheetState.bottomSheetState.currentValue == BottomSheetValue.Expanded) {
                            coroutine.launch {
                                sheetState.bottomSheetState.collapse()

                            }
                        } else {
                            coroutine.launch {
                                sheetState.bottomSheetState.expand()

                            }
                        }
                    },
                    remove = {
                        val chosenLitterIndex = points.indexOf(point.latitude?.let {
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
                        val waypoints = if (points.size > 1) {
                            points.subList(1, points.size - 1)
                        } else {
                            null
                        }
                        chosenLitter = latLngToPointMap[nextPoint]
                        if(points.size == 1){
                            hikeFinished = true
                        }else {
                            glaces.getDirection(
                                origin = "${userLocation.latitude}, ${userLocation.longitude}",
                                waypoints = waypoints,
                                destination = "${points.last().latitude}, ${points.last().longitude}",
                            )
                        }

//                        coroutine.launch {
//                            animateCamera(
//                                cameraPositionState,
//                                nextPoint ?: LatLng(0.0, 0.0)
//                            )
//                        }
                    },
                    clean = {
                        if (point.longitude?.let {
                                point.latitude?.let { it1 ->
                                    LatLng(
                                        it1,
                                        it
                                    )
                                }
                            }?.let { userLocation.distanceTo(it) }!! < 0.2) {
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
                            mainViewModel.cleanPoint(point) {
                                cleanedPoints += 1
                                pointsEarned += point.rating
                            }
                            Toast.makeText(context, "Trash has been cleaned", Toast.LENGTH_LONG)
                                .show()
                            if(points.size == 1){
                                hikeFinished = true
                            }else {
                                glaces.getDirection(
                                    origin = "${userLocation.latitude}, ${userLocation.longitude}",
                                    waypoints = waypoints,
                                    destination = "${points.last().latitude}, ${points.last().longitude}",

                                    )
                            }

                        } else {
                            Toast.makeText(context, "You must nearby the point", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                )
            }
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {

        if (points.isNotEmpty()) {
            if (points.size > 1 && !hikeFinished)  {
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
                                    icon = bitmapDescriptorFromVector(context, R.drawable.litter_map),
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
//                            Polyline(
//                                points = glaces.polylines.value,
//                                onClick = {
//                                    Log.i(
//                                        "size ",
//                                        glaces.polylines.value.size.toString()
//                                    )
//                                },
//                                color = Color.Red.copy(alpha = 0.6F),
//                                pattern = pattern,
//                                width = 15f
//
//                            )

                            Polyline(
                                points = blueSegment,
                                onClick = {
                                    Log.i(
                                        "size ",
                                        glaces.polylines.value.size.toString()
                                    )
                                },
                                color = Color.Red.copy(alpha = 0.6F),
                                pattern = pattern,
                                width = 15f

                            )
                            Polyline(
                                points = greySegment,
                                onClick = {
                                    Log.i(
                                        "size ",
                                        glaces.polylines.value.size.toString()
                                    )
                                },
                                color = Color.DarkGray,
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
                                        context = context,
                                        origin = "${userLocation.latitude}, ${userLocation.longitude}",
                                        waypoints = waypoints,
                                        mode = routeMode,
                                        destination = "${points.last().latitude}, ${points.last().longitude}"
                                    ) {
                                        routeMode = "walking"
                                    }
                                }
                                .padding(8.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.SportsScore,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(start = 16.dp, top = 64.dp)
                                .align(Alignment.TopStart)
                                .background(Color.White, shape = CircleShape)
                                .clip(CircleShape)
                                .clickable {
                                    finishHike = true
                                }
                                .padding(8.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 10.dp, top = 64.dp)
                                .align(Alignment.TopEnd)
                                .background(Color.White, shape = CircleShape)
                                .clip(CircleShape)
                                .clickable {
                                    val waypoints = if (points.size > 1) {
                                        points.subList(1, points.size - 1)
                                    } else {
                                        null
                                    }
                                    glaces.getDirection(
                                        origin = "${userLocation.latitude}, ${userLocation.longitude}",
                                        waypoints = waypoints,
                                        destination = "${points.last().latitude}, ${points.last().longitude}",

                                        )
                                }
                                .padding(8.dp)
                        )

                        Text(
                            text = "Total distance: ${
                                glaces.googlePlacesInfoState.value.direction?.routes?.get(
                                    0
                                )?.legs?.get(0)?.distance?.text

                            }\n" + "Total time: ${
                                glaces.googlePlacesInfoState.value.direction?.routes?.get(
                                    0
                                )?.legs?.get(0)?.duration?.text

                            }",
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .align(Alignment.TopCenter)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            } else {
                    if(hikeFinished) {
                        mainViewModel.setHikeResults(
                            HikeResultsState(
                                pointsCleared = cleanedPoints,
                                pointsEarned = pointsEarned,
                                distanceTravelled = distanceTravelled
                            )
                        ) {}
                        HikeResults(mainViewModel = mainViewModel, navController = navController)
                    }else{
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Currently no trash points active in hike, please generate new hike path",
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { mainViewModel.resetPath(); navController.navigateAndClearStack(Tabs.Map.route) }) {
                                Text("Get back")
                            }

                        }
                    }



            }
        } else {
            CircularProgressIndicator()
        }
    }

}
private const val EARTH_RADIUS = 6371.0 // Earth's radius in kilometers

private fun distanceBetweenPoints(point1: LatLng, point2: LatLng): Double {
    val dLat = Math.toRadians(point2.latitude - point1.latitude)
    val dLon = Math.toRadians(point2.longitude - point1.longitude)
    val lat1 = Math.toRadians(point1.latitude)
    val lat2 = Math.toRadians(point2.latitude)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            sin(dLon / 2) * sin(dLon / 2) * cos(lat1) * cos(lat2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return EARTH_RADIUS * c * 1000 // Convert to meters
}

private fun splitPolyline(
    polyline: List<LatLng>,
    location: LatLng
): Pair<List<LatLng>, List<LatLng>> {
    var closestDistance = Double.MAX_VALUE
    var closestIndex = 0
    for (i in polyline.indices) {
        val distance = distanceBetweenPoints(location, polyline[i])
        if (distance < closestDistance) {
            closestDistance = distance
            closestIndex = i
        }
    }
    val leftSegment = polyline.subList(0, closestIndex)
    val rightSegment = polyline.subList(closestIndex, polyline.size)
    return Pair(leftSegment, rightSegment)
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


@Composable
fun FinishDialog(onAgree: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        title = {
            Text(
                text = "Do you want to finish your hike?",
                color = Color.Black,
                fontWeight = FontWeight(700),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Are you sure you want to finish your hike. You will not be able to come back if you agree.",
                    color = Color.Black,
                    fontWeight = FontWeight(700),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        onDismissRequest = {
            onCancel()
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onCancel()
                    },
                ) { Text("Cancel") }
                Button(
                    onClick = {
                        onAgree()
                    },
                ) { Text("Finish") }
            }
            Spacer(modifier = Modifier.height(24.dp))
        },
        //Shape of the ALERT dialog
        shape = RoundedCornerShape(10.dp),
        //Disabling default alert dialog width
        properties = DialogProperties(usePlatformDefaultWidth = false),
        //Main modifier of the alert dialog box
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .wrapContentHeight()
    )
}

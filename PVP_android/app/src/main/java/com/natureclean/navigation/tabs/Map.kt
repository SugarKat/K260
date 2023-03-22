package com.natureclean.navigation.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Map() {
    val startingCoordinates = LatLng(55.2484, 24.1205)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startingCoordinates, 7.5f)
    }
    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }
    val mapMarkers = remember { mutableStateListOf<LatLng>() }

    var showDialog by remember { mutableStateOf(false) }
    var coordinates by remember { mutableStateOf<LatLng>(LatLng(0.0, 0.0)) }
    if (showDialog) {
        AlertDialog(
            title = {
                Text(
                    text = "Register pollution point?",
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
                        text = "Are you sure you want to register pollution point? False registration will lead to suspension",
                        color = Color.Gray,
                        fontWeight = FontWeight(400),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            onDismissRequest = {
                showDialog = false
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            mapMarkers.add(coordinates)
                            showDialog = false
                        },
                    ) { Text("YES!") }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showDialog = false },
                    ) { Text("No!") }
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
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = uiSettings,
        onMapLongClick = {
            showDialog = true
            coordinates = it
        }
    ) {
        mapMarkers.forEach {
            Marker(state = MarkerState(position = it))
        }
    }
}
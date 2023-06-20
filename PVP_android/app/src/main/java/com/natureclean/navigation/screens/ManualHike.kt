package com.natureclean.navigation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.natureclean.api.model.PollutionPoint
import com.natureclean.calculateDistance
import com.natureclean.navigation.Screen
import com.natureclean.ui.components.DARK_GREEN
import com.natureclean.ui.components.MainTopAppBar
import com.natureclean.viewmodels.MainViewModel

@Composable
fun ManualHike(mainViewModel: MainViewModel, navController: NavController) {

    val myLocation = mainViewModel.userLocation.value
    val trashPoints = mainViewModel.pollutionPoints.value


    val chosenPoints = remember { mutableStateListOf<PollutionPoint>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 4.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
        ) {
            items(trashPoints.sortedBy { point ->
                point.latitude?.let { point.longitude?.let { it1 -> LatLng(it, it1) } }
                    ?.let { it2 ->
                        calculateDistance(
                            myLocation!!,
                            it2
                        )
                    }
            }.filter { point ->
                point.isActive == 1
            }

            ) { point ->
                val distance = calculateDistance(
                    myLocation!!,
                    point.latitude?.let { point.longitude?.let { it1 -> LatLng(it, it1) } })
                var selected by remember { mutableStateOf(false) }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(if (selected) DARK_GREEN else Color.LightGray)
                    .clickable {
                        if (selected) {
                            selected = false
                            chosenPoints.remove(point)
                        } else {
                            selected = true
                            if (!chosenPoints.contains(point)) {
                                chosenPoints.add(point)
                            }
                        }
                    }
                    .padding(vertical = 16.dp, horizontal = 8.dp)) {
                    Text(point.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(" Distance from you ${String.format("%.2f", distance)} km")
                    Spacer(modifier = Modifier.weight(1f))
                    if (selected) {
                        Icon(imageVector = Icons.Filled.Check, "", tint = Color.Blue)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                mainViewModel.resetPath()
                mainViewModel.setPathCoordinates(chosenPoints)
                navController.navigate(Screen.HikeMap.route)
            },
            colors = ButtonDefaults.buttonColors(
                DARK_GREEN,
            ),
            enabled = chosenPoints.isNotEmpty()
        ) {
            Text("Generate hike")
        }
    }
}
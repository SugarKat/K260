package com.natureclean.navigation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.natureclean.navigateAndClearStack
import com.natureclean.navigation.Tabs
import com.natureclean.ui.components.DARK_GREEN
import com.natureclean.viewmodels.MainViewModel

data class HikeResultsState(
    val pointsCleared: Int = 0,
    val pointsEarned: Int = 0,
    val distanceTravelled: Double = 0.0
)
@Composable
fun HikeResults(mainViewModel: MainViewModel, navController: NavController){

    val hikeResults by remember {mainViewModel.hikeResults}

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        Icon(
            imageVector = Icons.Filled.SportsScore,
            contentDescription = "",
            modifier = Modifier
                .padding(start = 16.dp, top = 32.dp)
                .clickable {
                }
                .size(64.dp)
        )
        Spacer(modifier = Modifier.height(64.dp))
        Text("Your results:".uppercase(), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DARK_GREEN)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Trash cleaned ${hikeResults.pointsCleared}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Points earned ${hikeResults.pointsEarned}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Distance travelled ${String.format("%.2f", hikeResults.distanceTravelled)} km")
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { navController.navigateAndClearStack(Tabs.Map.route) }) {
            Text("Next", color = Color.White)
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}
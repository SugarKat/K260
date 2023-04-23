package com.natureclean.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.natureclean.viewmodels.MainViewModel

@Composable
fun Leaderboard(mainViewModel: MainViewModel) {
    val users by remember { mainViewModel.users}

    LaunchedEffect(Unit) {
        mainViewModel.getUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green.copy(0.3F))
    ) {
        if (users.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Leaderbord is not available",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn{
                items(users) { user ->
                    UserItem(user.name, user.points.toString())
                }
            }
        }
    }
}

@Composable
fun UserItem(name: String, points: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue.copy(0.1F))
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Name: $name", modifier = Modifier.padding(vertical = 4.dp))
                Text("Score: $points", modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}
package com.natureclean.navigation.tabs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.natureclean.navigateAndClearStack
import com.natureclean.navigation.Screen
import com.natureclean.viewmodels.MainViewModel

@Composable
fun Account(navController: NavController, mainViewModel: MainViewModel) {

    val user by mainViewModel.userStorage.collectAsState(initial = null)


    LaunchedEffect(Unit) {
        Log.e("UNIT", user.toString())
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        user?.let {
            if(it.roles.isNotEmpty()) {
                if (it.roles[0] == "writer") {
                    Button(onClick = { navController.navigate(Screen.AdminPanel.route) }) {
                        Text(
                            "Admin panel",
                            color = Color.White
                        )
                    }
                }
            }
        }
        Button(onClick = {
            mainViewModel.logOffUser {
                navController.navigateAndClearStack(Screen.Login.route)
            }
        }
        ) {
            Text(
                "Sign out",
                color = Color.White)
        }
    }
}

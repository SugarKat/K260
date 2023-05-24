package com.natureclean.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codelab.android.datastore.User
import com.natureclean.navigation.screens.AdminPanel
import com.natureclean.navigation.tabs.Account
import com.natureclean.navigation.screens.HikeMap
import com.natureclean.navigation.screens.Login
import com.natureclean.navigation.screens.ManualHike
import com.natureclean.navigation.screens.Register
import com.natureclean.navigation.tabs.Hike
import com.natureclean.navigation.tabs.Leaderboard
import com.natureclean.navigation.tabs.Map
import com.natureclean.viewmodels.MainViewModel
import kotlinx.coroutines.flow.first

@Composable
fun Navigation(
    navController: NavController,
    mainViewModel: MainViewModel,
    insetsPadding: PaddingValues
) {
    val context = LocalContext.current

    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val user = mainViewModel.userStorage.first()
        startDestination = findStartDestination(user = user)
    }


    startDestination?.let { initRoute ->
        NavHost(
            navController = navController as NavHostController,
            startDestination = initRoute
        ) {
            composable(Tabs.Account.route) {
                Account(
                    navController = navController,
                    mainViewModel = mainViewModel
                )
            }
            composable(Tabs.Hike.route) { Hike(mainViewModel, navController) }
            //composable(Tabs.Containers.route) { Containers(mainViewModel) }
            composable(Tabs.Leaderboard.route) { Leaderboard(mainViewModel = mainViewModel) }
            composable(Tabs.Map.route) { Map(mainViewModel = mainViewModel) }

            composable(Screen.Login.route) { Login(navController, mainViewModel) }
            composable(Screen.Register.route) {
                Register(
                    navController = navController,
                    mainViewModel = mainViewModel
                )
            }
            composable(Screen.HikeMap.route) {
                HikeMap(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable(Screen.ManualHike.route) {
                ManualHike(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
            composable(Screen.AdminPanel.route) {
                AdminPanel(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }

        }
    }
}

fun findStartDestination(user: User): String {
    return if (user.token.isEmpty()) {
        Screen.Login.route
    } else Tabs.Map.route
}
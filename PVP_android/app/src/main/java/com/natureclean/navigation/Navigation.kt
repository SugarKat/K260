package com.natureclean.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.test_pvp.navigation.tabs.Account
import com.natureclean.navigation.screens.HikeMap
import com.natureclean.navigation.screens.Login
import com.natureclean.navigation.screens.ManualHike
import com.natureclean.navigation.screens.Register
import com.natureclean.navigation.tabs.Hike
import com.natureclean.navigation.tabs.Leaderboard
import com.natureclean.navigation.tabs.Map
import com.natureclean.viewmodels.MainViewModel

@Composable
fun Navigation(
    navController: NavController,
    mainViewModel: MainViewModel,
    insetsPadding: PaddingValues
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Tabs.Map.route
    ) {
        composable(Tabs.Account.route) { Account(navController = navController,) }
        composable(Tabs.Hike.route) { Hike(mainViewModel, navController)}
        //composable(Tabs.Containers.route) { Containers(mainViewModel) }
        composable(Tabs.Leaderboard.route) { Leaderboard(mainViewModel = mainViewModel) }
        composable(Tabs.Map.route) { Map(mainViewModel = mainViewModel) }

        composable(Screen.Login.route) { Login(navController, mainViewModel) }
        composable(Screen.Register.route) { Register(navController = navController, mainViewModel = mainViewModel) }
        composable(Screen.HikeMap.route) { HikeMap(mainViewModel = mainViewModel, navController = navController) }
        composable(Screen.ManualHike.route) { ManualHike(mainViewModel = mainViewModel, navController = navController)}

    }
}
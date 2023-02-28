package com.natureclean.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.example.test_pvp.navigation.tabs.Account
import com.natureclean.navigation.screens.Login
import com.natureclean.navigation.tabs.Containers
import com.natureclean.navigation.tabs.Map

@Composable
fun Navigation(
    navController: NavController,
    insetsPadding: PaddingValues
) {
    NavHost(navController = navController as NavHostController, startDestination = Screen.Login.route) {
        // Main tabs
        composable(Tabs.Account.route) {
            Account(
                navController = navController,

                )
        }
        composable(Tabs.Map.route) { Map() }
        composable(Tabs.Containers.route) { Containers() }
        composable(Screen.Login.route) {
            Login(
                navController
            )
        }
    }
}
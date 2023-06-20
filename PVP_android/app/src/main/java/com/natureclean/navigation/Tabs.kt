package com.natureclean.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Tabs(val route: String, val title: String, val actionTitle: String, val icon: ImageVector) {
    object Map: Tabs("map", "Map", "Add trash", icon = Icons.Filled.Map)
    object Account : Tabs("account", "Account", "", icon = Icons.Filled.Person)
    object Leaderboard: Tabs("leaderboard", "Leaderboard","", icon = Icons.Filled.Leaderboard)
    object Hike: Tabs("hike", "Hike", "", icon = Icons.Filled.Hiking)

    object Statistics: Tabs("statistics", "Statistics", "",icon = Icons.Filled.Monitor)
}

val bottomBarTabs = listOf(
    Tabs.Map, Tabs.Hike, Tabs.Leaderboard, Tabs.Account
)

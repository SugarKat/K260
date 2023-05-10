package com.natureclean.navigation

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Tabs(val route: String, val title: String, val actionTitle: String, val icon: ImageVector) {
    object Map: Tabs("map", "Map", "Add Point", icon = Icons.Filled.Map)
//    object Containers : Tabs("containers", "Containers", "Add bin", icon = Icons.Filled.List)
    object Account : Tabs("account", "Account", "", icon = Icons.Filled.Person)
    object Leaderboard: Tabs("leaderboard", "Leaderboard","", icon = Icons.Filled.Leaderboard)
    object Hike: Tabs("hike", "Hike", "", icon = Icons.Filled.Hiking)
}

val bottomBarTabs = listOf(
    Tabs.Map, Tabs.Hike, Tabs.Leaderboard, Tabs.Account
)

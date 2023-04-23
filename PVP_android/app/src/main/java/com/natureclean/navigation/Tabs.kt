package com.natureclean.navigation

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Tabs(val route: String, val title: String, val icon: ImageVector) {
    object Map: Tabs("map", "Map", icon = Icons.Filled.Map)
    object Containers : Tabs("containers", "Containers", icon = Icons.Filled.List)
    object Account : Tabs("account", "Account", icon = Icons.Filled.Person)
    object Leaderboard: Tabs("leaderboard", "Leaderboard", icon = Icons.Filled.Leaderboard)
}

val bottomBarTabs = listOf(
    Tabs.Map, Tabs.Containers, Tabs.Leaderboard, Tabs.Account
)

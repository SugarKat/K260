package com.natureclean.navigation


sealed class Tabs(val route: String, val title: String) {
    object Map: Tabs("map", "Map")
    object Containers : Tabs("containers", "Containers")
    object Account : Tabs("account", "Account")
}

val bottomBarTabs = listOf(
    Tabs.Map, Tabs.Containers, Tabs.Account
)

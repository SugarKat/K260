package com.natureclean.navigation

import java.net.URLEncoder

sealed class Screen(val route: String){
    object Login : Screen("screen")
    object Register: Screen("register")
    object HikeMap: Screen("hike_map")
    object ManualHike: Screen("manual_hike")
    object AdminPanel: Screen("admin_panel")

    fun withArgs(vararg args: Pair<String, String>): String {
        var res = route
        args.forEach {
            res = res.replace("{${it.first}}", URLEncoder.encode(it.second, "UTF-8"))
        }
        return res
    }
}
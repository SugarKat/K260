package com.natureclean.navigation


import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.natureclean.ui.components.MainTopAppBar

@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    var topTitle by remember {mutableStateOf("title")}
    val topBar: @Composable () -> Unit = {
        if (isTab(navBackStackEntry)) {
            MainTopAppBar(topTitle)
        }
    }

    val bottomBar: @Composable () -> Unit = {
        if (isTab(navBackStackEntry)) {
            BottomNavigation(
                modifier = Modifier.drawWithContent {
                    drawContent()

                    val strokeWidth = 1f * density
                    val y = strokeWidth / 2
                    drawLine(
                        Color.White,
                        Offset(0f, y),
                        Offset(size.width, y),
                        strokeWidth
                    )
                },
                backgroundColor = Color.Gray,
                elevation = 0.dp
            ) {
                val currentRoute = navBackStackEntry?.destination?.route
                bottomBarTabs.forEach { screen ->
                    val title = screen.title
                    BottomNavigationItem(
                        icon = { Icon(
                            Icons.Rounded.Add,
                            contentDescription = ""
                        ) },
                        label = { Text(
                            title,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = (-0.21).sp
                        ) },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Blue,
                        selected = currentRoute == screen.route,
                        onClick = {
                            topTitle = screen.title
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = { topBar() },
        bottomBar = { bottomBar() },
    ) {
        Navigation(
            navController = navController,
            insetsPadding = it
        )
    }

}

fun isTab(navBackStackEntry: NavBackStackEntry?): Boolean {
    return bottomBarTabs.any { it.route == navBackStackEntry?.destination?.route }
}
package com.natureclean.navigation


import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import com.natureclean.api.model.PollutionPoint
import com.natureclean.ui.components.ContainerAdd
import com.natureclean.ui.components.DARK_GREEN
import com.natureclean.ui.components.MainTopAppBar
import com.natureclean.ui.components.PollutionAdd
import com.natureclean.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppScaffold() {
    val mainViewModel: MainViewModel = hiltViewModel()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val coroutineScope = rememberCoroutineScope()

    val addState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val sheetState =
        rememberBottomSheetScaffoldState(bottomSheetState = addState)

    fun closeSheet() {
        coroutineScope.launch {
            addState.collapse()
        }
    }

    fun openSheet() {
        //modalSheetContent = modalContent
        coroutineScope.launch {
            addState.expand()
        }
    }

    var topTitle by remember { mutableStateOf("Add point") }

    val topBar: @Composable () -> Unit = {
        if (isTab(navBackStackEntry)) {
            MainTopAppBar(topTitle) {
                mainViewModel.topBarAction(navController.currentDestination?.route)
                //mainViewModel.showDialogStatus(true)
                openSheet()
            }
        }
    }
    val bottomBar: @Composable () -> Unit = {
        if (isTab(navBackStackEntry)) {
            BottomNavigation(
                modifier = Modifier.drawWithContent {
                    drawContent()
                },
                backgroundColor = DARK_GREEN,
                elevation = 0.dp
            ) {
                val currentRoute = navBackStackEntry?.destination?.route
                bottomBarTabs.forEach { screen ->
                    val title = screen.title
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = ""
                            )
                        },
                        label = {
                            Text(
                                title,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = (-0.21).sp
                            )
                        },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color(0xFFCCCCCC),
                        selected = currentRoute == screen.route,
                        onClick = {
                            topTitle = screen.actionTitle
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

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            if (mainViewModel.showPollutionAdd.value) {
                PollutionAdd(
                    closeDialog = { closeSheet() },
                    function = { name, description, type ->
                        mainViewModel.registerPoint(
                            name = name,
                            description = description,
                            type = type.toInt()
                        ) {
                            mainViewModel.getPoints()
                        }
                    })
            } else {
                ContainerAdd(closeDialog = { closeSheet() }, register = {
                    mainViewModel.addContainer(it) {
                        mainViewModel.getContainers()
                    }
                })
            }

        },
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        Scaffold(
            topBar = { topBar() },
            bottomBar = { bottomBar() },
        ) {
            Navigation(
                navController = navController,
                mainViewModel = mainViewModel,
                insetsPadding = it
            )
        }
    }


}

fun isTab(navBackStackEntry: NavBackStackEntry?): Boolean {
    return bottomBarTabs.any { it.route == navBackStackEntry?.destination?.route }
}
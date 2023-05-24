package com.natureclean.navigation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.natureclean.viewmodels.MainViewModel

@Composable
fun AdminPanel(mainViewModel: MainViewModel, navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            Icons.Filled.ArrowBack,
            "",
            modifier = Modifier
                .clickable {
                    navController.popBackStack()
                }
                .padding(16.dp)
        )

        Text("admin panel data")


    }
}
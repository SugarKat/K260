package com.natureclean.navigation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.natureclean.viewmodels.MainViewModel

@Composable
fun AdminPanel(mainViewModel: MainViewModel, navController: NavController) {

    val adminData by remember { mainViewModel.adminData }

    LaunchedEffect(Unit) {
        mainViewModel.getAdminData()
    }

    if (adminData != null) {
        Column(
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "",
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack()
                        }

                )
                Spacer(modifier = Modifier.weight(1f))
                Text("STATISTICS", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.weight(1f))
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())

                    .padding(16.dp)
            ) {
                adminData?.let { adminData ->
                    Text("General data:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("User count: ${adminData.userCount}")
                    Text("User total walked: ${adminData.userTotalWalked} km")
                    Text("Total trash : ${adminData.totalTrash}")
                    Text("Total trash cleaned: ${adminData.totalTrashCleaned}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Total trash by type:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    adminData.totalTrashByType.let {
                        Row() {
                            Text("${it.x1[0]}  ${it.x1[1]}")
                        }
                        Row() {
                            Text("${it.x2[0]}  ${it.x2[1]}")
                        }
                        Row() {
                            Text("${it.x3[0]}  ${it.x3[1]}")
                        }
                        Row() {
                            Text("${it.x4[0]}  ${it.x4[1]}")
                        }
                        Row() {
                            Text("${it.x5[0]}  ${it.x5[1]}")
                        }
                    }
                    Text("Total trash by size:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    adminData.totalTrashBySize.let {
                        Row() {
                            Text("${it.x1[0]}  ${it.x1[1]}")
                        }
                        Row() {
                            Text("${it.x2[0]}  ${it.x2[1]}")
                        }
                        Row() {
                            Text("${it.x3[0]}  ${it.x3[1]}")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Total trash cleaned by type:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    adminData.totalTrashCleanedByType.forEach {
                        Row() {
                            Text("${it.x1[0]}  ${it.x1[1]}")
                        }
                        Row() {
                            Text("${it.x2[0]}  ${it.x2[1]}")
                        }
                        Row() {
                            Text("${it.x3[0]}  ${it.x3[1]}")
                        }
                        Row() {
                            Text("${it.x4[0]}  ${it.x4[1]}")
                        }
                        Row() {
                            Text("${it.x5[0]}  ${it.x5[1]}")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Total trash cleaned by type:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    adminData.totalTrashCleanedBySize.let {
                        Row() {
                            Text("${it.x1[0]}  ${it.x1[1]}")
                        }
                        Row() {
                            Text("${it.x2[0]}  ${it.x2[1]}")
                        }
                        Row() {
                            Text("${it.x3[0]}  ${it.x3[1]}")
                        }
                    }
                    Text(
                        "Total active trash by type:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    adminData.totalTrashNotCleanedByType.forEach {
                        Row() {
                            Text("${it.x1[0]}  ${it.x1[1]}")
                        }
                        Row() {
                            Text("${it.x2[0]}  ${it.x2[1]}")
                        }
                        Row() {
                            Text("${it.x3[0]}  ${it.x3[1]}")
                        }
                        Row() {
                            Text("${it.x4[0]}  ${it.x4[1]}")
                        }
                        Row() {
                            Text("${it.x5[0]}  ${it.x5[1]}")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Total active trash cleaned by size:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    adminData.totalTrashNotCleanedBySize.let {
                        Row() {
                            Text("${it.x1[0]}  ${it.x1[1]}")
                        }
                        Row() {
                            Text("${it.x2[0]}  ${it.x2[1]}")
                        }
                        Row() {
                            Text("${it.x3[0]}  ${it.x3[1]}")
                        }
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
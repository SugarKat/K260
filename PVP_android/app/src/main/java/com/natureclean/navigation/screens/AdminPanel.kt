package com.natureclean.navigation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.natureclean.viewmodels.MainViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AdminPanel(mainViewModel: MainViewModel, navController: NavController) {

    val adminData by remember { mainViewModel.adminData }
    var generalDataExpand by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var totalTrashBySizeExpanded by remember {mutableStateOf(false)}
    var totalTrashCleanedByTypeExpanded by remember {mutableStateOf(false)}
    var totalTrashCleanedByPollutionExpanded by remember {mutableStateOf(false)}
    var activeBySizeExpanded by remember {mutableStateOf(false)}
    var activeByTypeExpanded by remember {mutableStateOf(false)}
    LaunchedEffect(Unit) {
        mainViewModel.getAdminData()
    }

    if (adminData != null) {
        Column(
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "",
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack()
                        }
                        .padding(start = 16.dp)

                )
                Spacer(modifier = Modifier.weight(1f))
                Text("STATISTICS", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.offset(x = (-16).dp))
                Spacer(modifier = Modifier.weight(1f))
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp)
            ) {
                adminData?.let { adminData ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { generalDataExpand = !generalDataExpand }
                            .background(Color.LightGray)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text("General data:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Icon(if(generalDataExpand) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward, "", modifier = Modifier.padding(end = 16.dp))

                    }
                    AnimatedContent(targetState = generalDataExpand) { targetState ->
                        if (targetState) {
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                Text("User count: ${adminData.userCount}")
                                Text("User total walked: ${String.format("%.2f", adminData.userTotalWalked)} km")
                                Text("Total trash : ${adminData.totalTrash}")
                                Text("Total trash cleaned: ${adminData.totalTrashCleaned}")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = !expanded }
                            .background(Color.LightGray)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            "Total trash by type",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(if(expanded) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward, "", modifier = Modifier.padding(end = 16.dp))
                    }
                    AnimatedContent(targetState = expanded) { targetState ->
                        if (targetState) {
                            Column(modifier = Modifier.padding(start = 16.dp)) {
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
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { totalTrashBySizeExpanded = !totalTrashBySizeExpanded }
                            .background(Color.LightGray)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text("Total trash by size", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Icon(if(totalTrashBySizeExpanded) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward, "", modifier = Modifier.padding(end = 16.dp))

                    }
                    AnimatedContent(targetState = totalTrashBySizeExpanded) { targetState ->
                        if (targetState) {
                            Column(modifier = Modifier.padding(start = 16.dp)) {
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
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { totalTrashCleanedByTypeExpanded = !totalTrashCleanedByTypeExpanded }
                            .background(Color.LightGray)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            "Total trash cleaned by type",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(if(totalTrashCleanedByTypeExpanded) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward, "", modifier = Modifier.padding(end = 16.dp))


                    }
                    AnimatedContent(targetState = totalTrashCleanedByTypeExpanded) { targetState ->
                        if (targetState) {
                            Column(modifier = Modifier.padding(start = 16.dp)) {
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
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { totalTrashCleanedByPollutionExpanded= !totalTrashCleanedByPollutionExpanded }
                            .background(Color.LightGray)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            "Total trash cleaned by size",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(if(totalTrashCleanedByPollutionExpanded) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward, "", modifier = Modifier.padding(end = 16.dp))


                    }
                    AnimatedContent(targetState = totalTrashCleanedByPollutionExpanded) { targetState ->
                        if (targetState) {
                            Column(modifier = Modifier.padding(start = 16.dp)) {
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
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { activeBySizeExpanded= !activeBySizeExpanded }
                            .background(Color.LightGray)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            "Total active trash by type",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(if(activeBySizeExpanded) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward, "", modifier = Modifier.padding(end = 16.dp))


                    }
                    AnimatedContent(targetState = activeBySizeExpanded) { targetState ->
                        if (targetState) {
                            Column(modifier = Modifier.padding(start = 16.dp)) {
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
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { activeByTypeExpanded = !activeByTypeExpanded }
                            .background(Color.LightGray)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            "Total active trash cleaned by size",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(if(activeByTypeExpanded) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward, "", modifier = Modifier.padding(end = 16.dp))

                    }
                    AnimatedContent(targetState = activeByTypeExpanded) { targetState ->
                        if (targetState) {
                            Column(modifier = Modifier.padding(start = 16.dp)) {
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
                    Spacer(modifier = Modifier.height(8.dp))
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
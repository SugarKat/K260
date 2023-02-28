package com.natureclean.navigation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.natureclean.navigation.Tabs

@Composable
fun Login(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        TextField(
            value = username,
            onValueChange = { username = it },
            placeholder = {Text("user@email.com")},
            label = {Text("username")}
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            label = {Text("password")}

        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Tabs.Map.route) }) {
            Text("Login")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
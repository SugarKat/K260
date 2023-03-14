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
import com.natureclean.api.model.UserCredentials
import com.natureclean.navigation.Tabs
import com.natureclean.viewmodels.MainViewModel

@Composable
fun Login(navController: NavController, mainViewModel: MainViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }

    var error by remember { mainViewModel.errorMessage }

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
            placeholder = { Text("user") },
            label = { Text("username") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("user@email.com") },
            label = { Text("email") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("password") }

        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = passwordConfirmation,
            onValueChange = { passwordConfirmation = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("password") }

        )
        Button(onClick = {
            mainViewModel.loginUser(
                userCreds = UserCredentials(
                    name = username,
                    email = email,
                    password = password,
                    password_confirmation = passwordConfirmation
                )
            ) {
                navController.navigate(Tabs.Map.route)
            }
        }
        ) {
            Text("Login")
        }

        error.apply {
            if (this.isNotEmpty()) {
                Text(this)

            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
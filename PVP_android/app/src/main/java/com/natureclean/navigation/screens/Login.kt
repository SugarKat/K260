package com.natureclean.navigation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.natureclean.api.model.UserCredentials
import com.natureclean.navigateAndClearStack
import com.natureclean.navigation.Screen
import com.natureclean.navigation.Tabs
import com.natureclean.viewmodels.MainViewModel

@Composable
fun Login(navController: NavController, mainViewModel: MainViewModel) {

    val context = LocalContext.current

    val username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val error by remember { mainViewModel.errorMessage }



    fun onValueChange() {
        mainViewModel.resetError()
    }

    fun validate(email: String, password: String){
        if(email.isBlank() || password.isBlank()){
            Toast.makeText(context, "Please fill in the values", Toast.LENGTH_LONG).show()
        }else{
            mainViewModel.loginUser(
                userCreds = UserCredentials(
                    name = username,
                    email = email,
                    password = password,
                )
            ) {
                navController.navigateAndClearStack(Tabs.Map.route)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = email,
            onValueChange = {
                onValueChange()
                email = it
            },
            placeholder = { Text("user@email.com") },
            label = { Text("email") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = {
                password = it; onValueChange()
            },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("password") }

        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
           validate(email, password)
        }
        ) {
            Text("Login")
        }

        error.apply {
            if (this.isNotEmpty()) {
                Text(this)

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate(Screen.Register.route)
        }
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
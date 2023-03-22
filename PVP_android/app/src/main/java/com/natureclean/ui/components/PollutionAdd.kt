package com.natureclean.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.natureclean.api.model.PollutionPoint

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PollutionAdd(closeDialog: () -> Unit, function: (String, String, String) -> Unit){

    var name by remember {mutableStateOf("")}
    var description by remember {mutableStateOf("")}
    var type by remember {mutableStateOf("")}

        AlertDialog(
            title = {
                Text(
                    text = "Register pollution point?",
                    color = Color.Black,
                    fontWeight = FontWeight(700),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("name") },
                        label = { Text("name") },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("description") },
                        label = { Text("description") },
                        modifier = Modifier.padding(bottom = 8.dp)

                    )
                    TextField(
                        value = type,
                        onValueChange = {
                            type = it },
                        placeholder = { Text("type") },
                        label = { Text("type") },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                }
            },
            onDismissRequest = {
                closeDialog()
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            function(name, description, type)
                        },
                    ) { Text("YES!") }
                }
                Spacer(modifier = Modifier.height(24.dp))
            },
            //Shape of the ALERT dialog
            shape = RoundedCornerShape(10.dp),
            //Disabling default alert dialog width
            properties = DialogProperties(usePlatformDefaultWidth = false),
            //Main modifier of the alert dialog box
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .wrapContentHeight()
        )
    }

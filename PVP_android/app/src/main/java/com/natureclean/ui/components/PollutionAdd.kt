package com.natureclean.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import com.natureclean.api.model.Container
import com.natureclean.api.model.PollutionPoint

@Composable
fun PollutionAdd(closeDialog: () -> Unit, function: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register pollution point?".uppercase(),
            color = Color.Black,
            fontWeight = FontWeight(700),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )
        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("name") },
            label = { Text("name") },
            modifier = Modifier.padding(vertical = 16.dp)
        )
        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("description") },
            label = { Text("description") },
            modifier = Modifier.padding(bottom = 16.dp)

        )
        TextField(
            value = type,
            onValueChange = {
                type = it
            },
            placeholder = { Text("type") },
            label = { Text("type") },
            modifier = Modifier.padding(bottom = 16.dp)

        )
        Spacer(modifier = Modifier.height(64.dp))
    }
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(DARK_GREEN),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .weight(1f)
                .requiredHeight(48.dp),
            onClick = {
                if (name.isNotEmpty() && description.isNotEmpty() && type.isNotEmpty()) {
                    function(name, description, type)
                }
            },
        ) {
            Text(
                text = "ADD".uppercase(),
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(64.dp))
        Button(
            colors = ButtonDefaults.buttonColors(DARK_GREEN),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .weight(1f)
                .requiredHeight(48.dp),
            onClick = {
                closeDialog()
            },
        ) {
            Text(
                text = "CANCEL".uppercase(),
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
    Spacer(modifier = Modifier.height(24.dp))


}


@Composable
fun PollutionClean(point: PollutionPoint, closeDialog: () -> Unit, onClick: () -> Unit = {}) {
    var areyousure by remember { mutableStateOf(false) }
    if (!areyousure) {
        AlertDialog(
            title = {
                Text(
                    text = point.name,
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
                    Text(
                        text = point.description ?: "No description",
                        color = Color.Black,
                        fontWeight = FontWeight(700),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Rating ${point.rating}",
                        color = Color.Black,
                        fontWeight = FontWeight(700),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            onDismissRequest = {
                closeDialog()
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            areyousure = true
                        },
                    ) { Text("CLEANED?") }
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
    } else {
        AlertDialog(
            title = {
                Text(
                    text = "Are you sure",
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
                    Text(
                        text = "Are you sure this pollution point is cleaned?",
                        color = Color.Black,
                        fontWeight = FontWeight(700),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            onDismissRequest = {
                closeDialog()
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            areyousure = false
                            onClick()
                        },
                    ) { Text("YES") }
                    Button(
                        onClick = {
                            areyousure = false
                        },
                    ) {
                        Text("NO")
                    }
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
}

@Composable
fun ContainerAdd(closeDialog: () -> Unit, register: (Container) -> Unit) {

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("0.0") }
    var latitude by remember { mutableStateOf("0.0") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ADD NEW BIN".uppercase(),
            color = Color.Black,
            fontWeight = FontWeight(700),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("name") },
            label = { Text("name") },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("description") },
            label = { Text("description") },
            modifier = Modifier.padding(bottom = 16.dp)

        )
        TextField(
            value = latitude,
            onValueChange = { latitude = it },
            placeholder = { Text("latitude") },
            label = { Text("latitude") },
            modifier = Modifier.padding(bottom = 16.dp)

        )
        TextField(
            value = longitude,
            onValueChange = { longitude = it },
            placeholder = { Text("longitude") },
            label = { Text("longitude") },
            modifier = Modifier.padding(bottom = 16.dp)

        )
        TextField(
            value = type,
            onValueChange = {
                type = it
            },
            placeholder = { Text("type") },
            label = { Text("type") },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = size,
            onValueChange = { size = it },
            placeholder = { Text("size") },
            label = { Text("size") },
            modifier = Modifier.padding(bottom = 16.dp)

        )
        Spacer(modifier = Modifier.height(64.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(DARK_GREEN),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .weight(1f)
                    .requiredHeight(48.dp),
                onClick = {
                    register(
                        Container(
                            name = name,
                            description = description,
                            type = type,
                            size = size,
                            longitude = longitude.toDouble(),
                            latitude = latitude.toDouble()
                        )
                    )
                },
            ) {
                Text(
                    text = "ADD".uppercase(),
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(64.dp))
            Button(
                colors = ButtonDefaults.buttonColors(DARK_GREEN),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .weight(1f)
                    .requiredHeight(48.dp),
                onClick = {
                    closeDialog()
                },
            ) {
                Text(
                    text = "CANCEL".uppercase(),
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        //Shape of the ALERT dialog


    }
}


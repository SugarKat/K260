package com.natureclean.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.maps.model.LatLng
import com.natureclean.api.model.Container
import com.natureclean.api.model.PollutionPoint
import com.natureclean.calculateDistance
import com.natureclean.toWasteType
import com.natureclean.viewmodels.sizeBinValues
import com.natureclean.viewmodels.sizePollutionValues
import com.natureclean.viewmodels.typeBinValues
import com.natureclean.viewmodels.typePollutionValues

@Composable
fun PollutionAdd(closeDialog: () -> Unit, function: (String, String, Int, Int) -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var type by remember { mutableStateOf(1) }
    var size by remember { mutableStateOf(1) }


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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
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
        DropDownMenu(typePollutionValues) {
            type = it
        }
        Spacer(modifier = Modifier.height(16.dp))
        DropDownMenu(sizePollutionValues) {
            size = it
        }
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
                if (name.isBlank() || description.isBlank()) {
                    Toast.makeText(context, "Please fill in the values", Toast.LENGTH_LONG)
                        .show()
                } else {
                    function(name, description, type, size)
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
fun PollutionInfo(
    point: PollutionPoint,
    myLocation: LatLng,
    remove: () -> Unit,
    clean: () -> Unit
) {

    val distance = calculateDistance(
        myLocation,
        point.latitude?.let { point.longitude?.let { it1 -> LatLng(it, it1) } })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Litter info:",
            color = Color.Black,
            fontWeight = FontWeight(700),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
        Text("Name: ${point.name}")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Description: ${point.description}")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Litter type: ${point.type.toWasteType()}")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Report count: ${point.reportCount}")
        Spacer(modifier = Modifier.height(16.dp))
        Text("$distance km")
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { remove() }, colors = ButtonDefaults.buttonColors(Color.Red)) {
                Text("Remove from hike")
            }
            Button(onClick = { clean() }, colors = ButtonDefaults.buttonColors(DARK_GREEN)) {
                Text("Cleaned")
            }
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
                        text = "Type: ${point.type.toWasteType()}",
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
fun ContainerAdd(closeDialog: () -> Unit, userLocation: LatLng, register: (Container) -> Unit) {

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var type by remember { mutableStateOf(1) }
    var size by remember { mutableStateOf(1) }


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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
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
        DropDownMenu(typeBinValues) {
            type = it
        }
        Spacer(modifier = Modifier.height(16.dp))
        DropDownMenu(sizeBinValues) {
            size = it
        }
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
                            longitude = userLocation.longitude,
                            latitude = userLocation.latitude
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DropDownMenu(values: Map<Int, String>, onChosen: (Int) -> Unit) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    var selectedText by remember { mutableStateOf(values.values.first()) }
    var selectedKey by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            androidx.compose.material3.TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },

                )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                values.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.value) },
                        onClick = {
                            selectedText = item.value
                            selectedKey = item.key
                            expanded = false
                            Toast.makeText(context, item.value, Toast.LENGTH_SHORT).show()
                            onChosen(selectedKey)
                        }
                    )
                }
            }
        }
    }
}
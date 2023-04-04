package com.natureclean.navigation.tabs

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.natureclean.api.model.Container
import com.natureclean.calculateDistance
import com.natureclean.ui.components.ContainerAdd
import com.natureclean.viewmodels.MainViewModel

@Composable
fun Containers(mainViewModel: MainViewModel) {

    val context = LocalContext.current

    val containers by remember { mainViewModel.containers }
    val addContainer by remember { mainViewModel.showDialog }
    val userLocation by remember { mainViewModel.userLocation }


    if (addContainer) {
        ContainerAdd(closeDialog = { mainViewModel.showDialogStatus(false) }) {
            mainViewModel.addContainer(it) {
                mainViewModel.getContainers()
                mainViewModel.showDialogStatus(false)
            }
        }
    }
    LaunchedEffect(Unit) {
        mainViewModel.getContainers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green.copy(0.3F))
    ) {
        if (containers.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Currently no containers available, please add a container",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn() {
                items(containers) { container ->
                    ContainerItem(context, container, userLocation!!)
                }
            }
        }
    }
}

@Composable

fun ContainerItem(context: Context, container: Container, myDistance: LatLng?) {
    val gmmIntentUri =
        Uri.parse("http://maps.google.com/maps?q=loc:${container.latitude},${container.longitude}")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")

    val distance =
        calculateDistance(myDistance!!,
            container.latitude?.let { container.longitude?.let { it1 -> LatLng(it, it1) } })


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue.copy(0.1F))
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column() {
                Text("Name: ${container.name}", modifier = Modifier.padding(vertical = 4.dp))
                Text("Type: ${container.type}", modifier = Modifier.padding(vertical = 4.dp))
                Text("Size: ${container.size}", modifier = Modifier.padding(vertical = 4.dp))
                Button(onClick = {
                    ContextCompat.startActivity(context, mapIntent, null)
                }) {
                    Text(
                        text = "Open in maps",
                    )
                }

            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (container.latitude != null && container.longitude != null) "Distance: $distance km" else "Unknown distance",
                fontSize = 14.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier
                    .padding(vertical = 4.dp)
            )
        }
    }
}
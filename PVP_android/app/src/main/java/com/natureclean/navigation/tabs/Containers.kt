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
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
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
import com.natureclean.ui.components.DARK_GREEN
import com.natureclean.viewmodels.MainViewModel


val LIGHT_GREY = Color(0xFFdfe3dc)
val containerList = listOf<Container>(
    Container("asdasd", "adsad", 84.949, 84984.78, "asd", "asd"),
    Container("asdasd", "adsad", 84.949, 84984.78, "asd", "asd"),
    Container("asdasd", "adsad", 84.949, 84984.78, "asd", "asd"),
    Container("asdasd", "adsad", 84.949, 84984.78, "asd", "asd"),
)

@Composable
fun Containers(mainViewModel: MainViewModel) {

    val context = LocalContext.current

    val containers by remember { mainViewModel.containers }
    val userLocation by remember { mainViewModel.userLocation }

    
    LaunchedEffect(Unit) {
        mainViewModel.getContainers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (containerList.isEmpty()) { //containerList
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
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn() {
                items(containerList) { container -> //containers
                    ContainerItem(context, container, userLocation!!)
                    Spacer(modifier = Modifier.height(8.dp))
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

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(LIGHT_GREY)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.Top,

    ) {
        Column {
            Text("Name: ${container.name}", modifier = Modifier.padding(vertical = 4.dp))
            Text("Type: ${container.type}", modifier = Modifier.padding(vertical = 4.dp))
            Text("Size: ${container.size}", modifier = Modifier.padding(vertical = 4.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = DARK_GREEN
                ),
                onClick = {
                    ContextCompat.startActivity(context, mapIntent, null)
                }) {
                Text(
                    text = "Open in map".uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Column {
            Spacer(modifier = Modifier.height(4.dp))
            Text("Distance from your location: ")
            Text(
                text = if (container.latitude != null && container.longitude != null) "$distance km" else "Unknown distance",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 4.dp)
            )
        }
    }
}

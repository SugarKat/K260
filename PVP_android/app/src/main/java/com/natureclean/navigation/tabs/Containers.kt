package com.natureclean.navigation.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.natureclean.api.model.Container
import com.natureclean.ui.components.ContainerAdd
import com.natureclean.viewmodels.MainViewModel

@Composable
fun Containers(mainViewModel: MainViewModel) {

    val containers by remember { mainViewModel.containers }
    val addContainer by remember { mainViewModel.showDialog }

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
                    ContainerItem(container)
                }
            }
        }
    }
}
@Composable
fun ContainerItem(container: Container){
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).background(Color.Blue.copy(0.1F))){
        Column() {
            Text("Name: ${container.name}")
            Text("Type: ${container.type}")
            Text("Size: ${container.size}")

        }
    }
}
package com.natureclean.navigation.tabs

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Forward
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.natureclean.navigation.Screen
import com.natureclean.ui.components.DARK_GREEN
import com.natureclean.viewmodels.MainViewModel


@Composable
fun Hike(mainViewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    val points = remember {mainViewModel.pollutionPoints.value.filter { point ->
        point.isActive == 1
    }}


    if(points.size > 1){
        if (showDialog) {
        Dialog(
            onClose = {showDialog = false},
            onProceed = {
                if(it >= 1F) {
                    mainViewModel.setMaxRange(it.toInt())
                    mainViewModel.autoHikeMode()
                    navController.navigate(Screen.HikeMap.route)

                }else{
                    Toast.makeText(context, "Range must be greater than 0 km", Toast.LENGTH_LONG).show()
                }
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Text("Start your hike".uppercase(), fontWeight = FontWeight.Bold, fontSize = 32.sp)
        Spacer(modifier = Modifier.height(128.dp))
        ListElem(
            title = "Generate manually",
            subtitle = "Select manually litter locations, and hike map will be generated",
            image = Icons.Filled.Build,
        ) {
            navController.navigate(Screen.ManualHike.route)
        }
        Spacer(modifier = Modifier.height(16.dp))
        ListElem(
            title = "Generate automatically",
            subtitle = "We will generate your hike path automatically based on your location",
            image = Icons.Filled.ChangeCircle
        ) {
            showDialog = true
        }
        Spacer(modifier = Modifier.weight(1f))
    }}
    else{
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            Text("Currently, there are no active trash sites, hence it is not available to create hiking path for you.", textAlign = TextAlign.Center)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListElem(
    title: String,
    subtitle: String,
    image: ImageVector,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                DARK_GREEN,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onSelect()
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        ListItem(
            icon = {
                Icon(
                    imageVector = image,
                    "",
                    modifier = Modifier.size(48.dp),
                    tint = Color.LightGray
                )
            },
            text = {
                Column {
                    androidx.compose.material.Text(
                        title,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    androidx.compose.material.Text(
                        subtitle,
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            },
            trailing = {
                Icon(
                    imageVector = Icons.Filled.ArrowRightAlt,
                    "",
                    tint = Color.LightGray,
                )
            },
        )
    }
}

@Composable
fun Dialog(
    onClose: () -> Unit,
    onProceed: (Float) -> Unit
) {

    var sliderValue by remember {
        mutableStateOf(0f) // pass the initial value
    }

    AlertDialog(
        title = {
            androidx.compose.material.Text(
                text = "Select hike range\n",
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
                androidx.compose.material.Text(
                    text = "In order to generate hiking path, we need you to select maximum range between trash points from your location.",
                    color = Color.Black,
                    fontWeight = FontWeight(700),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue_ ->
                        sliderValue = sliderValue_
                    },
                    onValueChangeFinished = {

                    },
                    valueRange = 0f..100f
                )
                Text(text = "${sliderValue.toInt()} km")
            }
        },
        onDismissRequest = {
            onClose()
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                androidx.compose.material.Button(
                    onClick = {
                        onClose()
                    },
                ) { androidx.compose.material.Text("Close") }
                Spacer(modifier = Modifier.width(16.dp))
                androidx.compose.material.Button(
                    onClick = {
                        onProceed(sliderValue)
                    },
                ) { androidx.compose.material.Text("Proceed") }
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
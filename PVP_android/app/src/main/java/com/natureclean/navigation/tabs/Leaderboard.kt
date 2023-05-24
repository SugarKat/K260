package com.natureclean.navigation.tabs

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.natureclean.ui.components.DARK_GREEN
import com.natureclean.viewmodels.MainViewModel

const val FILTER_POINTS = "FILTER_POINTS"
const val FILTER_DISTANCE = "FILTER_DISTANCE"

@Composable
fun Leaderboard(mainViewModel: MainViewModel) {

    var filteredBy by remember { mutableStateOf(FILTER_DISTANCE) }

    val users by remember { mainViewModel.users }

    LaunchedEffect(Unit) {
        mainViewModel.getUsers()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (users.isEmpty()) { //users
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {

            Box(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    itemsIndexed(users.sortedByDescending {
                        if(filteredBy == FILTER_POINTS) {
                            it.points
                        }else it.distance_travelled
                    }) { index, user -> //users
                        UserItem(user.name, user.points.toString(), distance = user.distance_travelled.toString(), index = (index + 1).toString())
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                DropDownMenu() {
                    filteredBy = it
                }
            }
        }
    }
}

@Composable
fun DropDownMenu(onChange: (String) -> Unit = {}) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        Icon(Icons.Filled.Sort, "", modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(top = 4.dp, end = 16.dp)
            .background(Color.White, shape = CircleShape)
            .clip(CircleShape)
            .clickable {
                expanded = !expanded; Log.e("clicked", "clicked")
            }
            .padding(8.dp))

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
        ) {
            DropdownMenuItem(
                text = { Text("By distance") },
                onClick = {
                    expanded = false
                    onChange(FILTER_DISTANCE)

                }
            )
            DropdownMenuItem(
                text = { Text("By points") },
                onClick = {
                    expanded = false
                    onChange(FILTER_POINTS)
                }
            )
        }
    }
}

@Composable
fun UserItem(name: String, points: String, distance: String,  index: String) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(LIGHT_GREY)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Text(
            index,
            modifier = Modifier.padding(end = 24.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = DARK_GREEN
        )
        Column {
            Text(
                "Name: $name",
                modifier = Modifier.padding(vertical = 4.dp),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Earned points: $points",
                modifier = Modifier.padding(vertical = 4.dp),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Distance travelled: $distance km",
                modifier = Modifier.padding(vertical = 4.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
package com.natureclean.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.natureclean.api.model.User
import com.natureclean.api.model.UserObj
import com.natureclean.ui.components.DARK_GREEN
import com.natureclean.viewmodels.MainViewModel

val tempList = listOf<UserObj>(
    UserObj(id = "", "adasd", "asdasd", "asdas", 150, "asdsad", "asdasd"),
    UserObj(id = "", "adasd", "asdasd", "asdas", 150, "asdsad", "asdasd"),
    UserObj(id = "", "adasd", "asdasd", "asdas", 150, "asdsad", "asdasd"),
    UserObj(id = "", "adasd", "asdasd", "asdas", 150, "asdsad", "asdasd"),
    UserObj(id = "", "adasd", "asdasd", "asdas", 150, "asdsad", "asdasd"),
    UserObj(id = "", "adasd", "asdasd", "asdas", 150, "asdsad", "asdasd"),

    )

@Composable
fun Leaderboard(mainViewModel: MainViewModel) {
    val users by remember { mainViewModel.users }

    LaunchedEffect(Unit) {
        mainViewModel.getUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (tempList.isEmpty()) { //users
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Leaderbord is not available",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                itemsIndexed(tempList) { index, user -> //users
                    UserItem(user.name, user.points.toString(), (index + 1).toString())
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
fun UserItem(name: String, points: String, index: String) {
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

        }
    }
}
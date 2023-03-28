package com.natureclean.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun MainTopAppBar(title: String, addPoint: () -> Unit) {
    TopAppBar(
        backgroundColor = Color.White,
        elevation = 0.dp
    ) {
        Row(
            modifier= Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = title,
                color = Color.Blue,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,

            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.Add, contentDescription = "", modifier = Modifier.padding(end = 16.dp).clickable {
                addPoint()
            })
        }
    }
}
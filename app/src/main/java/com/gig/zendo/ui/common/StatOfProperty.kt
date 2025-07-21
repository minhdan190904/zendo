package com.gig.zendo.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun StatOfProperty(
    title: String,
    value: String,
    widthOfProperty: Float = 0.65f,
    color: Color = Color.Black
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(widthOfProperty)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = color
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = color
        )
    }
}
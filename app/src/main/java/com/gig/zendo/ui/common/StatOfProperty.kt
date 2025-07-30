package com.gig.zendo.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gig.zendo.ui.presentation.tenant.getAnnotatedString

@Composable
fun StatOfProperty(
    title: String,
    value: String,
    widthOfProperty: Float = 1f,
    color: Color = Color.Black,
    pairOfWeight: Pair<Float, Float> = Pair(2f, 1f)
){
    Row(
        modifier = Modifier.fillMaxWidth(widthOfProperty)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = color,
            modifier = Modifier.weight(pairOfWeight.first)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = color,
            modifier = Modifier.weight(pairOfWeight.second)
        )
    }
}
package com.gig.zendo.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun FunctionIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    @DrawableRes iconRes: Int,
) {
    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.secondary,
        modifier = modifier
            .size(32.dp)
    )
}
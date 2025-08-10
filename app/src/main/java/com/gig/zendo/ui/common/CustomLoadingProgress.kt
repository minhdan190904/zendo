package com.gig.zendo.ui.common

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gig.zendo.ui.theme.DarkGreen

@Composable
fun CustomLoadingProgress(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = Modifier.size(48.dp),
        color = DarkGreen,
    )
}
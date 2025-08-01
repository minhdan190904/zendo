package com.gig.zendo.ui.common

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gig.zendo.ui.theme.DarkGreen

@Composable
fun CustomSwitch(
    onCheckedChange: (Boolean) -> Unit,
    checked: Boolean = false
) {
    Switch(
        checked = checked,
        onCheckedChange = { onCheckedChange(it) },
        colors = SwitchDefaults.colors(
            checkedThumbColor = DarkGreen,
            uncheckedThumbColor = Color.Gray,
            checkedTrackColor = DarkGreen.copy(alpha = 0.5f),
            uncheckedTrackColor = Color.Gray.copy(alpha = 0.5f)
        )
    )
}
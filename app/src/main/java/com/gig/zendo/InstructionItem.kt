package com.gig.zendo

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

data class InstructionItem(
    val id: Int,
    val title: String,
    val description: String,
    @DrawableRes val iconRes: Int
)

package com.gig.zendo.ui.presentation.instruction

import androidx.annotation.DrawableRes

data class InstructionItem(
    val id: Int,
    val title: String,
    val description: String,
    @DrawableRes val iconRes: Int
)

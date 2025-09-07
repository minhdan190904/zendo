package com.gig.zendo.ui.presentation.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.gig.zendo.ui.common.ZendoScaffold

@Composable
fun HouseDetailScreen(
    navController: NavController
) {
    ZendoScaffold(
        title = "Chi tiết nhà trọ",
        onBack = { navController.popBackStack() }
    ) {

    }
}
package com.gig.zendo.ui.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gig.zendo.ui.presentation.auth.AuthViewModel
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.utils.UiState
import kotlinx.coroutines.delay

@Composable
fun SplashScreenComponent() {

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0D47A1), // Xanh dương rất đậm
            Color(0xFF1976D2), // Xanh dương trung bình
            Color(0xFF42A5F5), // Xanh dương sáng
            Color(0xFF64B5F6), // Xanh dương nhạt hơn
        )

    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .systemBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Đang tải Zendo...",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun SplashScreen(
    navController: NavController,
    viewModelAuth: AuthViewModel
) {
    LaunchedEffect(Unit) {
        val currentUser = viewModelAuth.fetchCurrentUser()
        delay(2000)

        if (currentUser is UiState.Success && currentUser.data != null) {
            navController.navigate(Screens.HouseScreen.route) {
                popUpTo(Screens.SplashScreen.route) { inclusive = true }
                launchSingleTop = true
            }

            navController.getBackStackEntry(Screens.HouseScreen.route)
                .savedStateHandle["shouldRefreshHouses"] = true
        } else {
            navController.navigate(Screens.GoogleLoginScreen.route) {
                popUpTo(Screens.SplashScreen.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }


    SplashScreenComponent()
}
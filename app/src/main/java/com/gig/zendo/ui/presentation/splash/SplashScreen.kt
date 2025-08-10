package com.gig.zendo.ui.presentation.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.ui.presentation.auth.AuthViewModel
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.utils.UiState
import kotlinx.coroutines.delay

@Composable
fun CustomLoadingProgress(
    dotCount: Int = 3,
    dotSize: Dp = 8.dp,
    dotColor: Color = Color(0xFF45C3B3),
    spacing: Dp = 12.dp,
    animationDuration: Int = 300
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alphas = List(dotCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = animationDuration * dotCount
                    0.3f at index * animationDuration
                    1f at index * animationDuration + animationDuration / 2
                },
                repeatMode = RepeatMode.Restart
            )
        )
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        alphas.forEach { alphaAnim ->
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(dotColor.copy(alpha = alphaAnim.value))
            )
        }
    }
}

@Composable
fun OnboardingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFA598))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_house),
                contentDescription = "House Illustration",
                modifier = Modifier.size(240.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(vertical = 48.dp, horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Zendo",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF46666)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Quản lý nhà trọ thật dễ dàng",
                            fontSize = 16.sp,
                            color = Color(0xFF9E9E9E)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        CustomLoadingProgress()
                        Spacer(modifier = Modifier.height(40.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Zendo - Ứng dụng quản lý nhà trọ",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "www.github.com/gigzendo",
                                fontSize = 14.sp,
                                color = Color(0xFF00897B)
                            )
                        }
                    }
                }
            }
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
            navController.navigate(Screens.LoginScreen.route) {
                popUpTo(Screens.SplashScreen.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }


    OnboardingScreen()
}
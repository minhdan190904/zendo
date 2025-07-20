package com.gig.zendo.ui.presentation.auth.login

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.ui.presentation.auth.AuthViewModel
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.utils.UiState

@Composable
fun GoogleLoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {

    val context: Context = LocalContext.current
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        authViewModel.loginWithGoogle(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFC0B3), Color(0xFF4CAF50)),
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Icon người dùng nằm giữa
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF00897B),
                    modifier = Modifier
                        .size(48.dp)
                        .padding(16.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Circle + Progress ring
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                // Progress ring vẽ full-size
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 4.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)  // để icon không bị chạm viền
                )
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(color = Color(0x55FFFFFF), shape = CircleShape)
                        .blur(16.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Thông báo đang đăng nhập
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0x55FFFFFF),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Đang đăng nhập bằng Google",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Vui lòng đợi trong giây lát…",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            // Nút Quay lại
            Button(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color(0xFF00897B)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Quay lại",
                    color = Color(0xFF00897B),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(24.dp))

            // Footer
            Text(
                text = "Zendo - Quản lý nhà trọ dễ dàng",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is UiState.Success<*> -> {
                navController.navigate(Screens.HouseScreen.route){
                    popUpTo(Screens.GoogleLoginScreen.route) {
                        inclusive = true
                    }
                }
                snackbarHostState.showSnackbar(
                    "✓ Đăng nhập thành công"
                )
            }

            is UiState.Failure -> {
                navController.popBackStack()
                snackbarHostState.showSnackbar(
                    ("✗ " +
                            (authState as UiState.Failure).error)
                )
            }

            is UiState.Loading, UiState.Empty -> {
                // Do nothing while loading or empty state
            }
        }
    }
}
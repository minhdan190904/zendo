package com.gig.zendo.ui.presentation.auth.login

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.ui.presentation.auth.AuthViewModel
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.utils.GoogleSignInHelper
import com.gig.zendo.utils.UiState

@Composable
fun GoogleLoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val context: Context = LocalContext.current
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    // Legacy GoogleSignIn client
    val googleClient = remember(context) { GoogleSignInHelper.getClient(context) }

    // Nhận kết quả đăng nhập
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val idToken = GoogleSignInHelper.extractIdToken(result.data)
                authViewModel.loginWithGoogleIdToken(idToken)
            } catch (e: Exception) {
                authViewModel.onAuthFailure("Google Sign-In error: ${e.message}")
            }
        } else {
            authViewModel.onAuthFailure("Sign-In cancelled (code=${result.resultCode})")
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is UiState.Success -> {
                navController.navigate(Screens.HouseScreen.route) {
                    popUpTo(Screens.GoogleLoginScreen.route) { inclusive = true }
                }
                navController.getBackStackEntry(Screens.HouseScreen.route)
                    .savedStateHandle["shouldRefreshHouses"] = true
            }
            is UiState.Failure -> {
                Log.i("GoogleLoginScreen", "Login failed: ${(authState as UiState.Failure).error}")
            }
            else -> Unit
        }
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2), Color(0xFF42A5F5), Color(0xFF64B5F6))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .systemBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(28.dp))
            Logo()
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Zendo",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    fontSize = 40.sp,
                    shadow = Shadow(Color.Black.copy(alpha = 0.3f), Offset(3f, 3f), 6f)
                )
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Quản lý nhà trọ thật dễ dàng",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(56.dp))
            Text(
                text = "Chào mừng trở lại",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Đăng nhập để tiếp tục quản lý\nhệ thống nhà trọ của bạn",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(56.dp))

            // NÚT GOOGLE – legacy flow
            GoogleButton(
                onClick = {
                    authViewModel.beginGoogleLogin()
                    // (tuỳ chọn) luôn hiện chọn tài khoản:
                    // googleClient.signOut().addOnCompleteListener { googleLauncher.launch(googleClient.signInIntent) }
                    googleLauncher.launch(googleClient.signInIntent)
                },
                isLoading = authState is UiState.Loading
            )

            Spacer(Modifier.height(26.dp))
            Text(
                text = "Vì sao nên chọn Zendo?",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FeatureCard(
                    iconRes = R.drawable.ic_robot,
                    title = "Quản lý phòng",
                    caption = "Tạo/sửa phòng\nTheo dõi người thuê\nTrạng thái trống/đang thuê",
                    height = 220.dp
                )
                FeatureCard(
                    iconRes = R.drawable.ic_robot,
                    title = "Hoá đơn",
                    caption = "Ghi điện nước\nNhắc ngày thu\nXuất hoá đơn"
                )
                FeatureCard(
                    iconRes = R.drawable.ic_robot,
                    title = "Báo cáo",
                    caption = "Doanh thu – công nợ\nBáo cáo trực quan"
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Logo() {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo_app),
                contentDescription = "Logo",
                modifier = Modifier.size(72.dp),
                alignment = Alignment.Center
            )
        }
    }
}

@Composable
private fun GoogleButton(onClick: () -> Unit, isLoading: Boolean = false) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContentColor = Color(0xFF42A5F5),
            disabledContainerColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        enabled = !isLoading
    ) {
        if (!isLoading) {
            Icon(
                painter = painterResource(R.drawable.ic_google),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color(0xFF42A5F5),
                strokeWidth = 1.dp
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = if (isLoading) "Đang đăng nhập..." else "Đăng nhập với Google",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
        )
    }
}

@Composable
private fun RowScope.FeatureCard(
    iconRes: Int, title: String, caption: String, height: Dp = 180.dp
) {
    Card(
        modifier = Modifier.weight(1f).height(height).border(
            width = 1.dp, color = Color.White.copy(alpha = 0.35f), shape = RoundedCornerShape(18.dp)
        ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White.copy(alpha = 0.14f),
            contentColor = Color.White,
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(painter = painterResource(iconRes), contentDescription = null,
                tint = Color.White, modifier = Modifier.size(28.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold, color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.7f), lineHeight = 14.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

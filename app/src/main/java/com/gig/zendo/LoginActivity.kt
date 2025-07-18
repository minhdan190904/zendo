package com.gig.zendo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.gig.zendo.ui.theme.Silver
import com.gig.zendo.ui.theme.ZendoTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    private lateinit var credentialManager: CredentialManager

    override fun onStart() {
        super.onStart()
        credentialManager = CredentialManager.create(this)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val sysUi = rememberSystemUiController()
            SideEffect {
                // 2️⃣ Làm status bar trong suốt
                sysUi.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = true
                )
            }

            ZendoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreenWithRoundedContainer(
                        onGoogleClick = {
                            lifecycleScope.launch {
                                GoogleSignInHelper.handleGoogleSignIn(
                                    context = this@LoginActivity,
                                    credentialManager = credentialManager,
                                    onSuccess = { msg -> showToast(msg) },
                                    onError = { err -> showToast(err) }
                                )
                            }
                        },

                        onLoginClick = { email, pass ->
                            // TODO: Xử lý login bằng email/pass
                        }
                    )
//                    GoogleLoadingScreen {
//
//                    }
                }
            }
        }
    }


    @Composable
    fun LoginScreenWithRoundedContainer(
        onGoogleClick: () -> Unit,
        onLoginClick: (String, String) -> Unit
    ) {
        // State hoá email/password
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Header hồng + icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .background(color = Color(0xFFFFC0B3))
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_house),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.TopCenter)
                        .padding(top = 32.dp),
                    contentScale = ContentScale.FillBounds
                )
            }

            // 2. Container trắng bo góc trên
            Surface(
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = 200.dp)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "Chủ trọ",
                        fontSize = 32.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFF7043),
                    )
                    Text(
                        text = "Quản lý nhà trọ thật dễ dàng",
                        fontWeight = FontWeight(500),
                        color = Silver,
                    )

                    Spacer(Modifier.height(24.dp))

                    GoogleSignInButton(onClick = onGoogleClick)

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Hoặc đăng nhập",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email đăng nhập") },
                        placeholder = { Text("Ví dụ: abc1234@gmail.com") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Mật khẩu") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Nút Đăng ký (Outlined)
                        ElevatedButton(
                            onClick = {},
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Đăng ký")
                        }

                        // Nút Đăng nhập (Filled)
                        ElevatedButton(
                            onClick = { onLoginClick(email, password) },
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Đăng nhập")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun GoogleLoadingScreen(
        onBack: () -> Unit
    ) {
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
                    onClick = onBack,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
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
                    text = "Cretisoft – Giải pháp phần mềm chuyên nghiệp",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun PreviewGoogleLoading() {
        GoogleLoadingScreen(onBack = {})
    }

    @Composable
    fun GoogleSignInButton(onClick: () -> Unit) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_google),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Đăng nhập với Google",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewLoginScreen() {
        ZendoTheme {
            LoginScreenWithRoundedContainer(
                onGoogleClick = {},
                onLoginClick = { _, _ -> }
            )
        }
    }
}

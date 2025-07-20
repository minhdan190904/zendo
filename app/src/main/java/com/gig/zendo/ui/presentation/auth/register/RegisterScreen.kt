package com.gig.zendo.ui.presentation.auth.register

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.ui.common.InputType
import com.gig.zendo.ui.common.LabeledTextField
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.ui.presentation.auth.AuthViewModel
import com.gig.zendo.ui.presentation.navigation.Screens
import com.gig.zendo.utils.UiState

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val email by viewModel.emailRegister
    val password by viewModel.passwordRegister
    val confirmPassword by viewModel.confirmPasswordRegister

    val authState by viewModel.authState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        // Header background and icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(Color(0xFFFFC0B3))
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

        // White rounded container
        Surface(
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 200.dp)
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
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF7043)
                )
                Text(
                    text = "Quản lý nhà trọ thật dễ dàng",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(24.dp))

                LabeledTextField(
                    label = "Email đăng ký",
                    value = email,
                    onValueChange = { viewModel.updateEmailRegister(it) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    inputType = InputType.EMAIL,
                    useInternalLabel = false,
                    placeholder = "Ví dụ: abc123@gmail.com"
                )

                Spacer(Modifier.height(24.dp))

                LabeledTextField(
                    label = "Mật khẩu",
                    value = password,
                    onValueChange = { viewModel.updatePasswordRegister(it) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    inputType = InputType.PASSWORD,
                    useInternalLabel = false
                )

                Spacer(Modifier.height(24.dp))

                LabeledTextField(
                    label = "Mật khẩu xác nhận",
                    value = confirmPassword,
                    onValueChange = { viewModel.updateConfirmPasswordRegister(it) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    inputType = InputType.PASSWORD,
                    useInternalLabel = false
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    ElevatedButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Quay lại")
                    }

                    ElevatedButton(
                        onClick = { viewModel.register(email, password) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Đăng ký")
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }

    LoadingScreen(isLoading = authState is UiState.Loading)

    LaunchedEffect(authState) {
        when (val state = authState) {
            is UiState.Success<*> -> {
                snackbarHostState.showSnackbar("✓ Đăng ký thành công!")
            }

            is UiState.Failure -> snackbarHostState.showSnackbar(
                ("✗ " +
                        state.error) ?: "Đăng ký thất bại"
            )

            is UiState.Loading, UiState.Empty -> {
                // Do nothing while loading or empty state
            }
        }
    }
}

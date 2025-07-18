package com.gig.zendo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.gig.zendo.ui.theme.ZendoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // 1) Launcher cho FirebaseUI (Email/password + Google fallback)
    private val authUiLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result: FirebaseAuthUIAuthenticationResult ->
        handleAuthUIResult(result)
    }

    // 2) CredentialManager cho flow Google Sign-In tùy chỉnh (One-Tap / GoogleIdToken)
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        credentialManager = CredentialManager.create(this)

        setContent {
            ZendoTheme {
                // Trạng thái cho form
                var houseName by remember { mutableStateOf("") }
                var houseAddress by remember { mutableStateOf("") }
                val auth = FirebaseAuth.getInstance()
//                InstructionScreen(onBack = { finish() })
//                PropertyCard(
//                    title = "Nhà trọ con cặc",
//                    address = "123 Nguyễn Trường Tộ",
//                    roomCount = 0,
//                    availableCount = 0,
//                    overdueCount = 0,
//                    overdueAmount = 0,
//                    revenueThisMonth = 0,
//                    billingMonth = "7",
//                    billingDay = 1,
//                    onDetailClick = { /* no-op */ },
//                    onMenuClick   = { /* no-op */ },
//                    modifier      = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                )


//                CreateHouseScreen(
//                    houseName = houseName,
//                    onHouseNameChange = { houseName = it },
//                    houseAddress = houseAddress,
//                    onHouseAddressChange = { houseAddress = it },
//                    onSaveClick = {
//                        // TODO: lưu thông tin houseName & houseAddress vào Firebase/Room/…
//                    },
//                    onBack = {
//                        // TODO: nếu muốn quay về màn trước, ví dụ finish()
//                        finish()
//                    }
//                )
                val actions = listOf(
                    HomeAction(R.drawable.ic_add, "Thêm phòng") { /* onAddRoom() */ },
                    HomeAction(R.drawable.ic_lightbub, "Ghi điện nước") { /* onRecord() */ },
                    HomeAction(R.drawable.ic_money, "Thu tiền") { /* onCollect() */ },
                    HomeAction(R.drawable.ic_setting, "Cài đặt") { /* onSettings() */ },
                    HomeAction(R.drawable.ic_guide, "Hướng dẫn") { /* onGuide() */ },
                )

                HomeEmptyScreen(actions = actions)
            }
        }
    }

    // Khởi chạy FirebaseUI AuthUI với Email + GoogleProvider
    private fun launchFirebaseUI() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        authUiLauncher.launch(intent)
    }

    // Xử lý kết quả từ FirebaseUI
    private fun handleAuthUIResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, "AuthUI: Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "AuthUI: Đăng nhập thất bại: ${result.idpResponse?.error?.errorCode}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Flow custom Google Sign-In qua CredentialManager API
    private fun launchCustomGoogleSignIn() {
        lifecycleScope.launch {
            GoogleSignInHelper.handleGoogleSignIn(
                context = this@MainActivity,
                credentialManager = credentialManager,
                onSuccess = { msg -> showToast("GM: $msg") },
                onError = { err -> showToast("GM error: $err") }
            )
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

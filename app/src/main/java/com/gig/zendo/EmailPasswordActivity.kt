package com.gig.zendo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

class EmailPasswordActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        setContent {
            MaterialTheme {
                AuthScreen(
                    onRegister = { email, password -> createAccount(email, password) },
                    onLogin = { email, password -> signIn(email, password) },
                    onSendVerify = { sendEmailVerification() }
                )
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Tạo tài khoản thất bại.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Đăng nhập thất bại.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        if (user != null && !user.isEmailVerified) {
            user.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email xác minh đã được gửi.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        // Bạn có thể cập nhật trạng thái giao diện tại đây nếu muốn
        if (user != null) {
            Toast.makeText(this, "Xin chào ${user.email}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}

@Composable
fun AuthScreen(
    onRegister: (String, String) -> Unit,
    onLogin: (String, String) -> Unit,
    onSendVerify: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onLogin(email, password) }, modifier = Modifier.fillMaxWidth()) {
            Text("Đăng nhập")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onRegister(email, password) }, modifier = Modifier.fillMaxWidth()) {
            Text("Đăng ký")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onSendVerify, modifier = Modifier.fillMaxWidth()) {
            Text("Gửi email xác minh")
        }
    }
}

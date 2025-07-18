package com.gig.zendo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.gig.zendo.FirebaseUserManager.deleteUser
import com.gig.zendo.FirebaseUserManager.login
import com.gig.zendo.FirebaseUserManager.logout
import com.gig.zendo.FirebaseUserManager.register
import com.gig.zendo.FirebaseUserManager.sendEmailVerification
import com.gig.zendo.FirebaseUserManager.sendPasswordResetEmail
import com.gig.zendo.FirebaseUserManager.updateEmail
import com.gig.zendo.FirebaseUserManager.updatePassword
import com.gig.zendo.ui.theme.ZendoTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AuthActivity : ComponentActivity() {
    private lateinit var credentialManager: CredentialManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        credentialManager = CredentialManager.create(this@AuthActivity)

        setContent {
            ZendoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GoogleSignInScreen(
                        onSignIn = {
                            lifecycleScope.launch {
                                GoogleSignInHelper.handleGoogleSignIn(
                                    context = this@AuthActivity,
                                    credentialManager = credentialManager,
                                    onSuccess = { msg -> showToast(msg) },
                                    onError = { err -> showToast(err) }
                                )
                            }
                        }
                    )
                }
            }
        }

//        setContent {
//            ZendoTheme {
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    AccountScreen()
//                }
//            }
//        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    @Composable
    fun GoogleSignInScreen(onSignIn: () -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Google Sign-In", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(24.dp))

            Button(onClick = onSignIn) {
                Text("Sign in with Google")
            }

            Spacer(Modifier.height(32.dp))
            if (user != null) {
                Text("Signed in as: ${user.email}")
            } else {
                Text("Not signed in.")
            }
        }
    }

    @Composable
    fun AccountScreen() {
        val firebaseAuth = Firebase.auth
        var currentUser by remember { mutableStateOf<FirebaseUser?>(firebaseAuth.currentUser) }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var newEmail by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var resetEmail by remember { mutableStateOf("") }

        fun showToast(msg: String) =
            Toast.makeText(this@AuthActivity, msg, Toast.LENGTH_SHORT).show()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Firebase Auth", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Password") }, modifier = Modifier.fillMaxWidth()
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(modifier = Modifier.weight(1f), onClick = {
                    register(email, password) { ok, user ->
                        if (ok) {
                            currentUser = user
                            showToast("Registered – verification sent")
                        } else showToast("Register failed")
                    }
                }) { Text("Register") }

                Button(modifier = Modifier.weight(1f), onClick = {
                    login(email, password) { ok, user ->
                        if (ok) {
                            currentUser = user
                            showToast("Login success")
                        } else showToast("Login failed")
                    }
                }) { Text("Login") }
            }

            Spacer(Modifier.height(12.dp))
            Divider()
            Spacer(Modifier.height(12.dp))

            Text("Current user:")
            if (currentUser != null) {
                Text("Email: ${currentUser!!.email}")
                Text("Verified: ${currentUser!!.isEmailVerified}")
            } else {
                Text("Not signed in")
            }

            Button(onClick = {
                sendEmailVerification { ok ->
                    if (ok) showToast("Verification email sent")
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Send Verification Email")
            }

            Button(onClick = {
                Firebase.auth.currentUser?.reload()?.addOnCompleteListener {
                    currentUser = Firebase.auth.currentUser
                    showToast("Verification status reloaded")
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Kiểm tra xác minh email")
            }

            OutlinedTextField(
                value = newEmail, onValueChange = { newEmail = it },
                label = { Text("New Email") }, modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                updateEmail(newEmail) { ok ->
                    if (ok) showToast("Email updated")
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Update Email")
            }

            OutlinedTextField(
                value = newPassword, onValueChange = { newPassword = it },
                label = { Text("New Password") }, modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                updatePassword(newPassword) { ok ->
                    if (ok) showToast("Password updated")
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Update Password")
            }

            OutlinedTextField(
                value = resetEmail, onValueChange = { resetEmail = it },
                label = { Text("Reset Email") }, modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                sendPasswordResetEmail(resetEmail) { ok ->
                    if (ok) showToast("Reset email sent")
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Send Password Reset")
            }

            Button(onClick = {
                deleteUser { ok ->
                    if (ok) {
                        currentUser = null
                        showToast("Account deleted")
                    }
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Delete Account")
            }

            Button(onClick = {
                logout()
                currentUser = null
                showToast("Logged out")
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Logout")
            }
        }
    }
}

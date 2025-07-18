
package com.gig.zendo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SignInScreen(
    onEmailSignIn: () -> Unit,
    onGoogleSignIn: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onEmailSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Sign in with Email / Password")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onGoogleSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Sign in with Google (Custom)")
        }
    }
}

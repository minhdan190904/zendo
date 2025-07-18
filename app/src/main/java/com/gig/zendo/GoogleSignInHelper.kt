package com.gig.zendo

import android.content.Context
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

object GoogleSignInHelper {
    private const val TAG = "GoogleSignInHelper"

    private fun getGoogleSignInRequest(context: Context): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    suspend fun handleGoogleSignIn(
        context: Context,
        credentialManager: CredentialManager,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val request = getGoogleSignInRequest(context)
            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                firebaseAuthWithGoogle(googleIdToken, onSuccess, onError)
            } else {
                onError("Credential is not a Google ID token")
            }
        } catch (e: GetCredentialException) {
            onError("Failed to get credential: ${e.message}")
        }
    }

    private fun firebaseAuthWithGoogle(
        idToken: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    onSuccess("Login successful: ${user?.email}")
                } else {
                    onError("Login failed: ${task.exception?.message}")
                }
            }
    }
}

@file:Suppress("DEPRECATION")

package com.gig.zendo.utils

import android.content.Intent
import android.content.Context
import com.gig.zendo.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

object GoogleSignInHelper {

    fun getClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder()
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    fun extractIdToken(data: Intent?): String {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
        return account.idToken ?: throw IllegalStateException("Google account returned null idToken")
    }
}

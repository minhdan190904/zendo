// app/src/main/java/com/gig/zendo/FirebaseUserManager.kt
package com.gig.zendo

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest

object FirebaseUserManager {
    private const val TAG = "FirebaseUserManager"
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(email: String, password: String, onResult: (Boolean, FirebaseUser?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "register: success")
                    auth.currentUser?.sendEmailVerification()
                    onResult(true, auth.currentUser)
                } else {
                    Log.w(TAG, "register: failure", task.exception)
                    onResult(false, null)
                }
            }
    }

    fun login(email: String, password: String, onResult: (Boolean, FirebaseUser?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "login: success")
                    onResult(true, auth.currentUser)
                } else {
                    Log.w(TAG, "login: failure", task.exception)
                    onResult(false, null)
                }
            }
    }

    fun logout() {
        auth.signOut()
        Log.d(TAG, "logout: user signed out")
    }

    fun sendEmailVerification(onResult: (Boolean) -> Unit) {
        val user = auth.currentUser ?: return onResult(false)
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
                if (task.isSuccessful) Log.d(TAG, "verification email sent")
            }
    }

    fun updateEmail(newEmail: String, onResult: (Boolean) -> Unit) {
        val user = auth.currentUser ?: return onResult(false)
        user.updateEmail(newEmail)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
                if (task.isSuccessful) Log.d(TAG, "email updated")
            }
    }

    fun updatePassword(newPassword: String, onResult: (Boolean) -> Unit) {
        val user = auth.currentUser ?: return onResult(false)
        user.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
                if (task.isSuccessful) Log.d(TAG, "password updated")
            }
    }

    fun sendPasswordResetEmail(email: String, onResult: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
                if (task.isSuccessful) Log.d(TAG, "password reset email sent")
            }
    }

    fun deleteUser(onResult: (Boolean) -> Unit) {
        val user = auth.currentUser ?: return onResult(false)
        user.delete()
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
                if (task.isSuccessful) Log.d(TAG, "user deleted")
            }
    }

    fun reauthenticate(email: String, password: String, onResult: (Boolean) -> Unit) {
        val user = auth.currentUser ?: return onResult(false)
        val credential = EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
                if (task.isSuccessful) Log.d(TAG, "user reauthenticated")
            }
    }
}

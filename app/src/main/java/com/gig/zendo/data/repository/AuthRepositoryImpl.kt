package com.gig.zendo.data.repository

import com.gig.zendo.domain.model.User
import com.gig.zendo.domain.model.toUser
import com.gig.zendo.domain.repository.AuthRepository
import com.gig.zendo.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override suspend fun loginWithEmail(email: String, password: String): UiState<User> {
        return try {
            val firebaseUser = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = firebaseUser.user
                ?: return UiState.Failure("Login failed, no user returned")
            UiState.Success(user.toUser())
        } catch (e: Exception) {
            return UiState.Failure(e.message ?: "An error occurred during login")
        }
    }

    override suspend fun loginWithGoogle(idToken: String): UiState<User> {
        try {
            val cred = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(cred).await()
            return UiState.Success(result.user!!.toUser())
        } catch (e: Exception) {
            return UiState.Failure(e.message)
        }
    }

    override suspend fun registerWithEmail(email: String, password: String): UiState<User> {
        return try {
            val firebaseUser =
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = firebaseUser.user
                ?: return UiState.Failure("User creation failed, no user returned")
            UiState.Success(user.toUser())
        } catch (e: Exception) {
            return UiState.Failure(e.message ?: "An error occurred during registration")
        }
    }

    override fun getCurrentUser(): UiState<User?> {
        return try {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                UiState.Success(firebaseUser.toUser())
            } else {
                UiState.Success(null)
            }
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "An error occurred while retrieving the current user")
        }
    }

    override suspend fun checkUserAuthenticatedInFirebase(): UiState<User> {
        return try {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                UiState.Success(firebaseUser.toUser())
            } else {
                UiState.Failure("No authenticated user found")
            }
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "An error occurred while checking authentication")
        }
    }

    override suspend fun logout(): UiState<Unit> {
        return try {
            firebaseAuth.signOut()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Failure(e.message ?: "An error occurred during logout")
        }
    }

}
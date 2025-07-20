package com.gig.zendo.domain.repository

import com.gig.zendo.domain.model.User
import com.gig.zendo.utils.UiState

interface AuthRepository {
    suspend fun loginWithEmail(email: String, password: String): UiState<User>
    suspend fun loginWithGoogle(idToken: String): UiState<User>
    suspend fun registerWithEmail(email: String, password: String): UiState<User>
    fun getCurrentUser(): UiState<User?>
    suspend fun checkUserAuthenticatedInFirebase(): UiState<User>
    suspend fun logout(): UiState<Unit>
}
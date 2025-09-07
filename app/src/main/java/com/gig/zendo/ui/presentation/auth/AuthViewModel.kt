package com.gig.zendo.ui.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.domain.model.User
import com.gig.zendo.domain.repository.AuthRepository
import com.gig.zendo.utils.GoogleSignInHelper
import com.gig.zendo.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val authState: StateFlow<UiState<Any>> = _authState

    fun login(email: String, pwd: String) {
        _authState.value = UiState.Loading
        viewModelScope.launch { _authState.value = authRepository.loginWithEmail(email, pwd) }
    }

    fun register(email: String, pwd: String) {
        _authState.value = UiState.Loading
        viewModelScope.launch { _authState.value = authRepository.registerWithEmail(email, pwd) }
    }

    fun beginGoogleLogin() { _authState.value = UiState.Loading }

    fun loginWithGoogleIdToken(idToken: String) {
        viewModelScope.launch {
            try {
                _authState.value = authRepository.loginWithGoogle(idToken)
            } catch (e: Exception) {
                _authState.value = UiState.Failure(e.message ?: "Google Sign-In failed")
            }
        }
    }

    fun onAuthFailure(message: String) { _authState.value = UiState.Failure(message) }

    fun fetchCurrentUser(): UiState<User?> = authRepository.getCurrentUser()

    fun logout(context: Context) {
        viewModelScope.launch {
            try {
                GoogleSignInHelper.getClient(context).signOut().await()
            } catch (_: Exception) {  }

            _authState.value = UiState.Loading
            _authState.value = authRepository.logout()
        }
    }


    fun clearAuthState() { _authState.value = UiState.Empty }
}

package com.gig.zendo.ui.presentation.auth

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.utils.GoogleSignInHelper
import com.gig.zendo.domain.model.User
import com.gig.zendo.domain.repository.AuthRepository
import com.gig.zendo.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val credentialManager: CredentialManager
) : ViewModel() {

    private val _authState = MutableStateFlow<UiState<Any>>(UiState.Empty)
    val authState: StateFlow<UiState<Any>> = _authState

    fun login(email: String, pwd: String) {
        _authState.value = UiState.Loading
        viewModelScope.launch {
            _authState.value = authRepository.loginWithEmail(email, pwd)
        }
    }

    fun register(email: String, pwd: String) {
        _authState.value = UiState.Loading
        viewModelScope.launch {
            _authState.value = authRepository.registerWithEmail(email, pwd)
        }
    }

    fun loginWithGoogle(context: Context) {
        viewModelScope.launch {
            try {
                val idToken = GoogleSignInHelper.fetchIdToken(context, credentialManager)
                _authState.value = UiState.Loading
                _authState.value = authRepository.loginWithGoogle(idToken)
            } catch (e: Exception) {
                _authState.value = UiState.Failure(e.message)
            }
        }
    }

    fun fetchCurrentUser(): UiState<User?> {
        val userState = authRepository.getCurrentUser()
        return userState
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            _authState.value = authRepository.logout()
        }
    }

    fun clearAuthState() {
        _authState.value = UiState.Empty
    }
}
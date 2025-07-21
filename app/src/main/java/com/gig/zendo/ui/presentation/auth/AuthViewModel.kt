package com.gig.zendo.ui.presentation.auth

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.GoogleSignInHelper
import com.gig.zendo.data.repository.AuthRepositoryImpl
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

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog: StateFlow<Boolean> = _showLogoutDialog

    val currentUser = MutableStateFlow<User?>(null)

    init {
//        checkUserLoggedIn()
        getCurrentUser()
    }

    //for login screen
    var emailLogin = mutableStateOf("")
        private set
    var passwordLogin = mutableStateOf("")
        private set

    //for register screen
    var emailRegister = mutableStateOf("")
        private set
    var passwordRegister = mutableStateOf("")
        private set
    var confirmPasswordRegister = mutableStateOf("")
        private set

    fun checkUserLoggedIn() {
        viewModelScope.launch {
            _authState.value = authRepository.checkUserAuthenticatedInFirebase()
        }
    }

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

    private fun getCurrentUser() {
        viewModelScope.launch {
            val result = authRepository.getCurrentUser()
            if (result is UiState.Success) {
                currentUser.value = result.data
            } else {
                currentUser.value = null
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            _authState.value = authRepository.logout()
        }
    }

    fun showLogoutDialog() {
        _showLogoutDialog.value = true
    }

    fun dismissLogoutDialog() {
        _showLogoutDialog.value = false
    }

    fun updateEmailLogin(newEmail: String) {
        emailLogin.value = newEmail
    }

    fun updatePasswordLogin(newPassword: String) {
        passwordLogin.value = newPassword
    }

    fun updateEmailRegister(newEmail: String) {
        emailRegister.value = newEmail
    }

    fun updatePasswordRegister(newPassword: String) {
        passwordRegister.value = newPassword
    }

    fun updateConfirmPasswordRegister(newConfirmPassword: String) {
        confirmPasswordRegister.value = newConfirmPassword
    }
}
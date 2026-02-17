package com.krishiai.app.ui.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishiai.app.data.model.User
import com.krishiai.app.data.repository.AuthRepository
import com.krishiai.app.data.repository.OtpResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiState {
    object Initial : AuthUiState()
    object Loading : AuthUiState()
    object OtpSent : AuthUiState()
    object Verified : AuthUiState() // Valid User
    object NewUser : AuthUiState() // Needs Profile Setup
    data class Error(val message: String) : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private var verificationId: String? = null

    // For profile setup
    private val _currentUserUid = MutableStateFlow<String?>(null)
    val currentUserUid: StateFlow<String?> = _currentUserUid.asStateFlow()

    fun sendOtp(phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            authRepository.sendOtp(phoneNumber, activity)
                .catch { e ->
                    _uiState.value = AuthUiState.Error(e.message ?: "Failed to send OTP")
                }
                .collect { result ->
                    when (result) {
                        is OtpResult.CodeSent -> {
                            verificationId = result.verificationId
                            _uiState.value = AuthUiState.OtpSent
                        }
                        is OtpResult.AutoVerified -> {
                             checkUserStatus(authRepository.currentUser?.uid ?: "")
                        }
                        is OtpResult.Error -> {
                            _uiState.value = AuthUiState.Error(result.message)
                        }
                    }
                }
        }
    }

    fun verifyOtp(code: String) {
        val vid = verificationId
        if (vid == null) {
            _uiState.value = AuthUiState.Error("Verification ID is null")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.verifyOtp(vid, code)
            result.onSuccess { uid ->
                _currentUserUid.value = uid
                checkUserStatus(uid)
            }.onFailure { e ->
                _uiState.value = AuthUiState.Error(e.message ?: "Verification failed")
            }
        }
    }

    private suspend fun checkUserStatus(uid: String) {
        val existsResult = authRepository.checkUserExists(uid)
        existsResult.onSuccess { exists ->
            if (exists) {
                _uiState.value = AuthUiState.Verified
            } else {
                _uiState.value = AuthUiState.NewUser
            }
        }.onFailure {
            // If check fails, assume net error, or process as new user?
            // Safer to show error or retry.
             _uiState.value = AuthUiState.Error("Failed to check user profile")
        }
    }
    
    fun saveProfile(user: User) {
         viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val uid = _currentUserUid.value ?: return@launch
            val userWithUid = user.copy(uid = uid)
            
            authRepository.createUserProfile(userWithUid)
                .onSuccess {
                    _uiState.value = AuthUiState.Verified
                }
                .onFailure { e ->
                    _uiState.value = AuthUiState.Error(e.message ?: "Failed to save profile")
                }
        }
    }
    fun signOut() {
        authRepository.signOut()
        _currentUserUid.value = null
        _uiState.value = AuthUiState.Initial
    }
}

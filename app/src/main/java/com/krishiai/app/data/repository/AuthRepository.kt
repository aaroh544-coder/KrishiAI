package com.krishiai.app.data.repository

import android.app.Activity
import com.krishiai.app.data.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: User?
    
    fun sendOtp(phoneNumber: String, activity: Activity): Flow<OtpResult>
    suspend fun verifyOtp(verificationId: String, code: String): Result<String> // Returns UID
    suspend fun createUserProfile(user: User): Result<Unit>
    suspend fun getUserProfile(uid: String): Result<User?>
    suspend fun checkUserExists(uid: String): Result<Boolean>
    fun signOut()
}

sealed class OtpResult {
    data class CodeSent(val verificationId: String, val token: Any?) : OtpResult() // token is ForceResendingToken
    data class Error(val message: String) : OtpResult()
    object AutoVerified : OtpResult() // Instant verification
}

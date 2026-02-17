package com.krishiai.app.ui.auth

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krishiai.app.ui.components.KButton
import com.krishiai.app.ui.components.KTextField

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNewUser: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Krishi AI",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (uiState !is AuthUiState.OtpSent) {
            KTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Phone Number (+91)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(16.dp))
            KButton(
                text = "Get OTP",
                onClick = {
                    val activity = context as? Activity
                    if (activity != null && phoneNumber.isNotEmpty()) {
                        viewModel.sendOtp(phoneNumber, activity)
                    }
                },
                enabled = uiState !is AuthUiState.Loading
            )
        } else {
            KTextField(
                value = otpCode,
                onValueChange = { otpCode = it },
                label = "Enter OTP",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )
            Spacer(modifier = Modifier.height(16.dp))
            KButton(
                text = "Verify OTP",
                onClick = {
                    if (otpCode.isNotEmpty()) {
                        viewModel.verifyOtp(otpCode)
                    }
                },
                enabled = uiState !is AuthUiState.Loading
            )
        }
        
        if (uiState is AuthUiState.Error) {
             Spacer(modifier = Modifier.height(16.dp))
             Text(
                 text = (uiState as AuthUiState.Error).message,
                 color = MaterialTheme.colorScheme.error
             )
        }
        
        // Navigation side-effects
        LaunchedEffect(uiState) {
            when(uiState) {
                is AuthUiState.Verified -> onLoginSuccess()
                is AuthUiState.NewUser -> onNewUser()
                else -> {}
            }
        }
    }
}

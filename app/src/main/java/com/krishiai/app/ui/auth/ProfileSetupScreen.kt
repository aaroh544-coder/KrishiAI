package com.krishiai.app.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krishiai.app.data.model.User
import com.krishiai.app.ui.components.KButton
import com.krishiai.app.ui.components.KTextField

@Composable
fun ProfileSetupScreen(
    onProfileSaved: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("Maharashtra") }
    var crop by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Verified) {
            onProfileSaved()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Complete Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        KTextField(value = name, onValueChange = { name = it }, label = "Full Name")
        KTextField(value = village, onValueChange = { village = it }, label = "Village")
        KTextField(value = district, onValueChange = { district = it }, label = "District")
        KTextField(value = state, onValueChange = { state = it }, label = "State")
        KTextField(value = crop, onValueChange = { crop = it }, label = "Main Crop")
        
        Spacer(modifier = Modifier.height(32.dp))
        
        KButton(
            text = "Save & Continue",
            onClick = {
                val user = User(
                    name = name,
                    village = village,
                    district = district,
                    state = state,
                    mainCrop = crop,
                    preferredLanguage = "en" // Default for now, add dropdown later
                )
                viewModel.saveProfile(user)
            },
            enabled = name.isNotEmpty() && village.isNotEmpty()
        )
        
        if (uiState is AuthUiState.Error) {
             Spacer(modifier = Modifier.height(16.dp))
             Text(
                 text = (uiState as AuthUiState.Error).message,
                 color = MaterialTheme.colorScheme.error
             )
        }
    }
}

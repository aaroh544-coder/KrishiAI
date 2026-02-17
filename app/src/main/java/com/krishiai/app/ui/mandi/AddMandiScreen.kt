package com.krishiai.app.ui.mandi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krishiai.app.data.model.Mandi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMandiScreen(
    onBack: () -> Unit,
    viewModel: MandiViewModel = hiltViewModel()
) {
    var mandiName by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var commodity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Market Price") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = mandiName,
                onValueChange = { mandiName = it },
                label = { Text("Mandi Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = district,
                onValueChange = { district = it },
                label = { Text("District") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = commodity,
                onValueChange = { commodity = it },
                label = { Text("Commodity (e.g., Onion)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price (₹/quintal)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    if (mandiName.isNotBlank() && price.isNotBlank()) {
                         val newMandi = Mandi(
                             id = UUID.randomUUID().toString(),
                             name = mandiName,
                             district = district,
                             state = "Maharashtra", // Hardcoded for now
                             commodity = commodity,
                             price = price.toDoubleOrNull() ?: 0.0,
                             date = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date())
                         )
                         viewModel.addMandi(newMandi)
                         onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Price")
            }
        }
    }
}

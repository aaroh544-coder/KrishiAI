package com.krishiai.app.ui.disease

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DiseaseUiState {
    object Idle : DiseaseUiState()
    object Loading : DiseaseUiState()
    data class Success(val result: String) : DiseaseUiState()
    data class Error(val message: String) : DiseaseUiState()
}

@HiltViewModel
class DiseaseViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<DiseaseUiState>(DiseaseUiState.Idle)
    val uiState: StateFlow<DiseaseUiState> = _uiState

    // API Key is currently hardcoded in Repository, for this feature we will use it here as well
    // Ideally, this should be injected or fetched from a secure repository
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash", // Using flash for vision tasks
        apiKey = "AIzaSyDevhbbhiaSK-Gwq1sEnEi_LuQxdSQX2AU"
    )

    fun analyzeImage(bitmap: Bitmap) {
        _uiState.value = DiseaseUiState.Loading
        
        viewModelScope.launch {
            try {
                val inputContent = content {
                    image(bitmap)
                    text("Analyze this plant leaf for diseases. Identify the plant and the disease if any. Provide a brief description and suggest organic or chemical treatments in English.")
                }
                
                val response = generativeModel.generateContent(inputContent)
                val result = response.text ?: "Could not analyze the image."
                _uiState.value = DiseaseUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = DiseaseUiState.Error(e.message ?: "Analysis failed")
            }
        }
    }

    fun resetState() {
        _uiState.value = DiseaseUiState.Idle
    }
}

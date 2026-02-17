package com.krishiai.app.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishiai.app.data.model.Weather
import com.krishiai.app.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WeatherUiState {
    object Initial : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val weather: Weather) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun fetchWeather() {
        viewModelScope.launch {
            repository.getCurrentLocationWeather()
                .onStart { _uiState.value = WeatherUiState.Loading }
                .catch { e -> _uiState.value = WeatherUiState.Error(e.message ?: "Unknown error") }
                .collect { result ->
                    result.onSuccess { weather ->
                        _uiState.value = WeatherUiState.Success(weather)
                    }.onFailure { e ->
                        _uiState.value = WeatherUiState.Error(e.message ?: "Failed to fetch weather")
                    }
                }
        }
    }
}

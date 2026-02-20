package com.krishiai.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.krishiai.app.data.model.Mandi
import com.krishiai.app.data.model.Weather
import com.krishiai.app.data.repository.MandiRepository
import com.krishiai.app.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val weather: Weather? = null,
    val latestMandi: Mandi? = null,
    val tipOfTheDay: String = "Loading tip...",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val mandiRepository: MandiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyDevhbbhiaSK-Gwq1sEnEi_LuQxdSQX2AU"
    )

    init {
        refreshDashboard()
    }

    fun refreshDashboard() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        
        viewModelScope.launch {
            // Fetch Weather
            weatherRepository.getCurrentLocationWeather()
                .catch { e -> _uiState.value = _uiState.value.copy(error = e.message) }
                .collectLatest { result ->
                    result.onSuccess { weather ->
                        _uiState.value = _uiState.value.copy(weather = weather)
                    }
                }
        }

        viewModelScope.launch {
            // Fetch Latest Mandi
            mandiRepository.getMandiPrices()
                .catch { }
                .collectLatest { mandis ->
                    _uiState.value = _uiState.value.copy(latestMandi = mandis.firstOrNull())
                }
        }

        fetchTip()
    }

    private fun fetchTip() {
        viewModelScope.launch {
            try {
                val response = generativeModel.generateContent("Provide a short, one-sentence agricultural tip for a farmer today. Keep it practical and seasonal for Indian agriculture.")
                _uiState.value = _uiState.value.copy(
                    tipOfTheDay = response.text ?: "Check your soil moisture before irrigation.",
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    tipOfTheDay = "Stay updated with market prices to get the best value for your crops.",
                    isLoading = false
                )
            }
        }
    }
}

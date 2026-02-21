package com.krishiai.app.ui.mandi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishiai.app.data.model.Mandi
import com.krishiai.app.data.model.Review
import com.krishiai.app.data.repository.MandiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MandiUiState {
    object Initial : MandiUiState()
    object Loading : MandiUiState()
    data class Success(val mandis: List<Mandi>) : MandiUiState()
    data class Error(val message: String) : MandiUiState()
}

@HiltViewModel
class MandiViewModel @Inject constructor(
    private val repository: MandiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MandiUiState>(MandiUiState.Initial)
    val uiState: StateFlow<MandiUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    init {
        fetchMandiPrices()
    }

    fun fetchMandiPrices() {
        viewModelScope.launch {
            repository.getMandiPrices()
                .onStart { _uiState.value = MandiUiState.Loading }
                .catch { e -> _uiState.value = MandiUiState.Error(e.message ?: "Unknown error") }
                .collect { mandis ->
                    _uiState.value = MandiUiState.Success(mandis)
                }
        }
    }

    fun fetchReviews(mandiId: String) {
        viewModelScope.launch {
            repository.getReviews(mandiId).collect {
                _reviews.value = it
            }
        }
    }

    fun addReview(review: Review) {
        viewModelScope.launch {
            repository.addReview(review)
            fetchReviews(review.mandiId)
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun addMandi(mandi: Mandi) {
        viewModelScope.launch {
            repository.addMandiPrice(mandi)
            fetchMandiPrices()
        }
    }
}

package com.krishiai.app.ui.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishiai.app.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        _messages.add(ChatMessage(text, true))

        viewModelScope.launch {
            _messages.add(ChatMessage("Typing...", false))
            
            repository.sendMessage(text).collect { response ->
                _messages.removeLast() // Remove "Typing..."
                _messages.add(ChatMessage(response, false))
            }
        }
    }
}

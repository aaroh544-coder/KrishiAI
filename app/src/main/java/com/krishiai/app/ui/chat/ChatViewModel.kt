package com.krishiai.app.ui.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishiai.app.util.SpeechManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val speechManager: SpeechManager
) : ViewModel() {

    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages
    
    val isListening = speechManager.isListening
    val speechResult = speechManager.speechResult

    init {
        viewModelScope.launch {
            speechResult.collectLatest { text ->
                if (text.isNotBlank()) {
                    sendMessage(text, isVoice = true)
                }
            }
        }
    }

    fun startListening() {
        speechManager.startListening()
    }

    fun speak(text: String) {
        speechManager.speak(text)
    }

    fun sendMessage(text: String, isVoice: Boolean = false) {
        if (text.isBlank()) return

        _messages.add(ChatMessage(text, true))

        viewModelScope.launch {
            _messages.add(ChatMessage("Typing...", false))
            
            repository.sendMessage(text).collect { response ->
                _messages.removeLast() // Remove "Typing..."
                _messages.add(ChatMessage(response, false))
                if (isVoice) {
                    speak(response)
                }
            }
        }
    }
}

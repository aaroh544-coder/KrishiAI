package com.krishiai.app.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor() {

    // Initialize with a placeholder or inject via Hilt if configuring globally
    // For now, local instantiation is fine handling the API Key
    // WARNING: In production, API Key should be secured.
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "AIzaSyDevhbbhiaSK-Gwq1sEnEi_LuQxdSQX2AU"
    )

    fun sendMessage(userMessage: String): Flow<String> = flow {
        try {
            val response = generativeModel.generateContent(userMessage)
            emit(response.text ?: "Sorry, I didn't understand that.")
        } catch (e: Exception) {
            emit("Error: ${e.message}")
        }
    }
}

package com.max.pandaai.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.max.pandaai.AIService
import com.max.pandaai.IntentHandler
import com.max.pandaai.database.ChatDao
import com.max.pandaai.model.ChatMessage
import kotlinx.coroutines.launch

/**
 * ChatViewModel - ViewModel for managing chat messages and AI interactions
 * Handles business logic and coordinates between UI, database, and services
 */
class ChatViewModel(
    private val chatDao: ChatDao,
    private val aiService: AIService,
    private val intentHandler: IntentHandler
) : ViewModel() {

    // LiveData for chat messages
    val messages: LiveData<List<ChatMessage>> = chatDao.getAllMessages()

    // Loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Last AI response (for TTS)
    private val _lastAiResponse = MutableLiveData<String?>()
    val lastAiResponse: LiveData<String?> = _lastAiResponse

    /**
     * Add a message to chat history
     */
    fun addMessage(message: ChatMessage) {
        viewModelScope.launch {
            chatDao.insertMessage(message)
        }
    }

    /**
     * Process user message - check for commands, then get AI response
     */
    fun processUserMessage(userMessage: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // First, check if it's a command that IntentHandler can process
            val commandResponse = intentHandler.processCommand(userMessage)

            if (commandResponse.isNotEmpty()) {
                // Command was handled, add response
                val aiMessage = ChatMessage(text = commandResponse, isUser = false)
                chatDao.insertMessage(aiMessage)
                _lastAiResponse.value = commandResponse
            } else {
                // No command found, get AI response
                val aiResponse = aiService.getAIResponse(userMessage)
                val aiMessage = ChatMessage(text = aiResponse, isUser = false)
                chatDao.insertMessage(aiMessage)
                _lastAiResponse.value = aiResponse
            }

            _isLoading.value = false
        }
    }

    /**
     * Clear all messages from chat history
     */
    fun clearAllMessages() {
        viewModelScope.launch {
            chatDao.deleteAllMessages()
        }
    }

    /**
     * Check if there are any messages in history
     */
    suspend fun hasMessages(): Boolean {
        return chatDao.getMessageCount() > 0
    }

    /**
     * Delete a specific message
     */
    fun deleteMessage(message: ChatMessage) {
        viewModelScope.launch {
            chatDao.deleteMessage(message)
        }
    }
}

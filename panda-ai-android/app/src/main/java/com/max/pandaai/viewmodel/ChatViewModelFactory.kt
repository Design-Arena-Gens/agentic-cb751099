package com.max.pandaai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.pandaai.AIService
import com.max.pandaai.IntentHandler
import com.max.pandaai.database.ChatDao

/**
 * ChatViewModelFactory - Factory for creating ChatViewModel with dependencies
 */
class ChatViewModelFactory(
    private val chatDao: ChatDao,
    private val aiService: AIService,
    private val intentHandler: IntentHandler
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(chatDao, aiService, intentHandler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

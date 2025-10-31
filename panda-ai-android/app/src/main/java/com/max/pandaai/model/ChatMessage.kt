package com.max.pandaai.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ChatMessage - Data model for chat messages
 * Stored in Room database for persistent chat history
 */
@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

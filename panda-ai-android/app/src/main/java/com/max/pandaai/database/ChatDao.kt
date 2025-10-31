package com.max.pandaai.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.max.pandaai.model.ChatMessage

/**
 * ChatDao - Data Access Object for chat messages
 * Provides database operations for chat history
 */
@Dao
interface ChatDao {

    /**
     * Get all chat messages, ordered by timestamp
     */
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): LiveData<List<ChatMessage>>

    /**
     * Insert a new message
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    /**
     * Delete all messages (clear chat history)
     */
    @Query("DELETE FROM chat_messages")
    suspend fun deleteAllMessages()

    /**
     * Get message count
     */
    @Query("SELECT COUNT(*) FROM chat_messages")
    suspend fun getMessageCount(): Int

    /**
     * Delete a specific message
     */
    @Delete
    suspend fun deleteMessage(message: ChatMessage)

    /**
     * Get latest N messages
     */
    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getLatestMessages(limit: Int): List<ChatMessage>
}

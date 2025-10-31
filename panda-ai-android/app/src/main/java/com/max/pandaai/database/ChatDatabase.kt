package com.max.pandaai.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.max.pandaai.model.ChatMessage

/**
 * ChatDatabase - Room database for storing chat messages
 * Provides persistent storage for chat history
 */
@Database(entities = [ChatMessage::class], version = 1, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: ChatDatabase? = null

        /**
         * Get database instance (singleton pattern)
         */
        fun getDatabase(context: Context): ChatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatDatabase::class.java,
                    "chat_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

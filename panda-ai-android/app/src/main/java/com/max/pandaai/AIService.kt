package com.max.pandaai

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

/**
 * AIService - Handles communication with AI API (OpenAI GPT)
 * Replace API_KEY with your actual OpenAI API key
 */
class AIService(private val context: Context) {

    companion object {
        private const val TAG = "AIService"

        // IMPORTANT: Replace with your actual OpenAI API key
        // Get your key from: https://platform.openai.com/api-keys
        private const val API_KEY = "sk-YOUR-OPENAI-API-KEY-HERE"

        private const val API_URL = "https://api.openai.com/v1/chat/completions"
        private const val MODEL = "gpt-3.5-turbo" // or "gpt-4" for better responses
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    /**
     * Send message to AI and get response
     * @param userMessage The user's message
     * @return AI's response text
     */
    suspend fun getAIResponse(userMessage: String): String = withContext(Dispatchers.IO) {
        try {
            // Check if API key is configured
            if (API_KEY == "sk-YOUR-OPENAI-API-KEY-HERE") {
                return@withContext "⚠️ Please configure your OpenAI API key in AIService.kt to enable AI responses. " +
                        "Get your key from https://platform.openai.com/api-keys"
            }

            // Build request payload
            val requestBody = buildRequestBody(userMessage)
            val jsonBody = gson.toJson(requestBody)

            val request = Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer $API_KEY")
                .addHeader("Content-Type", "application/json")
                .post(jsonBody.toRequestBody("application/json".toMediaType()))
                .build()

            // Execute request
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful) {
                Log.e(TAG, "API Error: ${response.code} - $responseBody")
                return@withContext "Sorry, I'm having trouble connecting to my AI brain. Error: ${response.code}"
            }

            // Parse response
            val aiResponse = gson.fromJson(responseBody, OpenAIResponse::class.java)
            val message = aiResponse.choices.firstOrNull()?.message?.content
                ?: "I didn't quite understand that. Could you try again?"

            return@withContext message.trim()

        } catch (e: Exception) {
            Log.e(TAG, "Error getting AI response", e)
            return@withContext "Oops! I encountered an error: ${e.message}. Please check your internet connection."
        }
    }

    /**
     * Build request body for OpenAI API
     */
    private fun buildRequestBody(userMessage: String): OpenAIRequest {
        val systemPrompt = """
            You are Panda AI by Max, a friendly and helpful voice assistant.
            Your personality is warm, cheerful, and supportive.
            You help users with tasks like opening apps, searching the web, making calls, and answering questions.
            Keep responses concise and natural, as they will be spoken aloud.
            When users ask you to do something, acknowledge it warmly and confirm the action.
            Examples of responses:
            - "Opening YouTube for you!"
            - "Sure! Let me search that for you."
            - "I'd be happy to help with that!"
        """.trimIndent()

        return OpenAIRequest(
            model = MODEL,
            messages = listOf(
                Message(role = "system", content = systemPrompt),
                Message(role = "user", content = userMessage)
            ),
            temperature = 0.7,
            maxTokens = 150
        )
    }

    /**
     * Alternative: Use Google Gemini API (Uncomment to use)
     */
    /*
    suspend fun getGeminiResponse(userMessage: String): String = withContext(Dispatchers.IO) {
        try {
            val apiKey = "YOUR-GEMINI-API-KEY-HERE"
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=$apiKey"

            // Build Gemini request
            val requestJson = """
                {
                  "contents": [{
                    "parts":[{"text": "$userMessage"}]
                  }]
                }
            """.trimIndent()

            val request = Request.Builder()
                .url(url)
                .post(requestJson.toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            // Parse and return response

        } catch (e: Exception) {
            return@withContext "Error: ${e.message}"
        }
    }
    */

    // Data classes for OpenAI API
    data class OpenAIRequest(
        val model: String,
        val messages: List<Message>,
        val temperature: Double = 0.7,
        @SerializedName("max_tokens") val maxTokens: Int = 150
    )

    data class Message(
        val role: String,
        val content: String
    )

    data class OpenAIResponse(
        val choices: List<Choice>
    )

    data class Choice(
        val message: Message
    )
}

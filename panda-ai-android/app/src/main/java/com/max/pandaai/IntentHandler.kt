package com.max.pandaai

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.*

/**
 * IntentHandler - Processes user commands and opens apps/performs actions
 * Handles voice commands like "open YouTube", "call mom", "search Google", etc.
 */
class IntentHandler(private val context: Context) {

    /**
     * Process user command and determine action
     * @param command User's voice/text command
     * @return Response message to user
     */
    fun processCommand(command: String): String {
        val lowerCommand = command.lowercase(Locale.getDefault())

        return when {
            // Open apps
            lowerCommand.contains("open youtube") -> openApp("com.google.android.youtube", "YouTube")
            lowerCommand.contains("open whatsapp") -> openApp("com.whatsapp", "WhatsApp")
            lowerCommand.contains("open instagram") -> openApp("com.instagram.android", "Instagram")
            lowerCommand.contains("open facebook") -> openApp("com.facebook.katana", "Facebook")
            lowerCommand.contains("open twitter") || lowerCommand.contains("open x") ->
                openApp("com.twitter.android", "Twitter")
            lowerCommand.contains("open gmail") -> openApp("com.google.android.gm", "Gmail")
            lowerCommand.contains("open spotify") -> openApp("com.spotify.music", "Spotify")
            lowerCommand.contains("open netflix") -> openApp("com.netflix.mediaclient", "Netflix")
            lowerCommand.contains("open maps") -> openApp("com.google.android.apps.maps", "Google Maps")
            lowerCommand.contains("open settings") -> openSettings()

            // Google search
            lowerCommand.contains("search") || lowerCommand.contains("google") -> {
                val query = extractSearchQuery(lowerCommand)
                searchGoogle(query)
            }

            // YouTube search
            lowerCommand.contains("play") && (lowerCommand.contains("youtube") ||
                lowerCommand.contains("video")) -> {
                val query = extractVideoQuery(lowerCommand)
                searchYouTube(query)
            }

            // Phone call
            lowerCommand.contains("call") || lowerCommand.contains("phone") -> {
                val contact = extractContactName(lowerCommand)
                makePhoneCall(contact)
            }

            // Send SMS
            lowerCommand.contains("send message") || lowerCommand.contains("send sms") ||
                lowerCommand.contains("text") -> {
                val contact = extractContactName(lowerCommand)
                sendSMS(contact)
            }

            // Open camera
            lowerCommand.contains("camera") || lowerCommand.contains("take photo") ||
                lowerCommand.contains("take picture") -> openCamera()

            // Play music
            lowerCommand.contains("play music") || lowerCommand.contains("open music") ->
                openMusicPlayer()

            // Set alarm
            lowerCommand.contains("set alarm") || lowerCommand.contains("wake me up") -> {
                val time = extractTime(lowerCommand)
                setAlarm(time)
            }

            // Create calendar event
            lowerCommand.contains("create event") || lowerCommand.contains("add calendar") ||
                lowerCommand.contains("schedule") -> createCalendarEvent()

            // Show time
            lowerCommand.contains("what time") || lowerCommand.contains("current time") ->
                getCurrentTime()

            // Show date
            lowerCommand.contains("what date") || lowerCommand.contains("today's date") ->
                getCurrentDate()

            // Open browser
            lowerCommand.contains("open browser") || lowerCommand.contains("open chrome") ->
                openBrowser()

            // No recognized command
            else -> ""
        }
    }

    /**
     * Open app by package name
     */
    private fun openApp(packageName: String, appName: String): String {
        return try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                "Opening $appName! ✨"
            } else {
                "$appName is not installed on your device. Would you like to install it from Play Store?"
            }
        } catch (e: Exception) {
            "Sorry, I couldn't open $appName. ${e.message}"
        }
    }

    /**
     * Open device settings
     */
    private fun openSettings(): String {
        return try {
            val intent = Intent(android.provider.Settings.ACTION_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            "Opening Settings! ⚙️"
        } catch (e: Exception) {
            "Couldn't open settings."
        }
    }

    /**
     * Search Google with query
     */
    private fun searchGoogle(query: String): String {
        return try {
            val searchUrl = "https://www.google.com/search?q=${Uri.encode(query)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            "Searching Google for '$query'! 🔍"
        } catch (e: Exception) {
            "Couldn't perform Google search."
        }
    }

    /**
     * Search YouTube
     */
    private fun searchYouTube(query: String): String {
        return try {
            val searchUrl = "https://www.youtube.com/results?search_query=${Uri.encode(query)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            "Searching YouTube for '$query'! 🎥"
        } catch (e: Exception) {
            "Couldn't search YouTube."
        }
    }

    /**
     * Make phone call
     */
    private fun makePhoneCall(contact: String): String {
        return if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED) {
            try {
                // Open dialer with contact name (user needs to select from contacts)
                val intent = Intent(Intent.ACTION_DIAL)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                "Opening dialer to call $contact! 📞"
            } catch (e: Exception) {
                "Couldn't open dialer."
            }
        } else {
            "I need phone permission to make calls. Please grant permission in settings."
        }
    }

    /**
     * Send SMS
     */
    private fun sendSMS(contact: String): String {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            "Opening SMS to send message to $contact! 💬"
        } catch (e: Exception) {
            "Couldn't open SMS app."
        }
    }

    /**
     * Open camera
     */
    private fun openCamera(): String {
        return try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            "Opening camera! 📸"
        } catch (e: Exception) {
            "Couldn't open camera."
        }
    }

    /**
     * Open music player
     */
    private fun openMusicPlayer(): String {
        return try {
            val intent = Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            "Opening music player! 🎵"
        } catch (e: Exception) {
            // Try alternative
            openApp("com.google.android.music", "Music")
        }
    }

    /**
     * Set alarm
     */
    private fun setAlarm(time: String): String {
        return try {
            val intent = Intent(AlarmClock.ACTION_SET_ALARM)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            "Opening alarm settings! ⏰"
        } catch (e: Exception) {
            "Couldn't set alarm."
        }
    }

    /**
     * Create calendar event
     */
    private fun createCalendarEvent(): String {
        return try {
            val intent = Intent(Intent.ACTION_INSERT)
            intent.data = CalendarContract.Events.CONTENT_URI
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            "Opening calendar to create event! 📅"
        } catch (e: Exception) {
            "Couldn't open calendar."
        }
    }

    /**
     * Get current time
     */
    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
        return "The current time is $hour:${String.format("%02d", minute)} $amPm ⏰"
    }

    /**
     * Get current date
     */
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val year = calendar.get(Calendar.YEAR)
        val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        return "Today is $dayOfWeek, $month $day, $year 📆"
    }

    /**
     * Open browser
     */
    private fun openBrowser(): String {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            "Opening browser! 🌐"
        } catch (e: Exception) {
            "Couldn't open browser."
        }
    }

    // Helper methods to extract information from commands

    private fun extractSearchQuery(command: String): String {
        val keywords = listOf("search for", "search", "google", "find")
        for (keyword in keywords) {
            val index = command.indexOf(keyword, ignoreCase = true)
            if (index != -1) {
                return command.substring(index + keyword.length).trim()
            }
        }
        return command
    }

    private fun extractVideoQuery(command: String): String {
        val keywords = listOf("play", "video", "youtube")
        for (keyword in keywords) {
            val index = command.indexOf(keyword, ignoreCase = true)
            if (index != -1) {
                return command.substring(index + keyword.length).trim()
            }
        }
        return command
    }

    private fun extractContactName(command: String): String {
        val keywords = listOf("call", "phone", "message", "text", "sms")
        for (keyword in keywords) {
            val index = command.indexOf(keyword, ignoreCase = true)
            if (index != -1) {
                return command.substring(index + keyword.length).trim()
            }
        }
        return "contact"
    }

    private fun extractTime(command: String): String {
        // Simple extraction - can be enhanced with regex
        val words = command.split(" ")
        for (i in words.indices) {
            if (words[i].contains(":") || words[i].matches(Regex("\\d+"))) {
                return words[i]
            }
        }
        return "time"
    }
}

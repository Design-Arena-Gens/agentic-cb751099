package com.max.pandaai

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.max.pandaai.adapter.ChatAdapter
import com.max.pandaai.database.ChatDatabase
import com.max.pandaai.databinding.ActivityMainBinding
import com.max.pandaai.model.ChatMessage
import com.max.pandaai.viewmodel.ChatViewModel
import com.max.pandaai.viewmodel.ChatViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

/**
 * MainActivity - Main screen of Panda AI voice assistant
 * Handles voice input, displays chat messages, and manages AI interactions
 */
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatViewModel: ChatViewModel
    private var textToSpeech: TextToSpeech? = null
    private var isTtsReady = false
    private var isListening = false

    // Voice recognition launcher
    private val speechRecognizerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            spokenText?.let {
                if (it.isNotEmpty()) {
                    handleVoiceInput(it[0])
                }
            }
        }
        stopListeningAnimation()
    }

    // Audio permission launcher
    private val audioPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startVoiceRecognition()
        } else {
            showPermissionRationale(
                "Microphone Access Required",
                "Panda AI needs microphone access to listen to your voice commands. Please grant permission in settings."
            )
        }
    }

    // Phone call permission launcher
    private val phonePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            showPermissionRationale(
                "Phone Access Required",
                "Phone permission is needed to make calls on your behalf."
            )
        }
    }

    // SMS permission launcher
    private val smsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            showPermissionRationale(
                "SMS Access Required",
                "SMS permission is needed to send text messages on your behalf."
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "🐼 Panda AI by Max"

        // Initialize TextToSpeech
        textToSpeech = TextToSpeech(this, this)

        // Setup RecyclerView for chat messages
        setupRecyclerView()

        // Initialize ViewModel
        val database = ChatDatabase.getDatabase(applicationContext)
        val factory = ChatViewModelFactory(database.chatDao(), AIService(this), IntentHandler(this))
        chatViewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]

        // Observe chat messages
        chatViewModel.messages.observe(this) { messages ->
            chatAdapter.submitList(messages)
            if (messages.isNotEmpty()) {
                binding.recyclerView.smoothScrollToPosition(messages.size - 1)
            }
        }

        // Observe AI responses to speak them
        chatViewModel.lastAiResponse.observe(this) { response ->
            response?.let {
                speakText(it)
            }
        }

        // Observe loading state
        chatViewModel.isLoading.observe(this) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Setup microphone button
        binding.micButton.setOnClickListener {
            checkAudioPermissionAndStartListening()
        }

        // Setup send button
        binding.sendButton.setOnClickListener {
            val text = binding.messageInput.text.toString().trim()
            if (text.isNotEmpty()) {
                handleTextInput(text)
                binding.messageInput.text?.clear()
            }
        }

        // Show welcome message
        showWelcomeMessage()
    }

    /**
     * Setup RecyclerView with chat adapter
     */
    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = chatAdapter
        }
    }

    /**
     * Show welcome message when app starts
     */
    private fun showWelcomeMessage() {
        lifecycleScope.launch {
            val hasMessages = chatViewModel.hasMessages()
            if (!hasMessages) {
                val welcomeText = "Hello! I'm Panda AI by Max, your personal voice assistant. " +
                        "Tap the microphone to speak, or type your message below. " +
                        "I can help you open apps, search the web, make calls, and much more!"
                chatViewModel.addMessage(ChatMessage(text = welcomeText, isUser = false))
                speakText(welcomeText)
            }
        }
    }

    /**
     * Check audio permission and start voice recognition
     */
    private fun checkAudioPermissionAndStartListening() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startVoiceRecognition()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                showPermissionRationale(
                    "Microphone Permission",
                    "Panda AI needs microphone access to understand your voice commands."
                ) {
                    audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
            else -> {
                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    /**
     * Start voice recognition using Android's speech recognizer
     */
    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "🐼 Speak now...")
        }

        try {
            startListeningAnimation()
            speechRecognizerLauncher.launch(intent)
        } catch (e: Exception) {
            stopListeningAnimation()
            Toast.makeText(this, "Speech recognition not available", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Start listening animation - pulsating mic button
     */
    private fun startListeningAnimation() {
        isListening = true
        binding.micButton.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(300)
            .start()
        binding.listeningIndicator.visibility = View.VISIBLE
    }

    /**
     * Stop listening animation
     */
    private fun stopListeningAnimation() {
        isListening = false
        binding.micButton.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setDuration(300)
            .start()
        binding.listeningIndicator.visibility = View.GONE
    }

    /**
     * Handle voice input from speech recognizer
     */
    private fun handleVoiceInput(text: String) {
        // Add user message to chat
        chatViewModel.addMessage(ChatMessage(text = text, isUser = true))

        // Process the message
        chatViewModel.processUserMessage(text)
    }

    /**
     * Handle text input from keyboard
     */
    private fun handleTextInput(text: String) {
        // Add user message to chat
        chatViewModel.addMessage(ChatMessage(text = text, isUser = true))

        // Process the message
        chatViewModel.processUserMessage(text)
    }

    /**
     * TextToSpeech initialization callback
     */
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech?.setLanguage(Locale.US)
            isTtsReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED

            // Set TTS properties
            textToSpeech?.setPitch(1.0f)
            textToSpeech?.setSpeechRate(1.0f)
        }
    }

    /**
     * Speak the given text using TextToSpeech
     */
    private fun speakText(text: String) {
        if (isTtsReady && textToSpeech != null) {
            // Check if TTS is enabled in settings
            val prefs = getSharedPreferences("PandaAI_Settings", MODE_PRIVATE)
            val isTtsEnabled = prefs.getBoolean("tts_enabled", true)

            if (isTtsEnabled) {
                textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    /**
     * Show permission rationale dialog
     */
    private fun showPermissionRationale(title: String, message: String, onPositive: (() -> Unit)? = null) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                onPositive?.invoke()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Create options menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * Handle menu item selection
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_clear_chat -> {
                showClearChatDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Show dialog to confirm clearing chat history
     */
    private fun showClearChatDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Clear Chat History")
            .setMessage("Are you sure you want to clear all chat messages? This action cannot be undone.")
            .setPositiveButton("Clear") { _, _ ->
                chatViewModel.clearAllMessages()
                Toast.makeText(this, "Chat history cleared", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Clean up resources when activity is destroyed
     */
    override fun onDestroy() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDestroy()
    }
}

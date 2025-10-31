package com.max.pandaai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.max.pandaai.databinding.ActivitySettingsBinding

/**
 * SettingsActivity - Manages app settings and preferences
 * Allows users to customize their Panda AI experience
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"

        // Initialize SharedPreferences
        prefs = getSharedPreferences("PandaAI_Settings", MODE_PRIVATE)

        // Load current settings
        loadSettings()

        // Setup listeners
        setupListeners()
    }

    /**
     * Load current settings from SharedPreferences
     */
    private fun loadSettings() {
        // TTS enabled
        val ttsEnabled = prefs.getBoolean("tts_enabled", true)
        binding.switchTts.isChecked = ttsEnabled

        // Dark mode
        val darkMode = prefs.getInt("dark_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        when (darkMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> binding.radioButtonDark.isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> binding.radioButtonLight.isChecked = true
            else -> binding.radioButtonSystem.isChecked = true
        }

        // Assistant name
        val assistantName = prefs.getString("assistant_name", "Panda AI")
        binding.editAssistantName.setText(assistantName)
    }

    /**
     * Setup click listeners for settings
     */
    private fun setupListeners() {
        // TTS toggle
        binding.switchTts.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("tts_enabled", isChecked).apply()
        }

        // Dark mode selection
        binding.radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                R.id.radioButtonDark -> AppCompatDelegate.MODE_NIGHT_YES
                R.id.radioButtonLight -> AppCompatDelegate.MODE_NIGHT_NO
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            prefs.edit().putInt("dark_mode", mode).apply()
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        // Save assistant name
        binding.buttonSaveName.setOnClickListener {
            val name = binding.editAssistantName.text.toString()
            if (name.isNotBlank()) {
                prefs.edit().putString("assistant_name", name).apply()
                finish()
            }
        }

        // Privacy Policy
        binding.buttonPrivacyPolicy.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }

        // About section
        binding.buttonAbout.setOnClickListener {
            showAboutDialog()
        }
    }

    /**
     * Show About dialog
     */
    private fun showAboutDialog() {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle("About Panda AI")
            .setMessage("""
                🐼 Panda AI by Max
                Version 1.0

                A personal voice assistant that helps you with daily tasks,
                opens apps, searches the web, and much more!

                Made with ❤️ by Max

                © 2024 All rights reserved
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

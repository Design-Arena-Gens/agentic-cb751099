package com.max.pandaai

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.max.pandaai.databinding.ActivityPrivacyPolicyBinding

/**
 * PrivacyPolicyActivity - Displays the privacy policy
 * Complies with Google Play Store requirements
 */
class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Privacy Policy"

        // Load privacy policy text
        loadPrivacyPolicy()
    }

    /**
     * Load and display privacy policy
     */
    private fun loadPrivacyPolicy() {
        val privacyText = """
            PRIVACY POLICY
            Last updated: ${getCurrentDate()}

            Welcome to Panda AI by Max!

            1. INTRODUCTION
            Panda AI respects your privacy. This policy explains how we handle your data.

            2. DATA COLLECTION
            Panda AI does NOT collect, store, or transmit any personal data to external servers, except:
            - Voice commands are processed locally on your device
            - Text sent to AI API (OpenAI) is transmitted securely for processing
            - No voice recordings are stored permanently
            - Chat history is stored locally on your device only

            3. PERMISSIONS
            Panda AI requires the following permissions:

            • RECORD_AUDIO: To listen to your voice commands
            • INTERNET: To communicate with AI services
            • CALL_PHONE: To make phone calls (only when you request)
            • SEND_SMS: To send text messages (only when you request)
            • CALENDAR: To create events (only when you request)

            All permissions are requested only when needed and require your explicit consent.

            4. THIRD-PARTY SERVICES
            Panda AI uses OpenAI's API to process your messages. Please review OpenAI's privacy policy at:
            https://openai.com/privacy

            5. DATA STORAGE
            - All chat messages are stored locally on your device
            - No data is uploaded to our servers
            - You can clear chat history anytime from the app menu

            6. USER CONTROL
            You have full control over:
            - What commands you give to the assistant
            - When to grant or revoke permissions
            - Clearing your chat history
            - Uninstalling the app (removes all local data)

            7. SECURITY
            We implement industry-standard security practices to protect your data.

            8. CHILDREN'S PRIVACY
            Panda AI is not intended for children under 13. We do not knowingly collect data from children.

            9. CHANGES TO POLICY
            We may update this policy. Changes will be posted in the app.

            10. CONTACT
            For questions about privacy, contact: pandaai.support@example.com

            11. NO BACKGROUND SPYING
            Panda AI NEVER:
            - Records audio without your explicit action
            - Runs in the background without your knowledge
            - Shares your data with third parties (except AI API)
            - Tracks your location or browsing

            By using Panda AI, you agree to this privacy policy.

            © 2024 Panda AI by Max. All rights reserved.
        """.trimIndent()

        binding.privacyPolicyText.text = privacyText
    }

    private fun getCurrentDate(): String {
        return java.text.SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.US)
            .format(java.util.Date())
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

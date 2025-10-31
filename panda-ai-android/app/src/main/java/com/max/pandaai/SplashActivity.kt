package com.max.pandaai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.max.pandaai.databinding.ActivitySplashBinding

/**
 * SplashActivity - Shows a beautiful splash screen with Panda AI logo
 * Displays for 2.5 seconds before launching MainActivity
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start animations
        startAnimations()

        // Navigate to MainActivity after delay
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2500) // 2.5 seconds
    }

    /**
     * Start splash screen animations
     */
    private fun startAnimations() {
        // Fade in animation for logo
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.logoText.startAnimation(fadeIn)

        // Scale animation for panda emoji
        val scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_bounce)
        binding.pandaEmoji.startAnimation(scaleAnim)

        // Fade in for tagline
        binding.tagline.alpha = 0f
        binding.tagline.animate()
            .alpha(1f)
            .setDuration(1000)
            .setStartDelay(500)
            .start()
    }
}

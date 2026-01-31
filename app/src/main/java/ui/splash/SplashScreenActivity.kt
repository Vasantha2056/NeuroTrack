package com.example.neurotrack.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.neurotrack.ui.onboarding.OnboardingActivity // Target: Onboarding

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }

        Handler(Looper.getMainLooper()).postDelayed({

            splashScreen.setKeepOnScreenCondition { false }

            // Navigate to Onboarding Activity
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()

        }, 1000) // 1 second delay
    }
}
package com.example.neurotrack.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neurotrack.databinding.ActivityHomeBinding
import com.example.neurotrack.ui.analysis.AnalysisActivity
import com.example.neurotrack.ui.screentime.ScreenTimeActivity
import com.example.neurotrack.ui.auth.LoginActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Navigate to Typing Analysis
        binding.btnTypingAnalysis.setOnClickListener {
            val intent = Intent(this, AnalysisActivity::class.java)
            startActivity(intent)
        }

        // 2. Navigate to Screen Time Analysis
        binding.btnScreenTimeAnalysis.setOnClickListener {
            val intent = Intent(this, ScreenTimeActivity::class.java)
            startActivity(intent)
        }

        // 3. Logout
        binding.btnLogout.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
package com.example.neurotrack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neurotrack.databinding.ActivityDashboardBinding

import com.example.neurotrack.ui.auth.LoginActivity
import com.example.neurotrack.ui.home.MainActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the Logout button listener
        binding.btnLogout.setOnClickListener {
            handleLogout()
        }

        // Setup listener for the Typing Feature button
        binding.btnStartTyping.setOnClickListener {
            // TODO: Navigate to the Typing Analysis Screen (MainActivity or a new activity)
            // For now, let's navigate to MainActivity if that's where the core feature is.
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Setup listener for Screen Analysis button
        binding.btnScreenAnalysis.setOnClickListener {
            // TODO: Implement logic to show screen analysis data or settings
            // Example:
            // Toast.makeText(this, "Opening Screen Analysis...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLogout() {
        // 1. Clear any login session data (if using SharedPreferences beyond the demo)
        // If you want to force re-login:
        // val sharedPref = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
        // sharedPref.edit().clear().apply()

        // 2. Navigate back to the Login Screen
        val intent = Intent(this, LoginActivity::class.java)

        // Flags to clear the back stack so the user cannot press Back to return to Dashboard
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish() // Close DashboardActivity
    }
}
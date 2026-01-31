package com.example.neurotrack.ui.auth // Assuming the file is within the 'ui.auth' package

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neurotrack.databinding.ActivityLoginBinding
import com.example.neurotrack.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FIX: btnLogIn listener set here (Line 23:17 where the error occurs)
        binding.btnLogIn.setOnClickListener {
            // Placeholder: Perform authentication check here

            // Assume successful login leads to the Home screen
            navigateToHome()
        }

        // Navigate to Sign Up from the prompt link
        binding.tvSignUpPrompt.setOnClickListener {
            navigateToSignUp()
        }
    }

    /**
     * Navigates to the SignUpActivity.
     */
    private fun navigateToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish() // Close Login so the user lands on Sign Up
    }

    /**
     * Navigates to the HomeActivity and clears the back stack.
     */
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        // Clear activity stack so pressing back doesn't return to login/signup
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
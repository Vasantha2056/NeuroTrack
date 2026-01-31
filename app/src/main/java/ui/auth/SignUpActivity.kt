package com.example.neurotrack.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neurotrack.databinding.ActivitySignUpBinding
import com.example.neurotrack.ui.home.HomeActivity // FIX: HomeActivity Import

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigate to Login from the prompt link
        // Assumes ID: tvLoginPrompt exists in activity_sign_up.xml
        binding.tvLoginPrompt.setOnClickListener {
            navigateToLogin()
        }

        // SignUp button logic (successful signup leads to Home)
        binding.btnSignUp.setOnClickListener {
            // Placeholder: Perform authentication check here
            navigateToHome()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
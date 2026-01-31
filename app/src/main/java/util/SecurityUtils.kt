package com.example.neurotrack.utils

object SecurityUtils {

    // Simple email format validation (useful for LoginActivity)
    fun isValidEmail(email: String): Boolean {
        // Using Android's built-in email pattern checker
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Dummy password hashing function - used for clarity
    fun hashPassword(password: String): String {
        // Since we are skipping Room, we skip real hashing too.
        // This is a dummy function placeholder for future use.
        return "mock_hash_${password.length}"
    }
}
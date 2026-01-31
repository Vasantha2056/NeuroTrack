package com.example.neurotrack.data.repository

import android.content.Context


// Key for the login status
const val PREF_KEY_LOGGED_IN = "is_logged_in"
const val PREF_FILE_NAME = "user_prefs"

class UserRepository(context: Context) {

    private val sharedPrefs = context.getSharedPreferences(
        PREF_FILE_NAME,
        Context.MODE_PRIVATE
    )

    // 💡 NOTE: We are skipping real encryption for simplicity, but if you want
    // to use AndroidX Security (which causes red lines if not set up),
    // you would use EncryptedSharedPreferences instead.

    // For now, we use simple SharedPreferences. If you need security,
    // we must re-add AndroidX Security library and update this.

    fun isLoggedIn(): Boolean {
        // Checks if the 'is_logged_in' flag is true in local storage
        return sharedPrefs.getBoolean(PREF_KEY_LOGGED_IN, false)
    }

    // In a real app, this would be called after successful API login.
    fun setLoggedIn(status: Boolean) {
        sharedPrefs.edit().putBoolean(PREF_KEY_LOGGED_IN, status).apply()
    }

    // Simulates "registering" a user. Since there's no database, we just log in.
    fun registerAndLogin(email: String) {
        // You can save the email here if needed:
        // sharedPrefs.edit().putString("user_email", email).apply()
        setLoggedIn(true)
    }

    fun logout() {
        setLoggedIn(false)
    }
}
package com.example.neurotrack.ui.login

import androidx.lifecycle.ViewModel
import com.example.neurotrack.data.repository.UserRepository
import com.example.neurotrack.utils.SecurityUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Tracks if the authentication was successful
    private val _loginStatus = MutableStateFlow<Boolean?>(null)
    val loginStatus: StateFlow<Boolean?> = _loginStatus

    // Simple function to simulate login
    fun authenticate(email: String, password: String) {
        if (!SecurityUtils.isValidEmail(email)) {
            _loginStatus.value = false // Invalid email format
            return
        }

        // Since we have no backend/database:
        // We consider any valid input as a successful "registration/login".
        userRepository.registerAndLogin(email)
        _loginStatus.value = true
    }

    fun isUserLoggedIn(): Boolean {
        return userRepository.isLoggedIn()
    }
}
package com.example.neurotrack.ui.analysis

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.neurotrack.R
import com.example.neurotrack.databinding.ActivityAnalysisBinding
// 👇 Import the new ScreenTimeActivity
import com.example.neurotrack.ui.screentime.ScreenTimeActivity

class AnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalysisBinding

    // Typing Metrics
    private var startTime: Long = 0
    private var totalKeystrokes: Int = 0
    private var backspaceCount: Int = 0
    private var errorCount: Int = 0
    private var totalWords: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvAnalysisResult.text = "Start typing and press ANALYZE."
        setupTypingListener()

        // Analyze Button
        binding.btnAnalyze.setOnClickListener {
            analyzeTyping()
        }

        // 👇 UPDATED LISTENER: Navigating to ScreenTime instead of Home 👇
        binding.btnBackToHome.setOnClickListener {
            navigateToScreenTime()
        }
    }

    private fun setupTypingListener() {
        // Track backspace and total keystrokes
        binding.etTypingBox.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (startTime == 0L) {
                    startTime = System.currentTimeMillis() // Start timing on first key press
                }

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    backspaceCount++
                    errorCount++ // Treat backspace usage as an error
                } else {
                    totalKeystrokes++
                }
            }
            false
        })

        // Track changes to calculate words
        binding.etTypingBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                // Simple word count: based on spaces
                totalWords = s.toString().trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
            }
        })
    }

    private fun analyzeTyping() {
        // --- (Analysis Logic is the same) ---
        if (startTime == 0L) {
            Toast.makeText(this, "Please start typing before analyzing.", Toast.LENGTH_SHORT).show()
            return
        }

        val typedText = binding.etTypingBox.text.toString()
        val totalTimeSeconds = (System.currentTimeMillis() - startTime) / 1000.0

        if (totalTimeSeconds < 5.0) {
            Toast.makeText(this, "Type for at least 5 seconds for accurate analysis.", Toast.LENGTH_SHORT).show()
            return
        }

        // --- 1. Calculate Metrics ---
        val wpm = if (totalTimeSeconds > 0) (typedText.length / 5) / (totalTimeSeconds / 60.0) else 0.0
        val errorRate = if (totalKeystrokes > 0) (errorCount.toDouble() / totalKeystrokes) * 100 else 0.0
        val backspacePerWord = if (totalWords > 0) backspaceCount.toDouble() / totalWords else 0.0

        startTime = 0L // Reset timer

        // --- 2. Run Analysis Logic (Thresholds) ---
        var resultTitle = "Analysis Report 📊"
        var resultMessage = "WPM: ${"%.1f".format(wpm)}, Errors: ${"%.1f".format(errorRate)}%, Backspace/Word: ${"%.1f".format(backspacePerWord)}\n\n"
        var alertColor = ContextCompat.getColor(this, R.color.status_normal)
        var hasAlert = false

        // Issue 2: Mental Alert - Excessive Backspaces (> 1.5)
        if (backspacePerWord > 1.5) {
            resultTitle = "Mental Alert 🧠"
            resultMessage += "Excessive corrections detected. Your focus and cognitive load seem high. Take a break."
            alertColor = ContextCompat.getColor(this, R.color.alert_mental_red)
            hasAlert = true
        }

        // Issue 1: Physical Alert - Motor Fatigue (> 5% Error Rate)
        if (errorRate > 5.0) {
            if (!hasAlert) {
                resultTitle = "Physical Alert ✋"
                resultMessage += "High error rate detected. Your motor control may be fatigued. Take a rest."
                alertColor = ContextCompat.getColor(this, R.color.alert_physical_orange)
                hasAlert = true
            } else {
                resultMessage += "\n\nAlso noted: High error rate (Physical Fatigue)."
            }
        }

        // Issue 3: Mental Alert - High Stress/Arousal (> 80 WPM AND > 4% Error)
        if (wpm > 80.0 && errorRate > 4.0) {
            if (!hasAlert) {
                resultTitle = "High Stress Alert ⚠️"
                resultMessage += "Typing too fast with many errors. This pattern suggests stress or high tension."
                alertColor = ContextCompat.getColor(this, R.color.alert_mental_red)
                hasAlert = true
            } else {
                resultMessage += "\n\nAlso noted: Very high WPM (Stress)."
            }
        }

        // Issue 4: General Alert - Low Performance (< 20 WPM)
        if (wpm < 20.0 && !hasAlert) {
            resultTitle = "General Alert 🐌"
            resultMessage += "Low typing speed detected. Are you feeling low energy or distracted? Try to re-engage."
            alertColor = ContextCompat.getColor(this, R.color.alert_general_yellow)
            hasAlert = true
        }

        if (!hasAlert) {
            resultMessage = "Analysis complete. Metrics look stable. Keep up the good work."
        }

        // --- 3. Display Result ---
        displayAlert(resultTitle, resultMessage, alertColor)
    }

    private fun displayAlert(title: String, message: String, color: Int) {
        // ... (displayAlert logic is the same) ...
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etTypingBox.windowToken, 0)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            binding.etTypingBox.setText("")
            resetMetrics()
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(color)
    }

    /**
     * Navigates the user to the ScreenTime analysis screen.
     */
    private fun navigateToScreenTime() {
        val intent = Intent(this, ScreenTimeActivity::class.java)
        // We do NOT use finish() here so the user can go back to the Analysis screen if needed.
        startActivity(intent)
    }

    private fun resetMetrics() {
        totalKeystrokes = 0
        backspaceCount = 0
        errorCount = 0
        totalWords = 0
        binding.tvAnalysisResult.text = "Start typing and press ANALYZE."
    }
}
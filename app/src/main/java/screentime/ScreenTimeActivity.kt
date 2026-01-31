package com.example.neurotrack.ui.screentime

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.neurotrack.R
import com.example.neurotrack.databinding.ActivityScreenTimeBinding
import com.example.neurotrack.ui.home.HomeActivity
import java.util.Calendar

class ScreenTimeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScreenTimeBinding
    private val USAGE_ACCESS_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Grant Permission button listener
        binding.btnGrantPermission.setOnClickListener {
            openUsageAccessSettings()
        }

        // Back to Home Page Button listener
        binding.btnBackToHome.setOnClickListener {
            navigateToHome()
        }
    }

    override fun onResume() {
        super.onResume()
        // Every time the user comes back to this screen, check permission and run analysis
        checkForPermissionAndAnalyze()
    }

    private fun checkForPermissionAndAnalyze() {
        if (isUsageAccessGranted()) {
            binding.btnGrantPermission.visibility = View.GONE
            binding.tvScreenTimeAlert.visibility = View.VISIBLE
            binding.btnBackToHome.visibility = View.VISIBLE
            // 👇 Connects to phone settings and analyzes data 👇
            analyzeScreenTime()
        } else {
            binding.tvUsageResult.text = "Permission required to analyze screen time. Click Grant Access."
            binding.tvScreenTimeAlert.visibility = View.GONE
            binding.btnBackToHome.visibility = View.GONE
            binding.btnGrantPermission.visibility = View.VISIBLE
        }
    }

    /**
     * Checks if the app has 'Usage Access' permission from the Android OS.
     * This is the official way to access screen time data.
     */
    private fun isUsageAccessGranted(): Boolean {
        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * Opens the Android system settings page for the user to grant Usage Access.
     */
    private fun openUsageAccessSettings() {
        try {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivityForResult(intent, USAGE_ACCESS_REQUEST_CODE)
        } catch (e: Exception) {
            Toast.makeText(this, "Could not open Usage Access settings.", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Connects to UsageStatsManager to retrieve and process daily screen time.
     */
    private fun analyzeScreenTime() {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()

        // Define Today's start and end time (from midnight to now)
        val endTime = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startTime = calendar.timeInMillis

        // Get usage stats for the entire day
        val usageStatsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )

        var totalTimeInForegroundMs = 0L
        for (usageStats in usageStatsList) {
            totalTimeInForegroundMs += usageStats.totalTimeInForeground
        }

        // Convert milliseconds to hours
        val totalTimeHours = totalTimeInForegroundMs / (1000.0 * 60.0 * 60.0)

        // --- 1. Display Usage Time (Highlights in Navy Blue as requested) ---
        binding.tvUsageResult.text = "Total Daily Screen Usage: ${"%.1f".format(totalTimeHours)} hours"

        // --- 2. Run Analysis Logic (Thresholds for Mental/Physical Issues) ---
        var resultMessage = ""
        var alertColor = ContextCompat.getColor(this, R.color.status_normal)
        var hasAlert = false

        val MENTAL_THRESHOLD = 5.0 // (Alert 1: Cognitive Overload)
        val PHYSICAL_THRESHOLD = 7.0 // (Alert 2: Sedentary Risk)

        if (totalTimeHours > PHYSICAL_THRESHOLD) {
            resultMessage = "Physical Alert: Severe screen usage (${"%.1f".format(totalTimeHours)} hours) detected. High risk of eye strain, headaches, and sedentary behavior. Immediately reduce usage and move around."
            alertColor = ContextCompat.getColor(this, R.color.alert_physical_orange)
            hasAlert = true
        }
        else if (totalTimeHours > MENTAL_THRESHOLD) {
            resultMessage = "Mental Alert: High daily screen time (${"%.1f".format(totalTimeHours)} hours) detected. This is linked to cognitive overload and can disturb sleep. Enforce a strict 'digital curfew'."
            alertColor = ContextCompat.getColor(this, R.color.alert_mental_red)
            hasAlert = true
        }

        if (!hasAlert) {
            resultMessage = "Good usage habits! Your screen time is healthy."
            alertColor = ContextCompat.getColor(this, R.color.status_normal)
        }

        // --- 3. Display Alert ---
        displayAlert(resultMessage, alertColor)
    }

    private fun displayAlert(message: String, color: Int) {
        binding.tvScreenTimeAlert.text = message
        binding.tvScreenTimeAlert.setTextColor(Color.WHITE)
        binding.tvScreenTimeAlert.setBackgroundColor(color)
        binding.tvScreenTimeAlert.visibility = View.VISIBLE
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
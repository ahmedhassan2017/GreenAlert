package com.example.greenalert

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen") // Required if we are not using the new SplashScreen API for compatibility
class SplashActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    private val SPLASH_TIME_OUT: Long = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // No need to setContentView here if we are immediately deciding where to go
         setContentView(R.layout.activity_splash) // We can remove this if splash is purely transitional

        Handler(Looper.getMainLooper()).postDelayed({
            var isLanguageSelected = LocaleHelper.isLanguageSelected(this@SplashActivity)
            Log.d("SplashActivity", "isLanguageSelected: $isLanguageSelected")

            if (isLanguageSelected) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LanguageSelectionActivity::class.java))
            }
            finish()
        }, SPLASH_TIME_OUT)
    }
} 
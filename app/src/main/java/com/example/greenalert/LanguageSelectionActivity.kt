package com.example.greenalert

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.greenalert.databinding.ActivityLanguageSelectionBinding

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageSelectionBinding

    override fun attachBaseContext(newBase: Context) {
        // Apply the default or previously selected locale before the layout is inflated
        // This ensures the title on this screen itself is localized if the user returns to it.
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // If a language is already selected, and this activity is somehow reached,
        // just proceed to MainActivity. This acts as a safeguard.
        // However, SplashActivity should prevent this screen if language is already chosen.
        if (LocaleHelper.isLanguageSelected(this)) {
            navigateToMain()
            return
        }

        binding.buttonEnglish.setOnClickListener {
            setLanguageAndRestart("en")
        }

        binding.buttonArabic.setOnClickListener {
            setLanguageAndRestart("ar")
        }
    }

    private fun setLanguageAndRestart(languageCode: String) {
        LocaleHelper.setLocale(this, languageCode, true)
        // Restart the app by going to SplashActivity, which will then go to MainActivity
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun navigateToMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
} 
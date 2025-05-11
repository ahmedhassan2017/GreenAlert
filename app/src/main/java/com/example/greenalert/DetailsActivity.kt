package com.example.greenalert

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.greenalert.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    companion object {
        const val EXTRA_INDICATOR = "extra_indicator"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val indicator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_INDICATOR, Indicator::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Indicator>(EXTRA_INDICATOR)
        }

        indicator?.let {
            displayIndicatorDetails(it)
        } ?: run {
            // Handle error: Indicator data not found
            finish() // Close activity if no data
        }
    }

    private fun displayIndicatorDetails(indicator: Indicator) {
        supportActionBar?.title = indicator.name

        binding.textViewDetailIndicatorName.text = indicator.name
        binding.textViewDescription.text = indicator.description
        binding.textViewOptimalRange.text = indicator.optimalRange
        binding.textViewWorstRange.text = indicator.worstRange
        binding.textViewWarnings.text = indicator.warning
        binding.textViewRecommendations.text = indicator.recommendations
        binding.textViewSuitableCrops.text = indicator.suitableCrops.joinToString(", ")
        binding.textViewBestPlantingTime.text = indicator.bestPlantingTime
        binding.textViewBestLocations.text = indicator.bestLocations ?: getString(R.string.not_specified)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to previous one if fake on screen back button is pressed
            return true
        }
        return super.onOptionsItemSelected(item)
    }
} 
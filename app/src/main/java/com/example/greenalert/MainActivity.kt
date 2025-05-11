package com.example.greenalert

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenalert.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var indicatorAdapter: IndicatorAdapter
    private val indicatorsList = mutableListOf<Indicator>()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.title_activity_main)

        setupRecyclerView()
        loadSampleIndicators()
    }

    private fun setupRecyclerView() {
        indicatorAdapter = IndicatorAdapter(indicatorsList) {
            indicator -> navigateToDetails(indicator)
        }
        binding.recyclerViewIndicators.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = indicatorAdapter
        }
    }

    private fun navigateToDetails(indicator: Indicator) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_INDICATOR, indicator)
        }
        startActivity(intent)
    }

    private fun loadSampleIndicators() {
        indicatorsList.clear()

        val indicatorIds = listOf(
            "indicator_salinity",
            "indicator_sfi",
            "indicator_ndmi",
            "indicator_ser",
            "indicator_vhi",
            "indicator_sdl",
            "indicator_lst",
            "indicator_sti",
            "indicator_savi",
            "indicator_ndsi"
        )

        indicatorIds.forEach { resName ->
            val resId = resources.getIdentifier(resName, "array", packageName)
            if (resId != 0) {
                val values = resources.getStringArray(resId)
                indicatorsList.add(
                    Indicator(
                        id = resName,
                        name = values[0],
                        description = values[1],
                        optimalRange = values[2],
                        worstRange = values[3],
                        warning = values[4],
                        recommendations = values[5],
                        suitableCrops = values[6].split(",").map { it.trim() },
                        bestPlantingTime = values[7],
                        bestLocations = values[8].takeIf { it.isNotBlank() },
                        indicatorType = values[9]
                    )
                )
            }
        }

        indicatorAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            R.id.action_switch_language -> {
                showLanguageSelectionDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf(getString(R.string.language_english), getString(R.string.language_arabic))
        val languageCodes = arrayOf("en", "ar") // Corresponding language codes

        val currentLanguage = LocaleHelper.getLanguage(this)
        val currentLanguageIndex = languageCodes.indexOf(currentLanguage).takeIf { it >= 0 } ?: 0

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.language_dialog_title))
            .setSingleChoiceItems(languages, currentLanguageIndex) { dialog, which ->
                val selectedLangCode = languageCodes[which]
                LocaleHelper.setLocale(this, selectedLangCode, true)
                dialog.dismiss()
                // Recreate activity to apply new language
                val intent = Intent(this, SplashActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
} 
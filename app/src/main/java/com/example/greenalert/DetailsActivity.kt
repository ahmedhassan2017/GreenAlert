package com.example.greenalert

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.greenalert.databinding.ActivityDetailsBinding
import com.example.greenalert.data.AppDatabase
import com.example.greenalert.data.ProcessEntity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

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

        binding.buttonActivateProcess.setOnClickListener {
            indicator?.let { showActivateProcessDialog(it) }
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

    private fun showActivateProcessDialog(indicator: Indicator) {
        val dialogView = layoutInflater.inflate(android.R.layout.simple_list_item_2, null)
        val input = EditText(this)
        input.hint = getString(R.string.process_name)

        val dateButton = MaterialButton(this)
        dateButton.text = getString(R.string.select_date)
        var selectedDate: Long? = null

        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        container.setPadding(32, 32, 32, 0)
        container.addView(input)
        container.addView(dateButton)

        dateButton.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                cal.set(year, month, dayOfMonth, 0, 0, 0)
                selectedDate = cal.timeInMillis
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateButton.text = sdf.format(cal.time)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.activateProcess))
            .setView(container)
            .setPositiveButton(getString(R.string.save)) { dialog, _ ->
                val name = input.text.toString().trim()
                if (name.isEmpty() || selectedDate == null) {
                    Toast.makeText(this, getString(R.string.please_enter_a_name_and_select_a_date), Toast.LENGTH_SHORT).show()
                } else {
                    saveProcess(name, selectedDate!!, indicator.name)
                    dialog.dismiss()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun saveProcess(name: String, date: Long, indicatorName: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(applicationContext)
            db.processDao().insertProcess(ProcessEntity(
                processName = name,
                processDate = date,
                indicatorName = indicatorName
            ))
            // Schedule notification for 90 days later
            scheduleProcessReminder(name, indicatorName, date)
            runOnUiThread {
                Toast.makeText(this@DetailsActivity, getString(R.string.process_saved), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun scheduleProcessReminder(processName: String, indicatorName: String, processDate: Long) {
        // For testing, set delay to 10 seconds
        val delayMillis = 10_000L // 10 seconds
        val data = Data.Builder()
            .putString("processName", processName)
            .putString("indicatorName", indicatorName)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<ProcessReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
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
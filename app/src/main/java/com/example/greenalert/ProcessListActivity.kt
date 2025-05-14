package com.example.greenalert

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenalert.data.AppDatabase
import com.example.greenalert.databinding.ActivityProcessListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProcessListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProcessListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProcessListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarProcessList)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.process_list)
        binding.recyclerViewProcesses.layoutManager = LinearLayoutManager(this)
        loadProcesses()
    }

    private fun loadProcesses() {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(applicationContext)
            val processes = db.processDao().getAllProcesses()
            withContext(Dispatchers.Main) {
                binding.recyclerViewProcesses.adapter = ProcessListAdapter(processes)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
} 
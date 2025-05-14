package com.example.greenalert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greenalert.data.ProcessEntity
import java.text.SimpleDateFormat
import java.util.*

class ProcessListAdapter(private val items: List<ProcessEntity>) : RecyclerView.Adapter<ProcessListAdapter.ProcessViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_process, parent, false)
        return ProcessViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProcessViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class ProcessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.textViewProcessName)
        private val date: TextView = itemView.findViewById(R.id.textViewProcessDate)
        private val indicator: TextView = itemView.findViewById(R.id.textViewIndicatorName)
        private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        fun bind(item: ProcessEntity) {
            name.text = item.processName
            date.text = sdf.format(Date(item.processDate))
            indicator.text = item.indicatorName
        }
    }
} 
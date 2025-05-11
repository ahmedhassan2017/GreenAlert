package com.example.greenalert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class IndicatorAdapter(
        private val indicators: MutableList<Indicator>,
        private val onItemClick: (Indicator) -> Unit
) : RecyclerView.Adapter<IndicatorAdapter.IndicatorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndicatorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_indicator, parent, false)
        return IndicatorViewHolder(view)
    }

    override fun onBindViewHolder(holder: IndicatorViewHolder, position: Int) {
        val indicator = indicators[position]
        holder.bind(indicator)
    }

    override fun getItemCount(): Int = indicators.size

    inner class IndicatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewIndicatorName)
        private val descTextView: TextView = itemView.findViewById(R.id.textViewIndicatorShortDesc)

        fun bind(indicator: Indicator) {
            nameTextView.text = indicator.name
            descTextView.text = "${indicator.description}\n${itemView.context.getString(R.string.optimal_range)}: ${indicator.optimalRange}"

            itemView.setOnClickListener {
                onItemClick(indicator)
            }
        }
    }
}

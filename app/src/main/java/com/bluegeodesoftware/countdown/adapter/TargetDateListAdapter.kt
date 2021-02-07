package com.bluegeodesoftware.countdown.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bluegeodesoftware.countdown.EXTRA_DATE
import com.bluegeodesoftware.countdown.R
import com.bluegeodesoftware.countdown.entity.TargetDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TargetDateListAdapter : ListAdapter<TargetDate, TargetDateListAdapter.TargetDateViewHolder>(TargetDateComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetDateViewHolder {
        return TargetDateViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TargetDateViewHolder, position: Int) {
        val current = getItem(position)
        var targetDate = LocalDateTime.ofEpochSecond(current.epoch_time, 0, ZoneOffset.UTC)
        holder.bind(targetDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")))
    }

    class TargetDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val targetDateItemView: TextView = itemView.findViewById(R.id.targetDateListItem)

        fun bind(text: String?) {
            targetDateItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): TargetDateViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.timer_item, parent, false)
                return TargetDateViewHolder(view)
            }
        }
    }

    class TargetDateComparator : DiffUtil.ItemCallback<TargetDate>() {
        override fun areItemsTheSame(oldItem: TargetDate, newItem: TargetDate): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TargetDate, newItem: TargetDate): Boolean {
            return oldItem.epoch_time == newItem.epoch_time
        }
    }
}
package com.bluegeodesoftware.countdown.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bluegeodesoftware.countdown.R
import com.bluegeodesoftware.countdown.entity.TargetDate
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class TargetDateListAdapter(private val onClick: (TargetDate) -> Unit) :
    ListAdapter<TargetDate, TargetDateListAdapter.TargetDateViewHolder>(TargetDateComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetDateViewHolder {
        return TargetDateViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: TargetDateViewHolder, position: Int) {
        updateCountdownItem(position, holder)
    }

    private fun updateCountdownItem(
        position: Int,
        holder: TargetDateViewHolder
    ) {
        val current = getItem(position)
        val targetName = current.target_name
        val targetDate = current.getLocaleTarget()

        val countDownString = current.getCountDownString(holder.itemView.resources)

        holder.bind(
            current,
            targetName,
            targetDate.format(
                DateTimeFormatter.ofLocalizedDateTime(
                    FormatStyle.FULL,
                    FormatStyle.MEDIUM
                )
            ),
            countDownString
        )
    }

    class TargetDateViewHolder(itemView: View, val onClick: (TargetDate) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val targetNameView: TextView = itemView.findViewById(R.id.textViewTargetName)
        private val targetDateItemView: TextView = itemView.findViewById(R.id.targetDateListItem)
        private val countdownItemView: TextView = itemView.findViewById(R.id.countdownTextView)
        private var currentTargetDate: TargetDate? = null

        init {
            itemView.setOnClickListener {
                currentTargetDate?.let {
                    onClick(it)
                }
            }
        }

        fun bind(targetDate: TargetDate, targetName: String, text: String?, countdown: String) {
            targetNameView.text = targetName
            currentTargetDate = targetDate
            targetDateItemView.text = text
            countdownItemView.text = countdown
        }

        companion object {
            fun create(parent: ViewGroup, onClick: (TargetDate) -> Unit): TargetDateViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.timer_item, parent, false)
                return TargetDateViewHolder(view, onClick)
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
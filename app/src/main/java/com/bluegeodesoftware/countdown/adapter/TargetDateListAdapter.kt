package com.bluegeodesoftware.countdown.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bluegeodesoftware.countdown.CountdownApplication
import com.bluegeodesoftware.countdown.DisplayMessageActivity
import com.bluegeodesoftware.countdown.EXTRA_DATE
import com.bluegeodesoftware.countdown.R
import com.bluegeodesoftware.countdown.entity.TargetDate
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TargetDateListAdapter(private val onClick: (TargetDate) -> Unit) : ListAdapter<TargetDate, TargetDateListAdapter.TargetDateViewHolder>(TargetDateComparator()) {

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
        var targetDate = LocalDateTime.ofEpochSecond(current.epoch_time, 0, ZoneOffset.UTC)
        targetDate = targetDate.minusHours(targetDate.hour.toLong())
        targetDate = targetDate.minusMinutes(targetDate.minute.toLong())

        val now = LocalDateTime.now()

        val diff = Duration.between(now, targetDate)
        var diffMinutes = (diff.seconds / 60).toInt()
        val diffSeconds = (diff.seconds % 60).toInt()
        var diffHours = (diffMinutes / 60).toInt()
        diffMinutes %= 60
        var diffDays = (diffHours / 24).toInt()
        diffHours %= 24

        var countDownString = holder.itemView.resources.getQuantityString(
            R.plurals.countdown_with_days,
            diffDays,
            diffDays,
            diffHours,
            diffMinutes,
            diffSeconds
        )

        if (diffDays <= 0) {
            countDownString = holder.itemView.resources.getString(
                R.string.countdown_without_days,
                diffHours,
                diffMinutes,
                diffSeconds
            )
        }

        holder.bind(current, targetDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), countDownString)
    }

    class TargetDateViewHolder(itemView: View, val onClick: (TargetDate) -> Unit) : RecyclerView.ViewHolder(itemView) {
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

        fun bind(targetDate: TargetDate, text: String?, countdown: String) {
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
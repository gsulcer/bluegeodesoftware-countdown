package com.bluegeodesoftware.countdown

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bluegeodesoftware.countdown.entity.TargetDate
import com.bluegeodesoftware.countdown.viewmodel.TargetDateViewModel
import com.bluegeodesoftware.countdown.viewmodel.TargetDateViewModelFactory
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class ViewTargetActivity : AppCompatActivity() {

    lateinit var mainHandler: Handler

    private val updateTextTask = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            minusOneSecond()
            mainHandler.postDelayed(this, 1000)
        }
    }

    private val targetDateViewModel: TargetDateViewModel by viewModels {
        TargetDateViewModelFactory((application as CountdownApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_target)

        mainHandler = Handler(Looper.getMainLooper())

        val target = intent.getSerializableExtra(EXTRA_TARGET) as TargetDate

        val deleteButton = findViewById<ImageButton>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            targetDateViewModel.deleteById(target.id)

            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateTextTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateTextTask)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun minusOneSecond() {
        val target = intent.getSerializableExtra(EXTRA_TARGET) as TargetDate
        val targetDate =
            LocalDateTime.ofEpochSecond(intent.getLongExtra(EXTRA_DATE, 0), 0, ZoneOffset.UTC)

        val now = LocalDateTime.now()

        val diff = Duration.between(now, targetDate)
        var diffMinutes = (diff.seconds / 60).toInt()
        val diffSeconds = (diff.seconds % 60).toInt()
        var diffHours = (diffMinutes / 60)
        diffMinutes %= 60
        val diffDays = (diffHours / 24)
        diffHours %= 24

        var countDownString = resources.getQuantityString(
            R.plurals.countdown_with_days,
            diffDays,
            diffDays,
            diffHours,
            diffMinutes,
            diffSeconds
        )

        if (diffDays <= 0) {
            countDownString =
                getString(R.string.countdown_without_days, diffHours, diffMinutes, diffSeconds)
        }

        findViewById<TextView>(R.id.textView2).apply {
            text = target.target_name
        }

        findViewById<TextView>(R.id.dateTarget).apply {
            text = targetDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        }

        findViewById<TextView>(R.id.differenceView).apply {
            text = countDownString
        }
    }
}
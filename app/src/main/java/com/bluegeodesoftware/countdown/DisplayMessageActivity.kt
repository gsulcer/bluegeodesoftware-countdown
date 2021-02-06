package com.bluegeodesoftware.countdown

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DisplayMessageActivity : AppCompatActivity() {

    lateinit var mainHandler: Handler

    private val updateTextTask = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            minusOneSecond()
            mainHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_message)

        mainHandler = Handler(Looper.getMainLooper())

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
        var targetDate = LocalDateTime.ofEpochSecond(intent.getLongExtra(EXTRA_DATE, 0), 0, ZoneOffset.UTC)
        targetDate = targetDate.minusHours(targetDate.hour.toLong())
        targetDate = targetDate.minusMinutes(targetDate.minute.toLong())
        val now = LocalDateTime.now()

        val diff = Duration.between(now, targetDate)
        var diffMinutes = (diff.seconds / 60).toInt()
        val diffSeconds = (diff.seconds % 60).toInt()
        var diffHours = (diffMinutes / 60).toInt()
        diffMinutes %= 60
        var diffDays = ( diffHours / 24).toInt()
        diffHours %= 24

        var countDownString = resources.getQuantityString(R.plurals.countdown_with_days, diffDays, diffDays, diffHours, diffMinutes, diffSeconds)

        if(diffDays <= 0) {
            countDownString = getString(R.string.countdown_without_days, diffHours, diffMinutes, diffSeconds)
        }

        val targetView = findViewById<TextView>(R.id.dateTarget).apply {
            text = targetDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        }

        val diffView = findViewById<TextView>(R.id.differenceView).apply {
            text = countDownString
        }
    }
}
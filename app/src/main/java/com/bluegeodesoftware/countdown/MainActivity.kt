package com.bluegeodesoftware.countdown

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import java.util.*

const val EXTRA_DATE = "com.bluegeodesoftware.countdown.DATE"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dateView = findViewById<CalendarView>(R.id.calendarView)

        // get a calendar instance
        val calendar = Calendar.getInstance()

        // calendar view date change listener
        dateView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // set the calendar date as calendar view selected date
            calendar.set(year,month,dayOfMonth)

            // set this date as calendar view selected date
            dateView.date = calendar.timeInMillis
        }
    }

    fun sendMessage(view: View) {

        val dateView = findViewById<CalendarView>(R.id.calendarView)
        val date = dateView.date / 1000
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_DATE, date);
        }
        startActivity(intent);
    }
}
package com.bluegeodesoftware.countdown

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import com.bluegeodesoftware.countdown.entity.TargetDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class AddTargetDateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_target_date)

        val dateView = findViewById<CalendarView>(R.id.addDateCalendarView)

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

    fun saveDate(view: View) {
        val resultIntent = Intent()

        val dateView = findViewById<CalendarView>(R.id.addDateCalendarView)

        resultIntent.putExtra(EXTRA_DATE, dateView.date)
        setResult(Activity.RESULT_OK, resultIntent)

        finish()
    }
}
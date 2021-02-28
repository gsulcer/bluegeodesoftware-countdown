package com.bluegeodesoftware.countdown

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.bluegeodesoftware.countdown.entity.TargetDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.math.min

class AddTargetDateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_target_date)

        val dateView = findViewById<CalendarView>(R.id.addDateCalendarView)

        // get a calendar instance
        val calendar = Calendar.getInstance()
        var targetDate = LocalDateTime.ofEpochSecond(dateView.date / 1000, 0, ZoneOffset.UTC)

        var targetView = findViewById<TextView>(R.id.textViewDateTime).apply {
            text = targetDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM))
        }

        val timePicker = findViewById<TimePicker>(R.id.timePicker1)
        timePicker.visibility = INVISIBLE

        targetView.setOnClickListener { view ->
            timePicker.visibility = INVISIBLE
            dateView.visibility = VISIBLE
        }


        // calendar view date change listener
        dateView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // set the calendar date as calendar view selected date
            calendar.set(year,month,dayOfMonth)

            // set this date as calendar view selected date
            dateView.date = calendar.timeInMillis

            targetDate = LocalDateTime.ofEpochSecond(dateView.date / 1000, 0, ZoneOffset.UTC)

            targetView = findViewById<TextView>(R.id.textViewDateTime).apply {
                text = targetDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM))
            }

            dateView.visibility = INVISIBLE
            timePicker.visibility = VISIBLE
        }

        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            targetDate = targetDate.withHour(hourOfDay)
            targetDate = targetDate.withMinute(minute)

            targetView = findViewById<TextView>(R.id.textViewDateTime).apply {
                text = targetDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM))
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun saveDate(view: View) {
        val resultIntent = Intent()

        val dateView = findViewById<CalendarView>(R.id.addDateCalendarView)

        val targetName = findViewById<EditText>(R.id.editTextTargetName)
        val alarm = findViewById<Switch>(R.id.switchAlarm)
        val recur = findViewById<Switch>(R.id.switchAutoRecur)

        resultIntent.putExtra(EXTRA_DATE, dateView.date)
        resultIntent.putExtra(EXTRA_TARGET_NAME, targetName.text.toString())
        resultIntent.putExtra(EXTRA_ALARM, alarm.isChecked)
        resultIntent.putExtra(EXTRA_RECUR, recur.isChecked)
        setResult(Activity.RESULT_OK, resultIntent)

        finish()
    }
}
package com.bluegeodesoftware.countdown

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.CalendarView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluegeodesoftware.countdown.adapter.TargetDateListAdapter
import com.bluegeodesoftware.countdown.entity.TargetDate
import com.bluegeodesoftware.countdown.viewmodel.TargetDateViewModel
import com.bluegeodesoftware.countdown.viewmodel.TargetDateViewModelFactory
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

const val EXTRA_DATE = "com.bluegeodesoftware.countdown.DATE"

class MainActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main)

        mainHandler = Handler(Looper.getMainLooper())

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

        val recyclerView = findViewById<RecyclerView>(R.id.savedTimersView)
        val adapter = TargetDateListAdapter {date -> adapterOnClick(date)}
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        targetDateViewModel.allTargetDates.observe(this, Observer { dates ->
            // Update the cached copy of the words in the adapter.
            dates?.let { adapter.submitList(it) }
        })

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
        val recyclerView = findViewById<RecyclerView>(R.id.savedTimersView)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun clearAllTimers(view: View) {
        targetDateViewModel.deleteAll()
    }

    private fun adapterOnClick(targetDate: TargetDate) {
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
           putExtra(EXTRA_DATE, targetDate.epoch_time);
       }
       startActivity(intent);
    }

    fun sendMessage(view: View) {

        val dateView = findViewById<CalendarView>(R.id.calendarView)
        val date = dateView.date / 1000

        var newDate = LocalDateTime.ofEpochSecond(date, 0, ZoneOffset.UTC)
        newDate = newDate.minusHours(newDate.hour.toLong())
        newDate = newDate.minusMinutes(newDate.minute.toLong())

        val targetDate = TargetDate(epoch_time = newDate.toEpochSecond(ZoneOffset.UTC))

        targetDateViewModel.insert(targetDate)

//        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
//            putExtra(EXTRA_DATE, date);
//        }
//        startActivity(intent);
    }
}
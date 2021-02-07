package com.bluegeodesoftware.countdown

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluegeodesoftware.countdown.adapter.TargetDateListAdapter
import com.bluegeodesoftware.countdown.entity.TargetDate
import com.bluegeodesoftware.countdown.viewmodel.TargetDateViewModel
import com.bluegeodesoftware.countdown.viewmodel.TargetDateViewModelFactory
import java.util.*

const val EXTRA_DATE = "com.bluegeodesoftware.countdown.DATE"

class MainActivity : AppCompatActivity() {

    private val targetDateViewModel: TargetDateViewModel by viewModels {
        TargetDateViewModelFactory((application as CountdownApplication).repository)
    }

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

        val recyclerView = findViewById<RecyclerView>(R.id.savedTimersView)
        val adapter = TargetDateListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        targetDateViewModel.allTargetDates.observe(this, Observer { dates ->
            // Update the cached copy of the words in the adapter.
            dates?.let { adapter.submitList(it) }
        })
    }

    fun clearAllTimers(view: View) {
        targetDateViewModel.deleteAll()
    }

    fun sendMessage(view: View) {

        val dateView = findViewById<CalendarView>(R.id.calendarView)
        val date = dateView.date / 1000

        val targetDate = TargetDate(epoch_time = date)

        targetDateViewModel.insert(targetDate)

        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_DATE, date);
        }
        startActivity(intent);
    }
}
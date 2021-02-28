package com.bluegeodesoftware.countdown

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluegeodesoftware.countdown.adapter.TargetDateListAdapter
import com.bluegeodesoftware.countdown.entity.TargetDate
import com.bluegeodesoftware.countdown.viewmodel.TargetDateViewModel
import com.bluegeodesoftware.countdown.viewmodel.TargetDateViewModelFactory
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZoneOffset

const val EXTRA_TARGET = "com.bluegeodesoftware.countdown.TARGET"
const val EXTRA_ID = "com.bluegeodesoftware.countdown.ID"
const val EXTRA_DATE = "com.bluegeodesoftware.countdown.DATE"
const val EXTRA_TARGET_NAME = "com.bluegeodesoftware.countdown.TARGET_NAME"
const val EXTRA_ALARM  = "com.bluegeodesoftware.countdown.ALARM"
const val EXTRA_RECUR = "com.bluegeodesoftware.countdown.RECUR"

class MainActivity : AppCompatActivity() {

    lateinit var mainHandler: Handler

    private val newTargetDateActivityRequestCode = 1

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

        val recyclerView = findViewById<RecyclerView>(R.id.savedTimersView)
        val adapter = TargetDateListAdapter {date -> adapterOnClick(date)}
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        targetDateViewModel.allTargetDates.observe(this, Observer { dates ->
            // Update the cached copy of the words in the adapter.
            dates?.let { adapter.submitList(it) }
        })

        val fab: View = findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            fabOnClick()
        }
    }

    private fun fabOnClick() {
        val intent = Intent(this, AddTargetDateActivity::class.java)
        startActivityForResult(intent, newTargetDateActivityRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newTargetDateActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val epoch_time = data.getLongExtra(EXTRA_DATE, 0)
                val targetName = data.getStringExtra(EXTRA_TARGET_NAME)
                val alarm = data.getBooleanExtra(EXTRA_ALARM, false)
                val recur = data.getBooleanExtra(EXTRA_RECUR, false)

                val date = epoch_time / 1000
                var newDate = LocalDateTime.ofEpochSecond(date, 0, ZoneOffset.UTC)
//                newDate = newDate.minusSeconds(newDate.second.toLong())
//                newDate = newDate.minusHours(newDate.hour.toLong())
//                newDate = newDate.minusMinutes(newDate.minute.toLong())

                val targetDate = TargetDate(
                    epoch_time = newDate.toEpochSecond(ZoneOffset.UTC),
                    target_name = targetName ?: "",
                    alarm = alarm,
                    auto_recur = recur
                )

                targetDateViewModel.insert(targetDate)
            }
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
        val recyclerView = findViewById<RecyclerView>(R.id.savedTimersView)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun clearAllTimers(view: View) {
        targetDateViewModel.deleteAll()
    }

    private fun adapterOnClick(targetDate: TargetDate) {
        val intent = Intent(this, ViewTargetActivity::class.java).apply {
            putExtra(EXTRA_TARGET, targetDate as Serializable)
            putExtra(EXTRA_ID, targetDate.id)
           putExtra(EXTRA_DATE, targetDate.epoch_time)
       }
       startActivity(intent)
    }
}
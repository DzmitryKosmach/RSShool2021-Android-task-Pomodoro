package com.rsschool.pomodoro

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsschool.pomodoro.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity(), TimerListener, LifecycleObserver {

    private lateinit var binding: ActivityMainBinding

    private var viewModel: MainViewModel? = null
    private val timerAdapter = TimerAdapter(this)
    private var timers = mutableListOf<Timer>()
    private var nextId = 0
    private var startTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timerAdapter
        }

        binding.formButton.setOnClickListener {
            try {
                val seconds = binding.textInput.text.toString().toInt().toLong()
                if (seconds > 359999) {
                    throw IllegalArgumentException()
                }
                val startMs = seconds * 1000
                timers.add(Timer(nextId++, startMs, startMs, false))
                val list = timers.toList()

                timerAdapter.submitList(list)
            } catch (e: Exception) {
                val toast = Toast.makeText(this, "Enter correct data", Toast.LENGTH_LONG)
                toast.show()
            }

        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        startTime = timers.find { it.isStarted }?.currentMs ?: 0
        val startIntent = Intent(this, ForegroundService::class.java)
        startIntent.putExtra(COMMAND_ID, COMMAND_START)
        startIntent.putExtra(STARTED_TIMER_TIME_MS, startTime)
        startService(startIntent)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }

    override fun start(id: Int) {
        changeTimer(id, null, true)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeTimer(id, currentMs, false)
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timerAdapter.submitList(timers.toList())
    }

    private fun changeTimer(id: Int, currentMs: Long?, isStarted: Boolean) {

        val newTimers = mutableListOf<Timer>()
        timers.forEach {
            if (it.id == id) {
                newTimers.add(
                    Timer(
                        it.id,
                        startMs = it.startMs,
                        currentMs ?: it.currentMs,
                        isStarted
                    )
                )
            } else {
                newTimers.add(it)
            }
        }
        timerAdapter.submitList(newTimers)
        timers.clear()
        timers.addAll(newTimers)
    }

    private companion object {
        private const val KEY_LIST = "key_list"
    }
}
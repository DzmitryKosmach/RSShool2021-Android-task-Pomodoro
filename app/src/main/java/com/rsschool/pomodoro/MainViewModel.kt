package com.rsschool.pomodoro

import android.app.Application
import android.graphics.drawable.AnimationDrawable
import android.util.Log
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var timers = mutableListOf<Timer>()
    var timerJob: Job? = null
    var tickJob: Job? = null
    var currentTimer: Timer? = null
    var currentViewHolder: TimerViewHolder? = null

    fun tickTimer() {
        tickJob = GlobalScope.launch(Dispatchers.Main) {
            var interval = UNIT_TEN_MS
            while (true) {

                if (currentTimer != null) {
                    currentTimer!!.currentMs -= interval
                    if (currentTimer!!.currentMs < 0) {
                        val toast = Toast.makeText(getApplication(), "Timer ended!", Toast.LENGTH_LONG)
                        toast.show()
                        break
                    }
                    delay(interval)
                } else {
                    break
                }
            }
        }
    }

    fun continueTimer() {
        Log.i("DDD_Job", currentTimer.toString())
        timerJob = GlobalScope.launch(Dispatchers.Main) {
            var interval = UNIT_TEN_MS
            val _binding = currentViewHolder?.binding
            try {
                while (true) {

                    if (_binding != null) {
                        _binding.timerText.text = currentTimer!!.currentMs.displayTime()
                        _binding.progressBar.progress = getPercentProgress(currentTimer!!)
                    }
                    if (currentTimer?.currentMs!! < 0) {
                        break
                    }
                    delay(interval)
                }
                if (_binding != null) {
                    _binding.imageViewShape.isInvisible = false
                    (_binding.imageViewShape.background as? AnimationDrawable)?.stop()
                }
            } catch (e: CancellationException) {

                if (_binding != null) {
                    _binding.itemButton.text = "START"
                    _binding.imageViewShape.isInvisible = false
                    (_binding.imageViewShape.background as? AnimationDrawable)?.stop()
                    Log.i("DDD_Job: ", "job canceled")
                }
            }
        }
    }

}
package com.rsschool.pomodoro

import android.graphics.drawable.AnimationDrawable
import android.util.Log
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {
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
        Log.i("DDD_Job", currentViewHolder.toString())
        //val position = timerViewHolder.absoluteAdapterPosition
        timerJob = GlobalScope.launch(Dispatchers.Main) {
            var interval = UNIT_TEN_MS
            //var check = true
//            val timer = requireNotNull(currentViewHolder?.timer)
//            currentTimer = timer
            val _binding = currentViewHolder?.binding
            try {
                while (true) {

//                    if (position == timerViewHolder.absoluteAdapterPosition) {
                    //Log.i("DDD_Tick", currentViewHolder.toString())
                    //  interval = UNIT_TEN_MS
                    //timer.currentMs -= interval
                    if (_binding != null) {
                        _binding.timerText.text = currentTimer!!.currentMs.displayTime()
                        _binding.progressBar.progress = getPercentProgress(currentTimer!!)
                    }
                    if (currentTimer?.currentMs!! < 0) {
                        break
                    }
                    delay(interval)
//                    } else if (check) {
//                        check = false
//                        interval /= 2

//                    } else {
//                        Log.i("DDD_Tick2", timerViewHolder.toString())
//                        //cancel()
//                        delay(interval)
//                    }
                }
                if (_binding != null) {
                    _binding.imageViewShape.isInvisible = false
                    (_binding.imageViewShape.background as? AnimationDrawable)?.stop()
                }
                //timerJob?.cancel()
            } catch (e: CancellationException) {

                //timerViewHolder.listener.stop(timer.id, timer.currentMs)
                if (_binding != null) {
                    _binding.itemButton.text = "START"
                    _binding.imageViewShape.isInvisible = false
                    (_binding.imageViewShape.background as? AnimationDrawable)?.stop()
                    //timerJob = null
                    Log.i("DDD_Job: ", "job canceled")
                }
            }
        }
    }

    companion object {
//        var timerJob: Job? = null
//        var currentTimer: Timer? = null
//        var position = 0
    }

}
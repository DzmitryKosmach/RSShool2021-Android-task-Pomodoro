package com.rsschool.pomodoro

import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.rsschool.pomodoro.databinding.RecyclerViewItemBinding

class TimerViewHolder(
    val binding: RecyclerViewItemBinding, val listener: TimerListener,
    private val context: MainActivity
) :
    RecyclerView.ViewHolder(binding.root) {

    var timer: Timer? = null
    private val viewModel = ViewModelProvider(context).get(MainViewModel::class.java)

    fun bind(timer: Timer) {
        binding.timerText.text = timer.currentMs.displayTime()
        binding.progressBar.progress = getPercentProgress(timer)

        this.timer = timer

        if (timer.isStarted) {
            startTimer(timer)
        } else {
            stopTimer()
        }

        initButtonsListeners(timer)
    }

    private fun initButtonsListeners(timer: Timer) {
        binding.itemButton.setOnClickListener {
            if (timer.isStarted) {
                viewModel.tickJob?.cancel()
                viewModel.timerJob?.cancel()
                listener.stop(timer.id, timer.currentMs)
            } else {
                if (viewModel.currentTimer != null) {
                    val startedTimer = requireNotNull(viewModel.currentTimer)
                    viewModel.tickJob?.cancel()
                    viewModel.timerJob?.cancel()
                    listener.stop(startedTimer.id, startedTimer.currentMs)
                }
                listener.start(timer.id)
            }
        }

        binding.imageButtonDelete.setOnClickListener {
            if (timer.isStarted) {
                viewModel.tickJob?.cancel()
                viewModel.timerJob?.cancel()
            }
            listener.delete(timer.id)
        }
    }

    fun startTimer(timer: Timer) {
        binding.itemButton.text = context.resources.getString(R.string.item_button_stop)

        viewModel.currentViewHolder = this
        viewModel.currentTimer = timer

        viewModel.tickJob?.cancel()
        viewModel.tickTimer()

        viewModel.timerJob?.cancel()
        viewModel.continueTimer()

        binding.itemButton.text = context.resources.getString(R.string.item_button_stop)
        binding.imageViewShape.isInvisible = false
        (binding.imageViewShape.background as? AnimationDrawable)?.start()

    }

    private fun stopTimer() {
        binding.itemButton.text = context.resources.getString(R.string.item_button_start);

        binding.imageViewShape.isInvisible = false
        (binding.imageViewShape.background as? AnimationDrawable)?.stop()

    }

}
package com.rsschool.pomodoro

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rsschool.pomodoro.databinding.RecyclerViewItemBinding

class TimerAdapter(context: MainActivity) : ListAdapter<Timer, TimerViewHolder>(itemComparator) {

    private var currentTimerId = 0
    private var _context = context;
    private var viewModel: MainViewModel? = null;

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        viewModel = ViewModelProvider(_context).get(MainViewModel::class.java)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewItemBinding.inflate(layoutInflater, parent, false)
        return TimerViewHolder(
            binding,
            parent.context as TimerListener,
            parent.context as MainActivity
        )
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: TimerViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.i("DDD_onViewDetached", holder.toString())
        val timer = requireNotNull(holder.timer)
        if (timer.isStarted) {
            viewModel?.timerJob?.cancel()
            currentTimerId = timer.id
 //           viewModel?.currentViewHolder = null

//            Старт таймера без визуализации
//                    viewModel.timerJobHiden.
        }

    }

    override fun onViewAttachedToWindow(holder: TimerViewHolder) {
        super.onViewAttachedToWindow(holder)
        val timer = requireNotNull(holder.timer)
        Log.i("DDD_onViewAttached", holder.toString())
        if (timer.isStarted ) {
//        if (timer.isStarted && timer.id == currentTimerId && viewModel?.tickJob!!.isCancelled ) {
            holder.binding.itemButton.text = _context.resources.getString(R.string.item_button_stop)

            viewModel?.currentViewHolder = holder
            viewModel?.continueTimer()
            //Отключаем таймер без визуализации и включаем с визуализацией
        }
    }


    private companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<Timer>() {

            override fun areItemsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.currentMs == newItem.currentMs &&
                        oldItem.isStarted == newItem.isStarted
            }
        }
    }
}
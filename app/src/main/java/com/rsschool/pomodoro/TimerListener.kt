package com.rsschool.pomodoro

interface TimerListener {
    fun start(id: Int)

    fun stop(id: Int, currentMs: Long)

    fun delete(id: Int)
}
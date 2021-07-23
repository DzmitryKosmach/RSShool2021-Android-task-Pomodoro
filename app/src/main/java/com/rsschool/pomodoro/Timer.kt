package com.rsschool.pomodoro

data class Timer(
    val id: Int,
    var startMs: Long,
    var currentMs: Long,
    var isStarted: Boolean
)

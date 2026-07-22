package com.sys.androidkit.feature.system

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ForegroundTimerBus {
    private val _running = MutableStateFlow(false)
    val running: StateFlow<Boolean> = _running.asStateFlow()

    private val _seconds = MutableStateFlow(0)
    val seconds: StateFlow<Int> = _seconds.asStateFlow()

    fun markRunning(running: Boolean) {
        _running.value = running
    }

    fun tick(seconds: Int) {
        _seconds.value = seconds
    }

    fun reset() {
        _seconds.value = 0
        _running.value = false
    }
}

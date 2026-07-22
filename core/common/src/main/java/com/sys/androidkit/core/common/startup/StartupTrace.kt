package com.sys.androidkit.core.common.startup

/**
 * 记录 Application 启动耗时，供 Startup Lab 展示。
 */
object StartupTrace {
    @Volatile
    var appCreateCostMs: Long = -1L
        private set

    @Volatile
    var processStartElapsedRealtime: Long = -1L
        private set

    fun markAppCreate(costMs: Long, processStartElapsedRealtime: Long) {
        appCreateCostMs = costMs
        this.processStartElapsedRealtime = processStartElapsedRealtime
    }
}

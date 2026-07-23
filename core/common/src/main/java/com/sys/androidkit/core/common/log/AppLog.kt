package com.sys.androidkit.core.common.log

import timber.log.Timber

/**
 * COM-04：统一日志门面。
 * - Debug 下由 Application plant [Timber.DebugTree] + [InMemoryLogTree]
 * - 业务优先 [AppLog] / Timber；Logcat 与应用内 Viewer 同步可见
 */
object AppLog {
    private const val DEFAULT_TAG = "AndroidKit"
    var enabled: Boolean = true

    fun v(message: String, tag: String = DEFAULT_TAG) {
        if (enabled) Timber.tag(tag).v(message)
    }

    fun d(message: String, tag: String = DEFAULT_TAG) {
        if (enabled) Timber.tag(tag).d(message)
    }

    fun i(message: String, tag: String = DEFAULT_TAG) {
        if (enabled) Timber.tag(tag).i(message)
    }

    fun w(message: String, tag: String = DEFAULT_TAG) {
        if (enabled) Timber.tag(tag).w(message)
    }

    fun e(message: String, throwable: Throwable? = null, tag: String = DEFAULT_TAG) {
        if (!enabled) return
        if (throwable != null) {
            Timber.tag(tag).e(throwable, message)
        } else {
            Timber.tag(tag).e(message)
        }
    }
}

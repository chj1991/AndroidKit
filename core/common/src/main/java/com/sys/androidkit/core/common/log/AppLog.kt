package com.sys.androidkit.core.common.log

import android.util.Log

object AppLog {
    private const val DEFAULT_TAG = "AndroidKit"
    var enabled: Boolean = true

    fun d(message: String, tag: String = DEFAULT_TAG) {
        if (enabled) Log.d(tag, message)
    }

    fun i(message: String, tag: String = DEFAULT_TAG) {
        if (enabled) Log.i(tag, message)
    }

    fun e(message: String, throwable: Throwable? = null, tag: String = DEFAULT_TAG) {
        if (enabled) Log.e(tag, message, throwable)
    }
}

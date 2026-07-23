package com.sys.androidkit.core.common.log

import timber.log.Timber

/** Timber Tree：把日志写入 [LogBuffer]，供应用内查看器展示。 */
class InMemoryLogTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val full = if (t != null) {
            buildString {
                append(message)
                append('\n')
                append(t.stackTraceToString().take(1200))
            }
        } else {
            message
        }
        LogBuffer.append(priority, tag, full)
    }
}

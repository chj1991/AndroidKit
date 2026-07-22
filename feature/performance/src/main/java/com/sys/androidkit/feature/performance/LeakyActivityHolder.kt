package com.sys.androidkit.feature.performance

import android.content.Context

/**
 * 教学用：演示单例错误持有 Activity Context 造成泄漏。
 * 正确做法应持有 applicationContext 或不用单例持有 Context。
 */
object LeakyActivityHolder {
    private var retained: Context? = null

    fun retain(context: Context) {
        retained = context
    }

    fun retainApplication(context: Context) {
        retained = context.applicationContext
    }

    fun clear() {
        retained = null
    }

    fun describe(): String {
        val ctx = retained ?: return "当前未持有任何 Context"
        val isApp = ctx === ctx.applicationContext
        return if (isApp) {
            "持有 ApplicationContext（相对安全）: ${ctx.javaClass.simpleName}"
        } else {
            "持有 Activity Context（泄漏风险）: ${ctx.javaClass.simpleName}"
        }
    }

    fun isLeakingActivity(): Boolean {
        val ctx = retained ?: return false
        return ctx !== ctx.applicationContext
    }
}

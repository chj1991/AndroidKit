package com.sys.androidkit.core.network

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicReference

data class NetworkCallMetrics(
    val method: String,
    val url: String,
    val requestId: String?,
    val dnsMs: Long?,
    val connectMs: Long?,
    val ttfbMs: Long?,
    val totalMs: Long,
    val code: Int?,
    val secure: Boolean,
) {
    fun summary(): String = buildString {
        append("$method $url")
        append(" · total=${totalMs}ms")
        dnsMs?.let { append(" dns=${it}ms") }
        connectMs?.let { append(" connect=${it}ms") }
        ttfbMs?.let { append(" ttfb=${it}ms") }
        code?.let { append(" code=$it") }
        requestId?.let { append(" id=${it.take(8)}…") }
        if (secure) append(" tls")
    }
}

/**
 * 简易网络监控：由 [NetworkTimingEventListener] 写入最近一次与近期调用耗时。
 */
object NetworkMonitor {
    private const val MAX_RECENT = 8

    private val lastRef = AtomicReference<NetworkCallMetrics?>(null)
    private val recent = CopyOnWriteArrayList<NetworkCallMetrics>()

    val last: NetworkCallMetrics? get() = lastRef.get()

    fun recentCalls(): List<NetworkCallMetrics> = recent.toList()

    fun record(metrics: NetworkCallMetrics) {
        lastRef.set(metrics)
        recent.add(0, metrics)
        while (recent.size > MAX_RECENT) {
            recent.removeAt(recent.lastIndex)
        }
    }

    fun clear() {
        lastRef.set(null)
        recent.clear()
    }
}

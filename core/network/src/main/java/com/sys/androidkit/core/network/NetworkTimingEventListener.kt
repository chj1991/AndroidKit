package com.sys.androidkit.core.network

import okhttp3.Call
import okhttp3.EventListener
import okhttp3.Handshake
import okhttp3.Protocol
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * OkHttp EventListener：采集 DNS / 连接 / TTFB / 总耗时，写入 [NetworkMonitor]。
 */
class NetworkTimingEventListener : EventListener() {

    private var callStartNs: Long = 0L
    private var dnsStartNs: Long = 0L
    private var dnsMs: Long? = null
    private var connectStartNs: Long = 0L
    private var connectMs: Long? = null
    private var responseHeadersStartNs: Long = 0L
    private var code: Int? = null
    private var secure: Boolean = false
    private var method: String = ""
    private var url: String = ""
    private var requestId: String? = null

    override fun callStart(call: Call) {
        callStartNs = System.nanoTime()
        method = call.request().method
        url = call.request().url.toString()
        requestId = call.request().header(HeaderInterceptor.HEADER_REQUEST_ID)
    }

    override fun dnsStart(call: Call, domainName: String) {
        dnsStartNs = System.nanoTime()
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<java.net.InetAddress>) {
        if (dnsStartNs > 0L) {
            dnsMs = elapsedMs(dnsStartNs)
        }
    }

    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        connectStartNs = System.nanoTime()
    }

    override fun connectEnd(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?,
    ) {
        if (connectStartNs > 0L) {
            connectMs = elapsedMs(connectStartNs)
        }
    }

    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        secure = handshake != null
    }

    override fun responseHeadersStart(call: Call) {
        responseHeadersStartNs = System.nanoTime()
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        code = response.code
    }

    override fun callEnd(call: Call) {
        publish()
    }

    override fun callFailed(call: Call, ioe: IOException) {
        publish()
    }

    private fun publish() {
        if (callStartNs == 0L) return
        val totalMs = elapsedMs(callStartNs)
        val ttfbMs = if (responseHeadersStartNs > 0L) {
            TimeUnit.NANOSECONDS.toMillis(responseHeadersStartNs - callStartNs)
        } else {
            null
        }
        NetworkMonitor.record(
            NetworkCallMetrics(
                method = method,
                url = url,
                requestId = requestId,
                dnsMs = dnsMs,
                connectMs = connectMs,
                ttfbMs = ttfbMs,
                totalMs = totalMs,
                code = code,
                secure = secure,
            ),
        )
    }

    private fun elapsedMs(startNs: Long): Long =
        TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

    companion object {
        val FACTORY = EventListener.Factory { NetworkTimingEventListener() }
    }
}

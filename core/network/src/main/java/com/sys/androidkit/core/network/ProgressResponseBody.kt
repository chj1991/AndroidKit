package com.sys.androidkit.core.network

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Source
import okio.buffer

/** NW-05：包装响应体，回调下载进度 */
class ProgressResponseBody(
    private val responseBody: ResponseBody,
    private val listener: ProgressListener,
) : ResponseBody() {

    private val bufferedSource: BufferedSource by lazy {
        source(responseBody.source()).buffer()
    }

    override fun contentType(): MediaType? = responseBody.contentType()

    override fun contentLength(): Long = responseBody.contentLength()

    override fun source(): BufferedSource = bufferedSource

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            private var totalBytesRead = 0L

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                val done = bytesRead == -1L
                if (!done) {
                    totalBytesRead += bytesRead
                }
                listener.onProgress(totalBytesRead, contentLength(), done)
                return bytesRead
            }
        }
    }
}

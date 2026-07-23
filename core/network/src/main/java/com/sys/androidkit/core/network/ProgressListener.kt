package com.sys.androidkit.core.network

fun interface ProgressListener {
    /**
     * @param bytesProcessed 已读/已写字节
     * @param contentLength 总长度；未知时为 -1
     * @param done 是否结束
     */
    fun onProgress(bytesProcessed: Long, contentLength: Long, done: Boolean)
}

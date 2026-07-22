package com.sys.androidkit.core.common.ext

import com.sys.androidkit.core.common.log.AppLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.safeLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    onError: (Throwable) -> Unit = { AppLog.e("safeLaunch failed", it) },
    block: suspend CoroutineScope.() -> Unit,
): kotlinx.coroutines.Job {
    val handler = CoroutineExceptionHandler { _, throwable -> onError(throwable) }
    return launch(context + handler, block = block)
}

package com.sys.androidkit.feature.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * LC-04：自定义 [DefaultLifecycleObserver]，把宿主生命周期回调转成可读日志。
 */
class LoggingLifecycleObserver(
    private val tag: String,
    private val onEvent: (String) -> Unit,
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) = emit("onCreate")

    override fun onStart(owner: LifecycleOwner) = emit("onStart")

    override fun onResume(owner: LifecycleOwner) = emit("onResume")

    override fun onPause(owner: LifecycleOwner) = emit("onPause")

    override fun onStop(owner: LifecycleOwner) = emit("onStop")

    override fun onDestroy(owner: LifecycleOwner) = emit("onDestroy")

    private fun emit(event: String) {
        onEvent("Observer[$tag].$event")
    }
}

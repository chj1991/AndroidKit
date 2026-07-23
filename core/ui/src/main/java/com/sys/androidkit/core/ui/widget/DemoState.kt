package com.sys.androidkit.core.ui.widget

/** UI-04：通用页面状态 */
sealed class DemoState {
    data object Loading : DemoState()
    data object Content : DemoState()
    data class Empty(val message: CharSequence? = null) : DemoState()
    data class Error(val message: CharSequence, val canRetry: Boolean = true) : DemoState()
}

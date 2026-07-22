package com.sys.androidkit.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sys.androidkit.core.common.ext.safeLaunch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

open class BaseViewModel : ViewModel() {
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.safeLaunch(block = block)
    }
}

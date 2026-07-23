package com.sys.androidkit.debug

import leakcanary.LeakCanary

/**
 * Debug 专用：调低保留对象阈值，便于 Leak Lab 更快触发堆转储分析。
 * LeakCanary 本身由 ContentProvider 自动安装，此处只做演示向配置。
 */
object LeakCanaryInstaller {

    @JvmStatic
    fun install() {
        LeakCanary.config = LeakCanary.config.copy(
            retainedVisibleThreshold = 1,
        )
        LeakCanary.showLeakDisplayActivityLauncherIcon(true)
    }
}

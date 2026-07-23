package com.peakmain.ui

import android.app.Application
import androidx.core.content.FileProvider
import com.peakmain.ui.constants.BasicUIUtils
import com.peakmain.ui.utils.ActivityUtils

/**
 * author ：Peakmain
 * createTime：2020/12/24
 * mail:2726449200@qq.com
 * describe：
 */
class BasicUIProvider : FileProvider() {
    override fun onCreate(): Boolean {
        val app = context?.applicationContext as? Application
        BasicUIUtils.init(app)
        // TopToast / FPS 等依赖栈顶 Activity，需注册生命周期回调
        app?.let { ActivityUtils.mInstance.init(it) }
        return true
    }
}
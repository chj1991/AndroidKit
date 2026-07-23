package com.sys.androidkit.feature.androidktx

import android.content.Context
import com.dengzii.ktx.android.content.Preferences
import com.dengzii.ktx.android.content.preference

class DemoAppConfig(context: Context) : Preferences(context, "spf_android_ktx_demo") {
    var userName: String by preference("Tom")
    var age by preference(10)
}

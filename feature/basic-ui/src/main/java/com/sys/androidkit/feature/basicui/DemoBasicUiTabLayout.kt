package com.sys.androidkit.feature.basicui

import android.content.Context
import android.util.AttributeSet
import com.peakmain.ui.tablayout.BaseTabLayout

/**
 * Wiki：继承 BaseTabLayout，泛型换成业务实体；这里用 String 标题。
 */
class DemoBasicUiTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BaseTabLayout<String>(context, attrs, defStyleAttr) {

    override fun setTableTitle(bean: List<String>?, position: Int): String {
        return bean?.getOrNull(position).orEmpty()
    }
}

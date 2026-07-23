package com.sys.androidkit.core.ui.ext

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/**
 * UI-06：系统栏 / IME Insets 约定。
 *
 * - Activity 根布局：通常 [applySystemBarsPadding] 一次即可，避免 Fragment 再叠一层。
 * - 需要键盘避让时：再对输入区域调用 [applyImePadding]。
 */
fun View.applySystemBarsPadding(
    left: Boolean = true,
    top: Boolean = true,
    right: Boolean = true,
    bottom: Boolean = true,
) {
    val initialLeft = paddingLeft
    val initialTop = paddingTop
    val initialRight = paddingRight
    val initialBottom = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updatePadding(
            left = initialLeft + if (left) bars.left else 0,
            top = initialTop + if (top) bars.top else 0,
            right = initialRight + if (right) bars.right else 0,
            bottom = initialBottom + if (bottom) bars.bottom else 0,
        )
        insets
    }
    ViewCompat.requestApplyInsets(this)
}

fun View.applyImePadding(bottom: Boolean = true) {
    val initialBottom = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val bottomInset = maxOf(ime.bottom, bars.bottom)
        if (bottom) {
            view.updatePadding(bottom = initialBottom + bottomInset)
        }
        insets
    }
    ViewCompat.requestApplyInsets(this)
}

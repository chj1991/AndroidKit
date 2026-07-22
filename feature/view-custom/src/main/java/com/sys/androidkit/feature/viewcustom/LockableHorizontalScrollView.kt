package com.sys.androidkit.feature.viewcustom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import kotlin.math.abs

/**
 * 可切换是否向父布局申请拦截：用于演示滑动冲突。
 */
class LockableHorizontalScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : HorizontalScrollView(context, attrs) {

    /** true：横向滑动时 requestDisallowInterceptTouchEvent(true) */
    var lockParentOnHorizontalScroll: Boolean = true

    private var downX = 0f
    private var downY = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                if (lockParentOnHorizontalScroll) {
                    parent?.requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = abs(ev.x - downX)
                val dy = abs(ev.y - downY)
                if (lockParentOnHorizontalScroll && dx > dy) {
                    parent?.requestDisallowInterceptTouchEvent(true)
                } else if (!lockParentOnHorizontalScroll) {
                    parent?.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent?.requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (lockParentOnHorizontalScroll && ev.actionMasked == MotionEvent.ACTION_MOVE) {
            parent?.requestDisallowInterceptTouchEvent(true)
        }
        return super.onTouchEvent(ev)
    }
}

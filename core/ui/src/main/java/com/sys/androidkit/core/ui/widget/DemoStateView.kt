package com.sys.androidkit.core.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.sys.androidkit.core.ui.R
import com.sys.androidkit.core.ui.databinding.ViewDemoStateBinding

/**
 * UI-04：Loading / Empty / Error / Content 状态容器。
 * Content 通过 [contentContainer] 承载业务视图。
 */
class DemoStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewDemoStateBinding =
        ViewDemoStateBinding.inflate(LayoutInflater.from(context), this, true)

    val contentContainer: FrameLayout get() = binding.stateContent

    private var onRetry: (() -> Unit)? = null

    init {
        binding.btnRetry.setOnClickListener { onRetry?.invoke() }
        render(DemoState.Content)
    }

    fun setOnRetry(listener: (() -> Unit)?) {
        onRetry = listener
    }

    fun render(state: DemoState) {
        binding.stateLoading.isVisible = state is DemoState.Loading
        binding.stateEmpty.isVisible = state is DemoState.Empty
        binding.stateError.isVisible = state is DemoState.Error
        binding.stateContent.isVisible = state is DemoState.Content

        when (state) {
            DemoState.Loading -> Unit
            DemoState.Content -> Unit
            is DemoState.Empty -> {
                binding.tvEmptyMessage.text = state.message
                    ?: context.getString(R.string.core_ui_state_empty)
            }
            is DemoState.Error -> {
                binding.tvErrorMessage.text = state.message
                binding.btnRetry.isVisible = state.canRetry
            }
        }
    }
}

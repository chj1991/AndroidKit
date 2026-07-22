package com.sys.androidkit.core.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.sys.androidkit.core.ui.ext.showSnackbar

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = requireNotNull(_binding)

    protected abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun initView() = Unit

    protected open fun initObserver() = Unit

    protected fun showMessage(
        message: CharSequence,
        length: Int = Snackbar.LENGTH_SHORT,
        actionLabel: CharSequence? = null,
        action: (() -> Unit)? = null,
    ) = binding.root.showSnackbar(message, length, actionLabel, action)

    protected fun showMessage(
        @StringRes messageRes: Int,
        length: Int = Snackbar.LENGTH_SHORT,
        actionLabel: CharSequence? = null,
        action: (() -> Unit)? = null,
    ) = binding.root.showSnackbar(messageRes, length, actionLabel, action)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

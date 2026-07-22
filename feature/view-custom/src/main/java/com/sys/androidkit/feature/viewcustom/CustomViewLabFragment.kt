package com.sys.androidkit.feature.viewcustom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.viewcustom.databinding.FragmentCustomViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomViewLabFragment : BaseFragment<FragmentCustomViewBinding>() {

    private val viewModel: CustomViewLabViewModel by viewModels()
    private var syncing = false

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCustomViewBinding = FragmentCustomViewBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.ringProgress.onProgressChanged = { progress ->
            if (!syncing) {
                viewModel.setProgressPercent((progress * 100).toInt())
            }
        }
        binding.slider.addOnChangeListener(
            Slider.OnChangeListener { _, value, fromUser ->
                if (fromUser) {
                    viewModel.setProgressPercent(value.toInt())
                }
            },
        )
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    syncing = true
                    binding.tvProgress.text = getString(
                        R.string.feature_view_custom_progress,
                        state.progressPercent,
                    )
                    binding.ringProgress.progress = state.progressPercent / 100f
                    if (binding.slider.value.toInt() != state.progressPercent) {
                        binding.slider.value = state.progressPercent.toFloat()
                    }
                    syncing = false
                }
            }
        }
    }
}

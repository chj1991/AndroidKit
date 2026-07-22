package com.sys.androidkit.feature.viewcustom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.viewcustom.databinding.FragmentNestedScrollBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NestedScrollLabFragment : BaseFragment<FragmentNestedScrollBinding>() {

    private val viewModel: NestedScrollLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentNestedScrollBinding = FragmentNestedScrollBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.switchChildWins.setOnCheckedChangeListener { _, checked ->
            viewModel.setChildWins(checked)
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvStatus.text = state.status
                    binding.horizontalScroller.lockParentOnHorizontalScroll = state.childWins
                    if (binding.switchChildWins.isChecked != state.childWins) {
                        binding.switchChildWins.isChecked = state.childWins
                    }
                }
            }
        }
    }
}

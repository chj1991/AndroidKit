package com.sys.androidkit.feature.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.storage.databinding.FragmentMmkvBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MmkvLabFragment : BaseFragment<FragmentMmkvBinding>() {

    private val viewModel: MmkvLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMmkvBinding = FragmentMmkvBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnSaveNickname.setOnClickListener {
            viewModel.saveNickname(binding.etNickname.text?.toString().orEmpty())
        }
        binding.btnInc.setOnClickListener { viewModel.incCounter() }
        binding.btnToggleFlag.setOnClickListener { viewModel.toggleFlag() }
        binding.btnClear.setOnClickListener { viewModel.clearDemoKeys() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvNickname.text =
                        getString(R.string.feature_storage_mmkv_nickname, state.nickname.ifEmpty { "(空)" })
                    binding.tvCounter.text =
                        getString(R.string.feature_storage_mmkv_counter, state.counter)
                    binding.tvFlag.text =
                        getString(R.string.feature_storage_mmkv_flag, state.flag.toString())
                    binding.tvMeta.text = getString(
                        R.string.feature_storage_mmkv_meta,
                        state.keys.size,
                        state.totalSize,
                        state.keys.joinToString(", ").ifEmpty { "(无)" },
                    )
                    binding.tvStatus.text = state.status
                }
            }
        }
    }
}

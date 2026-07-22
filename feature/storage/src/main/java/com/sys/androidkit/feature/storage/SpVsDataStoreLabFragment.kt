package com.sys.androidkit.feature.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.storage.databinding.FragmentSpVsDatastoreBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SpVsDataStoreLabFragment : BaseFragment<FragmentSpVsDatastoreBinding>() {

    private val viewModel: SpVsDataStoreLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSpVsDatastoreBinding =
        FragmentSpVsDatastoreBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnWrite.setOnClickListener {
            viewModel.writeBoth(binding.etValue.text?.toString().orEmpty())
        }
        binding.btnClear.setOnClickListener { viewModel.clearBoth() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvSpValue.text = getString(
                        R.string.feature_storage_sp_value,
                        state.spValue.ifEmpty { "(空)" },
                    )
                    binding.tvDsValue.text = getString(
                        R.string.feature_storage_ds_value,
                        state.dataStoreValue.ifEmpty { "(空)" },
                    )
                    binding.tvStatus.text = state.status
                }
            }
        }
    }
}

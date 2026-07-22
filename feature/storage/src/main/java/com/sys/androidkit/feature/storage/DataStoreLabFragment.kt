package com.sys.androidkit.feature.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.datastore.ThemeMode
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.storage.databinding.FragmentDatastoreBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DataStoreLabFragment : BaseFragment<FragmentDatastoreBinding>() {

    private val viewModel: DataStoreLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDatastoreBinding = FragmentDatastoreBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnSystem.setOnClickListener { viewModel.setTheme(ThemeMode.SYSTEM) }
        binding.btnLight.setOnClickListener { viewModel.setTheme(ThemeMode.LIGHT) }
        binding.btnDark.setOnClickListener { viewModel.setTheme(ThemeMode.DARK) }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvTheme.text = "当前主题偏好：${state.themeMode}"
                }
            }
        }
    }
}

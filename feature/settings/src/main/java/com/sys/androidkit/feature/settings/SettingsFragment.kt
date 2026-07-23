package com.sys.androidkit.feature.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.datastore.ThemeMode
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.settings.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel: SettingsViewModel by viewModels()
    private var updatingUi = false

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.rgTheme.setOnCheckedChangeListener { _, checkedId ->
            if (updatingUi) return@setOnCheckedChangeListener
            val mode = when (checkedId) {
                R.id.rbLight -> ThemeMode.LIGHT
                R.id.rbDark -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM
            }
            viewModel.setTheme(mode)
        }
        binding.btnAbout.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("androidkit://settings/about".toUri())
                .build()
            findNavController().navigate(request)
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updatingUi = true
                    val checkedId = when (state.themeMode) {
                        ThemeMode.LIGHT -> R.id.rbLight
                        ThemeMode.DARK -> R.id.rbDark
                        ThemeMode.SYSTEM -> R.id.rbSystem
                    }
                    binding.rgTheme.check(checkedId)
                    binding.tvThemeHint.text = getString(
                        R.string.feature_settings_theme_hint,
                        state.themeMode.name,
                    )
                    updatingUi = false
                }
            }
        }
    }
}

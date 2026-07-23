package com.sys.androidkit.feature.network

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.core.ui.widget.DemoState
import com.sys.androidkit.feature.network.databinding.FragmentCacheBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CacheLabFragment : BaseFragment<FragmentCacheBinding>() {

    private val viewModel: CacheLabViewModel by viewModels()
    private lateinit var reportView: TextView

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCacheBinding = FragmentCacheBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        val pad = resources.getDimensionPixelSize(com.sys.androidkit.core.ui.R.dimen.ak_content_padding)
        reportView = TextView(requireContext()).apply {
            setPadding(pad, pad / 2, pad, pad / 2)
            typeface = android.graphics.Typeface.MONOSPACE
            TextViewCompat.setTextAppearance(
                this,
                com.google.android.material.R.style.TextAppearance_Material3_BodySmall,
            )
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        }
        binding.stateView.contentContainer.addView(
            reportView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            ),
        )
        binding.stateView.setOnRetry { viewModel.fetch(CacheMode.DEFAULT) }
        binding.btnDefault.setOnClickListener { viewModel.fetch(CacheMode.DEFAULT) }
        binding.btnNetwork.setOnClickListener { viewModel.fetch(CacheMode.FORCE_NETWORK) }
        binding.btnCache.setOnClickListener { viewModel.fetch(CacheMode.FORCE_CACHE) }
        binding.btnClear.setOnClickListener { viewModel.clearCache() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvStats.text = state.cacheStats
                    binding.stateView.render(state.demoState)
                    if (state.demoState is DemoState.Content) {
                        reportView.text = state.report.ifEmpty {
                            getString(R.string.feature_network_cache_placeholder)
                        }
                    }
                }
            }
        }
    }
}

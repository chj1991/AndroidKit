package com.sys.androidkit.feature.network

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.network.databinding.FragmentInterceptorBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InterceptorLabFragment : BaseFragment<FragmentInterceptorBinding>() {

    private val viewModel: InterceptorLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentInterceptorBinding = FragmentInterceptorBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.etToken.setText(viewModel.uiState.value.token)
        binding.etToken.doAfterTextChanged { text ->
            viewModel.onTokenChanged(text?.toString().orEmpty())
        }
        binding.btnFire.setOnClickListener { viewModel.fireRequest() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progress.isVisible = state.loading
                    binding.btnFire.isEnabled = !state.loading
                    binding.tvRequest.text = state.probedSummary.ifEmpty {
                        getString(R.string.feature_network_interceptor_request_placeholder)
                    }
                    binding.tvResponse.text = state.responseSummary.ifEmpty {
                        getString(R.string.feature_network_interceptor_response_placeholder)
                    }
                    binding.tvError.isVisible = state.error != null
                    binding.tvError.text = state.error.orEmpty()
                    binding.tvMetrics.isVisible = state.metricsText.isNotBlank()
                    binding.tvMetrics.text = state.metricsText
                }
            }
        }
    }
}

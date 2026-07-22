package com.sys.androidkit.feature.animation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.animation.databinding.FragmentLottieBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LottieLabFragment : BaseFragment<FragmentLottieBinding>() {

    private val viewModel: LottieLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentLottieBinding = FragmentLottieBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.lottieView.setAnimation(R.raw.androidkit_bounce)
        binding.lottieView.playAnimation()
        binding.btnToggle.setOnClickListener { viewModel.togglePlay() }
        binding.btnSpeed.setOnClickListener { viewModel.cycleSpeed() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvStatus.text = state.status
                    binding.lottieView.speed = state.speed
                    if (state.playing) {
                        if (!binding.lottieView.isAnimating) {
                            binding.lottieView.resumeAnimation()
                        }
                    } else {
                        binding.lottieView.pauseAnimation()
                    }
                    binding.btnToggle.text = getString(
                        if (state.playing) {
                            R.string.feature_animation_lottie_pause
                        } else {
                            R.string.feature_animation_lottie_play
                        },
                    )
                }
            }
        }
    }
}

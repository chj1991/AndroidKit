package com.sys.androidkit.feature.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.animation.databinding.FragmentAnimationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnimationLabFragment : BaseFragment<FragmentAnimationBinding>() {

    private val viewModel: AnimationLabViewModel by viewModels()
    private var runningAnimator: android.animation.Animator? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAnimationBinding = FragmentAnimationBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnPulse.setOnClickListener { playPulse() }
        binding.btnSlide.setOnClickListener { playSlide() }
        binding.btnValue.setOnClickListener { playValue() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvValue.text = getString(
                        R.string.feature_animation_value_label,
                        state.valueLabel,
                    ) + "\n" + state.lastAction
                }
            }
        }
    }

    override fun onDestroyView() {
        runningAnimator?.cancel()
        runningAnimator = null
        super.onDestroyView()
    }

    private fun playPulse() {
        cancelRunning()
        val scaleX = ObjectAnimator.ofFloat(binding.animBox, "scaleX", 1f, 1.35f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.animBox, "scaleY", 1f, 1.35f, 1f)
        val alpha = ObjectAnimator.ofFloat(binding.animBox, "alpha", 1f, 0.4f, 1f)
        runningAnimator = AnimatorSet().apply {
            playTogether(scaleX, scaleY, alpha)
            duration = 700
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        viewModel.onAction("AnimatorSet：scale + alpha")
    }

    private fun playSlide() {
        cancelRunning()
        runningAnimator = ObjectAnimator.ofFloat(binding.animBox, "translationX", 0f, 180f, 0f).apply {
            duration = 800
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        viewModel.onAction("ObjectAnimator：translationX")
    }

    private fun playValue() {
        cancelRunning()
        runningAnimator = ValueAnimator.ofInt(0, 100).apply {
            duration = 1000
            addUpdateListener { animator ->
                viewModel.onValue(animator.animatedValue as Int)
            }
            start()
        }
        viewModel.onAction("ValueAnimator：0 → 100")
    }

    private fun cancelRunning() {
        runningAnimator?.cancel()
        binding.animBox.scaleX = 1f
        binding.animBox.scaleY = 1f
        binding.animBox.alpha = 1f
        binding.animBox.translationX = 0f
    }
}

package com.sys.androidkit.feature.animation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.animation.databinding.FragmentMotionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MotionLabFragment : BaseFragment<FragmentMotionBinding>() {

    private val viewModel: MotionLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMotionBinding = FragmentMotionBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.motionLayout.setTransitionListener(
            object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) = Unit

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float,
                ) = Unit

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    viewModel.onProgress(currentId == R.id.end)
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float,
                ) = Unit
            },
        )
        binding.btnToggle.setOnClickListener {
            if (binding.motionLayout.currentState == R.id.start) {
                binding.motionLayout.transitionToEnd()
            } else {
                binding.motionLayout.transitionToStart()
            }
        }
        viewModel.onProgress(binding.motionLayout.currentState == R.id.end)
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { binding.tvStatus.text = it.status }
            }
        }
    }
}

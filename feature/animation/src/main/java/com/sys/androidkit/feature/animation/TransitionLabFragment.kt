package com.sys.androidkit.feature.animation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.transition.AutoTransition
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.animation.databinding.FragmentTransitionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransitionLabFragment : BaseFragment<FragmentTransitionBinding>() {

    private val viewModel: TransitionLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentTransitionBinding = FragmentTransitionBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnExpand.setOnClickListener { viewModel.toggleExpand() }
        binding.btnScene.setOnClickListener { viewModel.toggleScene() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.map { it.lastAction }.distinctUntilChanged().collect {
                        binding.tvStatus.text = it
                    }
                }
                launch {
                    viewModel.uiState.map { it.expanded }.distinctUntilChanged().collect { expanded ->
                        applyExpand(expanded)
                    }
                }
                launch {
                    viewModel.uiState.map { it.sceneB }.distinctUntilChanged().collect { sceneB ->
                        applyScene(sceneB)
                    }
                }
            }
        }
    }

    private fun applyExpand(expanded: Boolean) {
        val set = TransitionSet().apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            addTransition(ChangeBounds())
            addTransition(Fade())
            duration = 280
        }
        TransitionManager.beginDelayedTransition(binding.expandContainer, set)
        binding.detailPanel.visibility = if (expanded) View.VISIBLE else View.GONE
        binding.btnExpand.text = getString(
            if (expanded) {
                R.string.feature_animation_transition_collapse
            } else {
                R.string.feature_animation_transition_expand
            },
        )
    }

    private fun applyScene(sceneB: Boolean) {
        TransitionManager.beginDelayedTransition(
            binding.sceneContainer,
            AutoTransition().setDuration(320),
        )
        binding.sceneA.visibility = if (sceneB) View.GONE else View.VISIBLE
        binding.sceneB.visibility = if (sceneB) View.VISIBLE else View.GONE
    }
}

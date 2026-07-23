package com.sys.androidkit.feature.charts

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.Chart
import com.google.android.material.chip.Chip
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.charts.databinding.FragmentChartLabBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChartLabFragment : BaseFragment<FragmentChartLabBinding>() {

    private val viewModel: ChartLabViewModel by viewModels()
    private var currentChart: Chart<*>? = null
    private val chipByType = linkedMapOf<ChartType, Chip>()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentChartLabBinding = FragmentChartLabBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        ChartType.entries.forEach { type ->
            val chip = Chip(
                ContextThemeWrapper(
                    requireContext(),
                    com.google.android.material.R.style.Widget_Material3_Chip_Filter,
                ),
            ).apply {
                text = type.title
                isCheckable = true
                isChecked = type == ChartType.LINE
                setOnClickListener { viewModel.setType(type) }
            }
            chipByType[type] = chip
            binding.chipGroupType.addView(chip)
        }

        binding.btnRefresh.setOnClickListener { viewModel.refreshData() }
        binding.btnAnimate.setOnClickListener { viewModel.replayAnimation() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState
                        .map { it.type to it.seed }
                        .distinctUntilChanged()
                        .collect { (type, seed) ->
                            chipByType.forEach { (t, chip) -> chip.isChecked = t == type }
                            binding.tvSummary.text = "${type.title} · ${type.summary}"
                            renderChart(type, seed)
                        }
                }
                launch {
                    viewModel.uiState
                        .map { it.animateToken }
                        .distinctUntilChanged()
                        .collect { token ->
                            if (token > 0) {
                                currentChart?.animateY(800, Easing.EaseInOutQuad)
                            }
                        }
                }
            }
        }
    }

    private fun renderChart(type: ChartType, seed: Int) {
        binding.chartContainer.removeAllViews()
        val chart = ChartSampleFactory.create(requireContext(), type, seed)
        currentChart = chart
        binding.chartContainer.addView(chart)
    }
}

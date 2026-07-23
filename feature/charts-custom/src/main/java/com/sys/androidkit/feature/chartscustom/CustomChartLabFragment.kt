package com.sys.androidkit.feature.chartscustom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.chartscustom.databinding.FragmentCustomChartLabBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomChartLabFragment : BaseFragment<FragmentCustomChartLabBinding>() {

    private val viewModel: CustomChartLabViewModel by viewModels()
    private var currentChart: BaseChartView? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCustomChartLabBinding =
        FragmentCustomChartLabBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.chipLine.setOnClickListener { viewModel.setType(CustomChartType.LINE) }
        binding.chipBar.setOnClickListener { viewModel.setType(CustomChartType.BAR) }
        binding.chipPie.setOnClickListener { viewModel.setType(CustomChartType.PIE) }
        binding.chipRadar.setOnClickListener { viewModel.setType(CustomChartType.RADAR) }
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
                            binding.chipLine.isChecked = type == CustomChartType.LINE
                            binding.chipBar.isChecked = type == CustomChartType.BAR
                            binding.chipPie.isChecked = type == CustomChartType.PIE
                            binding.chipRadar.isChecked = type == CustomChartType.RADAR
                            binding.tvSummary.text = "${type.title} · ${type.summary}"
                            render(type, seed)
                        }
                }
                launch {
                    viewModel.uiState
                        .map { it.animateToken }
                        .distinctUntilChanged()
                        .collect { token ->
                            if (token > 0) currentChart?.replayAnimation()
                        }
                }
            }
        }
    }

    private fun render(type: CustomChartType, seed: Int) {
        binding.chartContainer.removeAllViews()
        val chart: BaseChartView = when (type) {
            CustomChartType.LINE -> LineChartView(requireContext()).also {
                it.series = CustomChartSampleFactory.lineSeries(seed)
            }
            CustomChartType.BAR -> BarChartView(requireContext()).also {
                it.points = CustomChartSampleFactory.barPoints(seed)
            }
            CustomChartType.PIE -> PieChartView(requireContext()).also {
                it.slices = CustomChartSampleFactory.pieSlices(seed)
                it.centerText = "份额"
                it.holeRatio = 0.55f
            }
            CustomChartType.RADAR -> RadarChartView(requireContext()).also {
                val (labels, series) = CustomChartSampleFactory.radar(seed)
                it.labels = labels
                it.series = series
            }
        }
        chart.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
        currentChart = chart
        binding.chartContainer.addView(chart)
        chart.replayAnimation()
    }
}

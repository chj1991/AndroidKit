package com.sys.androidkit.feature.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.core.ui.ext.showConfirmDialog
import com.sys.androidkit.core.ui.widget.DemoState
import com.sys.androidkit.feature.storage.databinding.FragmentPagingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PagingLabFragment : BaseFragment<FragmentPagingBinding>() {

    private val viewModel: PagingLabViewModel by viewModels()
    private val adapter = PagingNotesAdapter()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPagingBinding = FragmentPagingBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.stateView.contentContainer.removeAllViews()
        val list = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@PagingLabFragment.adapter
            clipToPadding = false
        }
        binding.stateView.contentContainer.addView(
            list,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            ),
        )
        binding.stateView.setOnRetry { viewModel.seedBulk() }
        binding.btnSeed.setOnClickListener { viewModel.seedBulk() }
        binding.btnClear.setOnClickListener {
            showConfirmDialog(
                title = getString(R.string.feature_storage_paging_clear_title),
                message = getString(R.string.feature_storage_paging_clear_message),
                positive = getString(R.string.feature_storage_room_clear_all),
                onPositive = { viewModel.clearNotes() },
            )
        }
        binding.btnRefreshMeta.setOnClickListener { viewModel.refreshMeta() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.pagingFlow.collectLatest { adapter.submitData(it) }
                }
                launch {
                    viewModel.meta.collect { meta ->
                        binding.tvMeta.text = getString(
                            R.string.feature_storage_paging_meta,
                            meta.totalNotes,
                            meta.message,
                        )
                    }
                }
                launch {
                    adapter.loadStateFlow.collect { loadStates ->
                        val refresh = loadStates.refresh
                        val isEmpty = refresh is LoadState.NotLoading && adapter.itemCount == 0
                        when {
                            refresh is LoadState.Loading && adapter.itemCount == 0 -> {
                                binding.stateView.render(DemoState.Loading)
                            }
                            refresh is LoadState.Error -> {
                                binding.stateView.render(
                                    DemoState.Error(
                                        refresh.error.message ?: "Paging 加载失败",
                                    ),
                                )
                            }
                            isEmpty -> {
                                binding.stateView.render(
                                    DemoState.Empty("暂无数据，点「种子 80 条」后滚动"),
                                )
                            }
                            else -> binding.stateView.render(DemoState.Content)
                        }
                    }
                }
            }
        }
    }
}

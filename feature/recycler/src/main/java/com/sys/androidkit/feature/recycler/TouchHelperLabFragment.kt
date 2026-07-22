package com.sys.androidkit.feature.recycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.recycler.databinding.FragmentTouchHelperBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TouchHelperLabFragment : BaseFragment<FragmentTouchHelperBinding>() {

    private val viewModel: TouchHelperLabViewModel by viewModels()
    private val adapter = TouchItemsAdapter()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentTouchHelperBinding = FragmentTouchHelperBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnReset.setOnClickListener { viewModel.reset() }
        binding.rvItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItems.adapter = adapter
        binding.rvItems.addItemDecoration(
            SpaceDividerDecoration(
                verticalSpacePx = (8 * resources.displayMetrics.density).toInt(),
                dividerColor = Color.parseColor("#22000000"),
            ),
        )
        val helper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT,
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {
                    val from = viewHolder.bindingAdapterPosition
                    val to = target.bindingAdapterPosition
                    adapter.move(from, to)
                    viewModel.replaceAll(adapter.snapshot())
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val index = viewHolder.bindingAdapterPosition
                    adapter.removeAt(index)
                    viewModel.replaceAll(adapter.snapshot(), "侧滑删除完成，剩余 ${adapter.itemCount} 项")
                }
            },
        )
        helper.attachToRecyclerView(binding.rvItems)
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.map { it.message }.distinctUntilChanged().collect {
                        binding.tvMessage.text = it
                    }
                }
                launch {
                    viewModel.uiState.map { it.items }.distinctUntilChanged().collect { items ->
                        if (adapter.snapshot() != items) {
                            adapter.submit(items)
                        }
                    }
                }
            }
        }
    }
}

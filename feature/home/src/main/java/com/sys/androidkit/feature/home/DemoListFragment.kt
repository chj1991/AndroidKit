package com.sys.androidkit.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.home.databinding.FragmentDemoListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DemoListFragment : BaseFragment<FragmentDemoListBinding>() {

    private val viewModel: DemoListViewModel by viewModels()

    private val adapter = DemoAdapter(
        onClick = { demo ->
            viewModel.recordRecent(demo.id)
            val request = NavDeepLinkRequest.Builder
                .fromUri("androidkit://demo/${demo.id}".toUri())
                .build()
            findNavController().navigate(request)
        },
        onFavoriteClick = { demo -> viewModel.toggleFavorite(demo.id) },
    )

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDemoListBinding = FragmentDemoListBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.rvDemos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDemos.adapter = adapter
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.toolbar.title = state.title.ifEmpty {
                        getString(R.string.feature_home_title)
                    }
                    adapter.submitList(state.rows)
                }
            }
        }
    }

    companion object {
        const val ARG_CATEGORY_ID = "categoryId"
    }
}

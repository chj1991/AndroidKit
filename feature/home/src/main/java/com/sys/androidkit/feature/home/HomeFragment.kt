package com.sys.androidkit.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    private val adapter = HomeListAdapter(
        onCategoryClick = { item ->
            navigate("androidkit://category/${item.data.id}")
        },
        onDemoClick = { item ->
            viewModel.recordRecent(item.data.id)
            navigate("androidkit://demo/${item.data.id}")
        },
    )

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.inflateMenu(R.menu.menu_home)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_favorites -> {
                    navigate("androidkit://favorites")
                    true
                }
                R.id.action_settings -> {
                    navigate("androidkit://settings")
                    true
                }
                else -> false
            }
        }
        binding.rvContent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContent.adapter = adapter
        binding.etSearch.doAfterTextChanged { editable ->
            viewModel.onQueryChanged(editable?.toString().orEmpty())
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.items)
                    binding.tvEmpty.isVisible = state.isEmpty
                    binding.rvContent.isVisible = !state.isEmpty
                }
            }
        }
    }

    private fun navigate(uri: String) {
        val request = NavDeepLinkRequest.Builder.fromUri(uri.toUri()).build()
        findNavController().navigate(request)
    }
}

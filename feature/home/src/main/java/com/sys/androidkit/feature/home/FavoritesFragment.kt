package com.sys.androidkit.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.home.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {

    private val viewModel: FavoritesViewModel by viewModels()

    private val favoritesAdapter = DemoAdapter(
        onClick = { demo -> openDemo(demo.id) },
        onFavoriteClick = { demo -> viewModel.toggleFavorite(demo.id) },
    )

    private val recentAdapter = DemoAdapter(
        onClick = { demo -> openDemo(demo.id) },
        onFavoriteClick = { demo -> viewModel.toggleFavorite(demo.id) },
    )

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFavoritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.adapter = favoritesAdapter
        binding.rvRecent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecent.adapter = recentAdapter
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val favoriteIds = state.favorites.map { it.id }.toSet()
                    favoritesAdapter.submitDemos(state.favorites, favoriteIds)
                    recentAdapter.submitDemos(state.recent, favoriteIds)
                    binding.tvFavoritesEmpty.isVisible = state.favorites.isEmpty()
                    binding.tvRecentEmpty.isVisible = state.recent.isEmpty()
                }
            }
        }
    }

    private fun openDemo(demoId: String) {
        viewModel.openRecorded(demoId)
        val request = NavDeepLinkRequest.Builder
            .fromUri("androidkit://demo/$demoId".toUri())
            .build()
        findNavController().navigate(request)
    }
}

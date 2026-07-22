package com.sys.androidkit.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.home.databinding.FragmentDemoListBinding

class DemoListFragment : BaseFragment<FragmentDemoListBinding>() {

    private val adapter = DemoAdapter { demo ->
        val request = NavDeepLinkRequest.Builder
            .fromUri("androidkit://demo/${demo.id}".toUri())
            .build()
        findNavController().navigate(request)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDemoListBinding = FragmentDemoListBinding.inflate(inflater, container, false)

    override fun initView() {
        val categoryId = requireArguments().getString(ARG_CATEGORY_ID).orEmpty()
        val category = DemoCatalog.findCategory(categoryId)
        binding.toolbar.title = category?.title ?: getString(R.string.feature_home_title)
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.rvDemos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDemos.adapter = adapter
        adapter.submitList(category?.demos.orEmpty())
    }

    companion object {
        const val ARG_CATEGORY_ID = "categoryId"
    }
}

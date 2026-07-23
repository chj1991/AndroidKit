package com.sys.androidkit.feature.basicui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.basicui.databinding.FragmentBasicUiShowcaseBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * deep link 参数 componentId；无 Safe Args 时从 arguments 读取。
 */
@AndroidEntryPoint
class BasicUiShowcaseFragment : BaseFragment<FragmentBasicUiShowcaseBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentBasicUiShowcaseBinding =
        FragmentBasicUiShowcaseBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        val componentId = arguments?.getString("componentId").orEmpty()
        val entry = BasicUiCatalog.find(componentId)
        binding.toolbar.title = entry?.title ?: componentId
        binding.tvWiki.text = if (entry != null) {
            getString(R.string.feature_basic_ui_wiki, entry.wiki)
        } else {
            "unknown componentId=$componentId"
        }

        binding.demoContainer.removeAllViews()
        if (componentId.isNotBlank()) {
            binding.demoContainer.addView(BasicUiDemoFactory.create(this, componentId))
        }
    }

    override fun initObserver() = Unit
}

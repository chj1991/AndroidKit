package com.sys.androidkit.feature.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.components.databinding.FragmentComponentShowcaseBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 使用 deep link 参数 componentId；无 Safe Args 插件时从 arguments 读取。
 */
@AndroidEntryPoint
class ComponentShowcaseFragment : BaseFragment<FragmentComponentShowcaseBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentComponentShowcaseBinding =
        FragmentComponentShowcaseBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        val componentId = arguments?.getString("componentId").orEmpty()
        val entry = ComponentCatalog.find(componentId)
        binding.toolbar.title = entry?.title ?: componentId
        binding.tvMeta.text = if (entry != null) {
            getString(
                R.string.feature_components_meta,
                entry.className,
                when (entry.group) {
                    ComponentGroup.SYSTEM -> getString(R.string.feature_components_group_system)
                    ComponentGroup.MATERIAL3 -> getString(R.string.feature_components_group_m3)
                },
            )
        } else {
            "unknown componentId=$componentId"
        }

        binding.demoContainer.removeAllViews()
        if (componentId.isNotBlank()) {
            binding.demoContainer.addView(ComponentDemoFactory.create(this, componentId))
        }
    }

    override fun initObserver() = Unit
}

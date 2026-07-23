package com.sys.androidkit.feature.androidktx

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.androidktx.databinding.FragmentAndroidKtxShowcaseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AndroidKtxShowcaseFragment : BaseFragment<FragmentAndroidKtxShowcaseBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAndroidKtxShowcaseBinding =
        FragmentAndroidKtxShowcaseBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        val componentId = arguments?.getString("componentId").orEmpty()
        val entry = AndroidKtxCatalog.find(componentId)
        binding.toolbar.title = entry?.title ?: componentId
        binding.tvSection.text = if (entry != null) {
            getString(R.string.feature_android_ktx_section, entry.section)
        } else {
            "unknown componentId=$componentId"
        }

        binding.demoContainer.removeAllViews()
        if (componentId.isNotBlank()) {
            binding.demoContainer.addView(AndroidKtxDemoFactory.create(this, componentId))
        }
    }

    override fun initObserver() = Unit
}

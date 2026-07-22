package com.sys.androidkit.feature.image

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import coil.load
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.image.databinding.FragmentCustomImagePickerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 结果页：配置可选张数，打开独立选择界面；确认后返回展示。
 */
@AndroidEntryPoint
class CustomImagePickerLabFragment : BaseFragment<FragmentCustomImagePickerBinding>() {

    private val viewModel: CustomImagePickerLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCustomImagePickerBinding =
        FragmentCustomImagePickerBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CustomImagePickerContract.REQUEST_KEY) { _, bundle ->
            val uris = bundle.getStringArrayList(CustomImagePickerContract.KEY_URIS)
                ?.map { it.toUri() }
                .orEmpty()
            viewModel.onPickedResult(uris)
        }
    }

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.etMaxCount.doAfterTextChanged { text ->
            viewModel.onMaxCountInputChanged(text?.toString().orEmpty())
        }
        binding.btnApplyMax.setOnClickListener { viewModel.applyMaxCount() }
        binding.btnOpenPicker.setOnClickListener {
            viewModel.applyMaxCount()
            val max = viewModel.resolveMaxCount()
            val request = NavDeepLinkRequest.Builder
                .fromUri("${CustomImagePickerContract.SELECT_DEEP_LINK}/$max".toUri())
                .build()
            findNavController().navigate(request)
        }
        binding.btnClear.setOnClickListener { viewModel.clearResult() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvMode.text = state.modeLabel
                    binding.tvStatus.text = state.status
                    bindPreview(state.confirmedPreview)
                }
            }
        }
    }

    private fun bindPreview(uris: List<Uri>) {
        if (uris.isEmpty()) {
            binding.ivPreview.setImageDrawable(null)
            binding.tvPreviewMeta.text = getString(R.string.feature_image_custom_preview_empty)
            return
        }
        binding.ivPreview.load(uris.first()) {
            crossfade(true)
            placeholder(R.drawable.ic_image_placeholder)
            error(R.drawable.ic_image_error)
        }
        binding.tvPreviewMeta.text = getString(
            R.string.feature_image_custom_preview_meta,
            uris.size,
            uris.joinToString("\n") { it.toString() },
        )
    }
}

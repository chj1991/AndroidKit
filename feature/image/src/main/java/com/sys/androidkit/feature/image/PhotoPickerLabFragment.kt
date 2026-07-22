package com.sys.androidkit.feature.image

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.image.databinding.FragmentPhotoPickerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Android Photo Picker Demo：
 * - 优先系统选择器，一般不需要 READ_MEDIA_* / READ_EXTERNAL_STORAGE
 * - 用 Activity Result API 拿 content Uri，再用 Coil 预览
 */
@AndroidEntryPoint
class PhotoPickerLabFragment : BaseFragment<FragmentPhotoPickerBinding>() {

    private val viewModel: PhotoPickerLabViewModel by viewModels()
    private var loadedUri: Uri? = UNSET_URI

    private val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            viewModel.onPicked(uri)
        } else {
            viewModel.onCancelled()
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPhotoPickerBinding = FragmentPhotoPickerBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        viewModel.onAvailability(PickVisualMedia.isPhotoPickerAvailable(requireContext()))
        binding.btnPick.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
        binding.btnClear.setOnClickListener { viewModel.clear() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.map { it.status }.distinctUntilChanged().collect { status ->
                        binding.tvStatus.text = status
                    }
                }
                launch {
                    viewModel.uiState.map { it.imageUri }.distinctUntilChanged().collect { uri ->
                        if (uri == loadedUri) return@collect
                        loadedUri = uri
                        if (uri == null) {
                            binding.ivPreview.setImageDrawable(null)
                        } else {
                            binding.ivPreview.load(uri) {
                                crossfade(true)
                                placeholder(R.drawable.ic_image_placeholder)
                                error(R.drawable.ic_image_error)
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val UNSET_URI: Uri = Uri.parse("androidkit://unset")
    }
}

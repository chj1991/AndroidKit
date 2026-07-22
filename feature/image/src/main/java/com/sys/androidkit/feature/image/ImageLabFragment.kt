package com.sys.androidkit.feature.image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.image.databinding.FragmentImageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImageLabFragment : BaseFragment<FragmentImageBinding>() {

    private val viewModel: ImageLabViewModel by viewModels()
    private var loadedUrl: String? = UNSET

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentImageBinding = FragmentImageBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnLoadSuccess.setOnClickListener { viewModel.loadSuccess() }
        binding.btnLoadFail.setOnClickListener { viewModel.loadFail() }
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
                    viewModel.uiState.map { it.imageUrl }.distinctUntilChanged().collect { url ->
                        if (url == loadedUrl) return@collect
                        loadedUrl = url
                        if (url == null) {
                            binding.ivPreview.setImageDrawable(null)
                            return@collect
                        }
                        binding.ivPreview.load(url) {
                            crossfade(true)
                            placeholder(R.drawable.ic_image_placeholder)
                            error(R.drawable.ic_image_error)
                            listener(
                                onSuccess = { _: ImageRequest, _: SuccessResult ->
                                    viewModel.onLoadResult(true, "加载成功（可走内存/磁盘缓存）")
                                },
                                onError = { _: ImageRequest, _: ErrorResult ->
                                    viewModel.onLoadResult(false, "加载失败，已显示 error 占位图")
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val UNSET = "__unset__"
    }
}

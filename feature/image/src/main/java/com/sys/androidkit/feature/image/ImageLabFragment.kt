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
import coil.size.Size
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.image.databinding.FragmentImageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImageLabFragment : BaseFragment<FragmentImageBinding>() {

    private val viewModel: ImageLabViewModel by viewModels()
    private var lastToken: Int = -1

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentImageBinding = FragmentImageBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.chipStyleNone.setOnClickListener { viewModel.setStyle(ImageStyle.NONE) }
        binding.chipStyleRounded.setOnClickListener { viewModel.setStyle(ImageStyle.ROUNDED) }
        binding.chipStyleCircle.setOnClickListener { viewModel.setStyle(ImageStyle.CIRCLE) }
        binding.chipStyleGray.setOnClickListener { viewModel.setStyle(ImageStyle.GRAYSCALE) }
        binding.chipStyleRoundedGray.setOnClickListener { viewModel.setStyle(ImageStyle.ROUNDED_GRAY) }

        binding.chipSizeView.setOnClickListener { viewModel.setSizeMode(ImageSizeMode.VIEW_SIZE) }
        binding.chipSizeDownsample.setOnClickListener { viewModel.setSizeMode(ImageSizeMode.DOWNSAMPLE) }
        binding.chipSizeOriginal.setOnClickListener { viewModel.setSizeMode(ImageSizeMode.ORIGINAL) }

        binding.btnLoadSuccess.setOnClickListener { viewModel.loadSuccess() }
        binding.btnLoadFail.setOnClickListener { viewModel.loadFail() }
        binding.btnClear.setOnClickListener {
            viewModel.clear()
            binding.ivPreview.load(null as String?) {
                fallback(R.drawable.ic_image_placeholder)
            }
        }
        binding.btnReloadStyle.setOnClickListener {
            // 样式/尺寸变更后重新加载同一 URL，观察 Transformation 缓存键差异
            if (viewModel.uiState.value.imageUrl != null && !viewModel.uiState.value.forceError) {
                viewModel.loadSuccess()
            } else {
                viewModel.onLoadResult("请先加载成功图，再切换样式后点「按当前样式重载」")
            }
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.map { it.status }.distinctUntilChanged().collect {
                        binding.tvStatus.text = it
                    }
                }
                launch {
                    viewModel.uiState.map { it.style }.distinctUntilChanged().collect { style ->
                        binding.chipStyleNone.isChecked = style == ImageStyle.NONE
                        binding.chipStyleRounded.isChecked = style == ImageStyle.ROUNDED
                        binding.chipStyleCircle.isChecked = style == ImageStyle.CIRCLE
                        binding.chipStyleGray.isChecked = style == ImageStyle.GRAYSCALE
                        binding.chipStyleRoundedGray.isChecked = style == ImageStyle.ROUNDED_GRAY
                    }
                }
                launch {
                    viewModel.uiState.map { it.sizeMode }.distinctUntilChanged().collect { mode ->
                        binding.chipSizeView.isChecked = mode == ImageSizeMode.VIEW_SIZE
                        binding.chipSizeDownsample.isChecked = mode == ImageSizeMode.DOWNSAMPLE
                        binding.chipSizeOriginal.isChecked = mode == ImageSizeMode.ORIGINAL
                    }
                }
                launch {
                    viewModel.uiState
                        .map { Triple(it.loadToken, it.style, it.sizeMode to it.imageUrl) }
                        .distinctUntilChanged()
                        .collect { (token, style, sizeAndUrl) ->
                            val (sizeMode, url) = sizeAndUrl
                            if (token == 0 || token == lastToken) return@collect
                            lastToken = token
                            if (url == null) {
                                binding.ivPreview.setImageDrawable(null)
                                return@collect
                            }
                            loadImage(url, style, sizeMode)
                        }
                }
            }
        }
    }

    private fun loadImage(url: String, style: ImageStyle, sizeMode: ImageSizeMode) {
        val density = resources.displayMetrics.density
        val cornerPx = 20f * density
        val transforms = buildTransformations(style, cornerPx)
        val styleLabel = style.name
        val sizeLabel = sizeMode.name

        binding.ivPreview.load(url) {
            crossfade(300)
            placeholder(R.drawable.ic_image_placeholder)
            error(R.drawable.ic_image_error)
            fallback(R.drawable.ic_image_placeholder)
            when (sizeMode) {
                ImageSizeMode.VIEW_SIZE -> Unit // 默认按 View 尺寸采样
                ImageSizeMode.DOWNSAMPLE -> size(Size(240, 150))
                ImageSizeMode.ORIGINAL -> size(Size.ORIGINAL)
            }
            if (transforms.isNotEmpty()) {
                transformations(transforms)
            }
            listener(
                onSuccess = { _: ImageRequest, result: SuccessResult ->
                    val drawable = result.drawable
                    val wh = "${drawable.intrinsicWidth}x${drawable.intrinsicHeight}"
                    viewModel.onLoadResult(
                        "成功 · style=$styleLabel · size=$sizeLabel · " +
                            "drawable=$wh · dataSource=${result.dataSource}",
                    )
                },
                onError = { _: ImageRequest, result: ErrorResult ->
                    viewModel.onLoadResult(
                        "失败 · 已显示 error 占位 · ${result.throwable.message ?: "unknown"}",
                    )
                },
            )
        }
    }

    private fun buildTransformations(style: ImageStyle, cornerPx: Float): List<Transformation> {
        return when (style) {
            ImageStyle.NONE -> emptyList()
            ImageStyle.ROUNDED -> listOf(RoundedCornersTransformation(cornerPx))
            ImageStyle.CIRCLE -> listOf(CircleCropTransformation())
            ImageStyle.GRAYSCALE -> listOf(GrayscaleTransformation())
            ImageStyle.ROUNDED_GRAY -> listOf(
                GrayscaleTransformation(),
                RoundedCornersTransformation(cornerPx),
            )
        }
    }
}

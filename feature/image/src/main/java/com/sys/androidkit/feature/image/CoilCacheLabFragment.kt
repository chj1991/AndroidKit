package com.sys.androidkit.feature.image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.image.databinding.FragmentCoilCacheBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoilCacheLabFragment : BaseFragment<FragmentCoilCacheBinding>() {

    private val viewModel: CoilCacheLabViewModel by viewModels()
    private var lastToken: Int = -1

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCoilCacheBinding = FragmentCoilCacheBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.chipDefault.setOnClickListener { viewModel.setMode(CoilCacheMode.DEFAULT) }
        binding.chipMemory.setOnClickListener { viewModel.setMode(CoilCacheMode.MEMORY_ONLY) }
        binding.chipDisk.setOnClickListener { viewModel.setMode(CoilCacheMode.DISK_ONLY) }
        binding.chipDisabled.setOnClickListener { viewModel.setMode(CoilCacheMode.DISABLED) }
        binding.btnLoad.setOnClickListener { viewModel.reload() }
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
                    viewModel.uiState.map { it.mode }.distinctUntilChanged().collect { mode ->
                        binding.chipDefault.isChecked = mode == CoilCacheMode.DEFAULT
                        binding.chipMemory.isChecked = mode == CoilCacheMode.MEMORY_ONLY
                        binding.chipDisk.isChecked = mode == CoilCacheMode.DISK_ONLY
                        binding.chipDisabled.isChecked = mode == CoilCacheMode.DISABLED
                    }
                }
                launch {
                    viewModel.uiState
                        .map { Triple(it.loadToken, it.mode, it.imageUrl) }
                        .distinctUntilChanged()
                        .collect { (token, mode, url) ->
                            if (token == 0 || token == lastToken) return@collect
                            lastToken = token
                            loadImage(url, mode)
                        }
                }
            }
        }
    }

    private fun loadImage(url: String, mode: CoilCacheMode) {
        val (memory, disk) = when (mode) {
            CoilCacheMode.DEFAULT -> CachePolicy.ENABLED to CachePolicy.ENABLED
            CoilCacheMode.MEMORY_ONLY -> CachePolicy.ENABLED to CachePolicy.DISABLED
            CoilCacheMode.DISK_ONLY -> CachePolicy.DISABLED to CachePolicy.ENABLED
            CoilCacheMode.DISABLED -> CachePolicy.DISABLED to CachePolicy.DISABLED
        }
        binding.ivPreview.load(url) {
            crossfade(true)
            memoryCachePolicy(memory)
            diskCachePolicy(disk)
            placeholder(R.drawable.ic_image_placeholder)
            error(R.drawable.ic_image_error)
            listener(
                onSuccess = { _: ImageRequest, result: SuccessResult ->
                    val fromMemory = result.dataSource.name.contains("MEMORY", ignoreCase = true)
                    viewModel.onLoadFinished(
                        fromMemory = fromMemory,
                        message = "加载成功 dataSource=${result.dataSource}",
                    )
                },
                onError = { _: ImageRequest, result: ErrorResult ->
                    viewModel.onLoadFinished(
                        fromMemory = null,
                        message = "加载失败：${result.throwable.message}",
                    )
                },
            )
        }
    }
}

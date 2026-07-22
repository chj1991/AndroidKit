package com.sys.androidkit.feature.image

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.image.databinding.FragmentCustomImagePickerSelectBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * 独立选择界面：确认后通过 Fragment Result 返回上一页。
 */
@AndroidEntryPoint
class CustomImagePickerSelectFragment : BaseFragment<FragmentCustomImagePickerSelectBinding>() {

    private val viewModel: CustomImagePickerSelectViewModel by viewModels()
    private val adapter = GalleryImageAdapter { viewModel.toggle(it) }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        viewModel.onPermissionChanged(granted)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCustomImagePickerSelectBinding =
        FragmentCustomImagePickerSelectBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.rvGallery.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvGallery.adapter = adapter
        binding.btnRequestPermission.setOnClickListener {
            permissionLauncher.launch(requiredPermission())
        }
        binding.btnReload.setOnClickListener {
            if (hasPermission()) {
                viewModel.loadGallery()
            } else {
                viewModel.onPermissionChanged(false)
            }
        }
        binding.btnConfirm.setOnClickListener {
            val uris = ArrayList(viewModel.currentSelectedUris().map { it.toString() })
            setFragmentResult(
                CustomImagePickerContract.REQUEST_KEY,
                bundleOf(CustomImagePickerContract.KEY_URIS to uris),
            )
            findNavController().navigateUp()
        }
        binding.btnCancel.setOnClickListener { findNavController().navigateUp() }
        viewModel.onPermissionChanged(hasPermission())
        if (!hasPermission()) {
            permissionLauncher.launch(requiredPermission())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onPermissionChanged(hasPermission())
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.map { it.images }.distinctUntilChanged().collect { rows ->
                        adapter.submitList(rows)
                    }
                }
                launch {
                    viewModel.uiState.collect { state ->
                        binding.tvMode.text = state.modeLabel
                        binding.tvStatus.text = state.status
                        binding.btnRequestPermission.isEnabled = !state.hasPermission
                        binding.btnConfirm.isEnabled = state.selectedUris.isNotEmpty()
                    }
                }
            }
        }
    }

    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            requiredPermission(),
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requiredPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }
}

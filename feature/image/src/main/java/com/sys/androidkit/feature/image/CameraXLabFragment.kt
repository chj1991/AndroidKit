package com.sys.androidkit.feature.image

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.image.databinding.FragmentCameraxBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * CameraX：Preview + ImageCapture，拍照写入 cacheDir。
 */
@AndroidEntryPoint
class CameraXLabFragment : BaseFragment<FragmentCameraxBinding>() {

    private val viewModel: CameraXLabViewModel by viewModels()
    private var imageCapture: ImageCapture? = null
    private var cameraExecutor: ExecutorService? = null
    private var loadedUri: Uri? = UNSET

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        viewModel.onPermissionChanged(granted)
        if (granted) bindCamera()
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCameraxBinding = FragmentCameraxBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.btnRequestPermission.setOnClickListener {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
        binding.btnCapture.setOnClickListener { takePhoto() }
        binding.btnClear.setOnClickListener { viewModel.clearCapture() }
        val granted = hasCameraPermission()
        viewModel.onPermissionChanged(granted)
        if (granted) {
            bindCamera()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
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
                    viewModel.uiState.map { it.hasPermission to it.bound }.distinctUntilChanged()
                        .collect { (hasPermission, bound) ->
                            binding.btnRequestPermission.isEnabled = !hasPermission
                            binding.btnCapture.isEnabled = hasPermission && bound
                        }
                }
                launch {
                    viewModel.uiState.map { it.capturedUri }.distinctUntilChanged().collect { uri ->
                        if (uri == loadedUri) return@collect
                        loadedUri = uri
                        if (uri == null) {
                            binding.ivCapture.setImageDrawable(null)
                        } else {
                            binding.ivCapture.load(uri) {
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

    override fun onDestroyView() {
        cameraExecutor?.shutdown()
        cameraExecutor = null
        imageCapture = null
        super.onDestroyView()
    }

    private fun bindCamera() {
        val future = ProcessCameraProvider.getInstance(requireContext())
        future.addListener(
            {
                try {
                    val provider = future.get()
                    val preview = Preview.Builder().build().also {
                        it.surfaceProvider = binding.previewView.surfaceProvider
                    }
                    val capture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()
                    imageCapture = capture
                    provider.unbindAll()
                    provider.bindToLifecycle(
                        viewLifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        capture,
                    )
                    viewModel.onBound(true, "已绑定 Preview + ImageCapture（后置）")
                } catch (t: Throwable) {
                    imageCapture = null
                    viewModel.onBound(false, "绑定失败：${t.message}")
                }
            },
            ContextCompat.getMainExecutor(requireContext()),
        )
    }

    private fun takePhoto() {
        val capture = imageCapture ?: run {
            viewModel.onCaptureError("ImageCapture 未就绪")
            return
        }
        val photoFile = File(
            requireContext().cacheDir,
            "camerax_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())}.jpg",
        )
        val options = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        val executor = cameraExecutor ?: ContextCompat.getMainExecutor(requireContext())
        capture.takePicture(
            options,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val uri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                    requireActivity().runOnUiThread { viewModel.onCaptured(uri) }
                }

                override fun onError(exception: ImageCaptureException) {
                    requireActivity().runOnUiThread {
                        viewModel.onCaptureError(exception.message ?: exception.toString())
                    }
                }
            },
        )
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val UNSET: Uri = Uri.parse("androidkit://unset")
    }
}

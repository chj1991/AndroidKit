package com.sys.androidkit.feature.system

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.system.databinding.FragmentShareBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShareLabFragment : BaseFragment<FragmentShareBinding>() {

    private val viewModel: ShareLabViewModel by viewModels()
    private var shareFile: File? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentShareBinding = FragmentShareBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnCreate.setOnClickListener { createShareFile() }
        binding.btnShare.setOnClickListener { shareFile() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvStatus.text = buildString {
                        append(state.message)
                        state.filePath?.let {
                            append("\n")
                            append(it)
                        }
                    }
                    binding.btnShare.isEnabled = state.fileReady
                }
            }
        }
    }

    private fun createShareFile() {
        runCatching {
            val dir = File(requireContext().cacheDir, "share").apply { mkdirs() }
            val file = File(dir, "androidkit-share.txt")
            file.writeText(
                """
                AndroidKit FileProvider Demo
                time=${System.currentTimeMillis()}
                不要用 file:// 分享，请用 content://（FileProvider）。
                """.trimIndent(),
            )
            shareFile = file
            viewModel.onFileCreated(file.absolutePath)
        }.onFailure {
            viewModel.onError("创建失败：${it.message}")
        }
    }

    private fun shareFile() {
        val file = shareFile
        if (file == null || !file.exists()) {
            viewModel.onError("请先创建分享文件")
            return
        }
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file,
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "AndroidKit Share")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.feature_system_share_chooser)))
        viewModel.onShared()
    }
}

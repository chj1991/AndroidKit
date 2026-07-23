package com.sys.androidkit.feature.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.lifecycle.databinding.FragmentRecreationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * LC-03：对比 Fragment 字段 / ViewModel / SavedStateHandle 在旋转与进程死亡下的表现。
 *
 * - Fragment 字段：任何重建都归零（本 Demo 故意不写入 Bundle）
 * - ViewModel：旋转保留，进程死亡丢失（sessionId 会变）
 * - SavedStateHandle：旋转与进程死亡均可恢复
 */
@AndroidEntryPoint
class RecreationLabFragment : BaseFragment<FragmentRecreationBinding>() {

    private val viewModel: RecreationLabViewModel by viewModels()

    /** 故意不写入 Bundle：重建后归零 */
    private var fragmentCounter = 0

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentRecreationBinding = FragmentRecreationBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.append(
            if (savedInstanceState == null) {
                "Fragment.onCreate savedInstanceState=null（冷启动）"
            } else {
                "Fragment.onCreate savedInstanceState!=null（配置变更 / 进程恢复）"
            },
        )
        viewModel.append("Fragment 字段重置为 $fragmentCounter（未做 Bundle 保存）")
    }

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.tvFragment.text = getString(
            R.string.feature_lifecycle_recreation_fragment,
            fragmentCounter,
        )
        binding.btnIncFragment.setOnClickListener {
            fragmentCounter++
            binding.tvFragment.text = getString(
                R.string.feature_lifecycle_recreation_fragment,
                fragmentCounter,
            )
            viewModel.append("Fragment field → $fragmentCounter")
        }
        binding.btnIncVm.setOnClickListener { viewModel.incrementVm() }
        binding.btnIncSaved.setOnClickListener { viewModel.incrementSaved() }
        binding.btnClear.setOnClickListener { viewModel.clearLogs() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvVm.text = getString(
                        R.string.feature_lifecycle_recreation_vm,
                        state.vmSessionId,
                        state.vmCounter,
                    )
                    binding.tvSaved.text = getString(
                        R.string.feature_lifecycle_recreation_saved,
                        state.savedCounter,
                    )
                    binding.tvLogs.text = state.logs.joinToString("\n")
                }
            }
        }
    }
}

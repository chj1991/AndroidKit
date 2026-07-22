package com.sys.androidkit.feature.image

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CustomImagePickerSelectUiState(
    val maxCount: Int = 1,
    val hasPermission: Boolean = false,
    val images: List<GalleryRow> = emptyList(),
    val selectedUris: List<Uri> = emptyList(),
    val status: String = "点选图片，确认后返回上一页",
) {
    val modeLabel: String
        get() = if (maxCount <= 1) "单选" else "多选（最多 $maxCount 张）"
}

@HiltViewModel
class CustomImagePickerSelectViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val maxCount: Int = (
        savedStateHandle.get<Int>(CustomImagePickerContract.ARG_MAX_COUNT) ?: 1
        ).coerceIn(1, MAX_ALLOWED)

    private val _uiState = MutableStateFlow(
        CustomImagePickerSelectUiState(maxCount = maxCount),
    )
    val uiState: StateFlow<CustomImagePickerSelectUiState> = _uiState.asStateFlow()

    private var selectedIds = linkedSetOf<Long>()
    private var cachedImages: List<GalleryImage> = emptyList()

    fun onPermissionChanged(granted: Boolean) {
        val wasGranted = _uiState.value.hasPermission
        _uiState.update { it.copy(hasPermission = granted) }
        if (granted) {
            if (!wasGranted || cachedImages.isEmpty()) {
                loadGallery()
            }
        } else {
            cachedImages = emptyList()
            selectedIds.clear()
            _uiState.update {
                it.copy(
                    images = emptyList(),
                    selectedUris = emptyList(),
                    status = "需要相册读取权限",
                )
            }
        }
    }

    fun loadGallery() {
        launch {
            _uiState.update { it.copy(status = "正在读取 MediaStore…") }
            cachedImages = GalleryMediaStore.loadImages(context)
            if (cachedImages.isEmpty()) {
                _uiState.update {
                    it.copy(
                        images = emptyList(),
                        selectedUris = emptyList(),
                        status = "相册为空，或尚未授予可读图片",
                    )
                }
            } else {
                publishRows()
                _uiState.update {
                    it.copy(status = "已加载 ${cachedImages.size} 张")
                }
            }
        }
    }

    fun toggle(image: GalleryImage) {
        if (selectedIds.contains(image.id)) {
            selectedIds.remove(image.id)
        } else if (maxCount <= 1) {
            selectedIds.clear()
            selectedIds.add(image.id)
        } else if (selectedIds.size < maxCount) {
            selectedIds.add(image.id)
        } else {
            _uiState.update { it.copy(status = "已达上限 $maxCount 张") }
            return
        }
        publishRows()
        _uiState.update { it.copy(status = "已选 ${selectedIds.size}/$maxCount") }
    }

    fun currentSelectedUris(): List<Uri> = _uiState.value.selectedUris

    private fun publishRows() {
        val orderMap = selectedIds.mapIndexed { index, id -> id to index + 1 }.toMap()
        val rows = cachedImages.map { image ->
            GalleryRow(
                image = image,
                selected = selectedIds.contains(image.id),
                order = orderMap[image.id],
            )
        }
        val uris = selectedIds.mapNotNull { id -> cachedImages.find { it.id == id }?.uri }
        _uiState.update { it.copy(images = rows, selectedUris = uris) }
    }

    companion object {
        private const val MAX_ALLOWED = 20
    }
}

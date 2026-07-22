package com.sys.androidkit.core.common.model

data class DemoCategory(
    val id: String,
    val title: String,
    val description: String,
    val demos: List<DemoItem>,
)

data class DemoItem(
    val id: String,
    val title: String,
    val summary: String,
    val tags: List<String> = emptyList(),
)

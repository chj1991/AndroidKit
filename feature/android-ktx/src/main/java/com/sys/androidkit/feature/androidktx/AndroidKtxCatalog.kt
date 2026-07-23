package com.sys.androidkit.feature.androidktx

/**
 * 对照 [dengzii/AndroidKtx](https://github.com/dengzii/AndroidKtx) README 目录。
 * 源码落在 `:core:androidktx`（包名 `com.dengzii.ktx`）。
 */
enum class AndroidKtxGroup {
    VIEW,
    CONTEXT,
    ACTIVITY,
    UTIL,
}

data class AndroidKtxEntry(
    val id: String,
    val title: String,
    val summary: String,
    val section: String,
    val group: AndroidKtxGroup,
)

object AndroidKtxCatalog {

    val all: List<AndroidKtxEntry> = listOf(
        AndroidKtxEntry(
            "view_click",
            "View 可见性 / 防抖点击",
            "gone / show / hide / toggleVisible / antiShakeClick",
            "View",
            AndroidKtxGroup.VIEW,
        ),
        AndroidKtxEntry(
            "view_text",
            "TextView Drawable / 状态色",
            "setDrawable* / setTextColorStateList / addTextWatcher",
            "View",
            AndroidKtxGroup.VIEW,
        ),
        AndroidKtxEntry(
            "context",
            "Context 资源与屏幕",
            "getColorCompat / getStatusBarHeight / getScreenHeight / 权限检查",
            "Context",
            AndroidKtxGroup.CONTEXT,
        ),
        AndroidKtxEntry(
            "activity",
            "Activity 启动与 Extra",
            "startActivity / intentExtra / lazyFindView",
            "Activity",
            AndroidKtxGroup.ACTIVITY,
        ),
        AndroidKtxEntry(
            "intent",
            "Intent / Bundle 扩展",
            "checkExtraExists / getStringExtraOrDefault / Bundle.getOrDefault",
            "Intent",
            AndroidKtxGroup.ACTIVITY,
        ),
        AndroidKtxEntry(
            "preferences",
            "Preferences 委托",
            "Preferences + preference() + update {}",
            "SharedPreferences",
            AndroidKtxGroup.UTIL,
        ),
        AndroidKtxEntry(
            "file",
            "File 扩展",
            "createOrExistsFile / rename / md5",
            "File",
            AndroidKtxGroup.UTIL,
        ),
        AndroidKtxEntry(
            "bitmap",
            "Bitmap 扩展",
            "toRound / toDrawable / saveTo / blur",
            "Bitmap",
            AndroidKtxGroup.UTIL,
        ),
        AndroidKtxEntry(
            "uri",
            "Uri 取真实路径",
            "Uri.getRealPath(context)",
            "Uri",
            AndroidKtxGroup.UTIL,
        ),
    )

    fun find(id: String): AndroidKtxEntry? = all.find { it.id == id }

    fun filter(group: AndroidKtxGroup?, query: String): List<AndroidKtxEntry> {
        val q = query.trim().lowercase()
        return all.filter { e ->
            (group == null || e.group == group) &&
                (
                    q.isEmpty() ||
                        e.title.lowercase().contains(q) ||
                        e.summary.lowercase().contains(q) ||
                        e.section.lowercase().contains(q)
                    )
        }
    }
}

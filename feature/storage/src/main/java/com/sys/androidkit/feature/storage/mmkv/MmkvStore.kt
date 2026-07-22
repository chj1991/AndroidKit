package com.sys.androidkit.feature.storage.mmkv

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * 腾讯 MMKV 轻量封装：同步 KV、mmap 落盘，适合高频读写偏好。
 *
 * 使用前需在 Application 调用 [MmkvInitializer.init]。
 */
class MmkvStore(
    private val mmkv: MMKV = requireNotNull(MMKV.defaultMMKV()) {
        "MMKV 未初始化，请先调用 MmkvInitializer.init(context)"
    },
) {

    fun putString(key: String, value: String): Boolean = mmkv.encode(key, value)

    fun getString(key: String, default: String = ""): String =
        mmkv.decodeString(key, default) ?: default

    fun putInt(key: String, value: Int): Boolean = mmkv.encode(key, value)

    fun getInt(key: String, default: Int = 0): Int = mmkv.decodeInt(key, default)

    fun putBoolean(key: String, value: Boolean): Boolean = mmkv.encode(key, value)

    fun getBoolean(key: String, default: Boolean = false): Boolean =
        mmkv.decodeBool(key, default)

    fun contains(key: String): Boolean = mmkv.containsKey(key)

    fun remove(key: String) {
        mmkv.removeValueForKey(key)
    }

    fun clearAll() {
        mmkv.clearAll()
    }

    fun allKeys(): List<String> = mmkv.allKeys()?.toList().orEmpty()

    fun totalSize(): Long = mmkv.totalSize()

    fun count(): Long = mmkv.count()

    companion object {
        const val KEY_NICKNAME = "demo_nickname"
        const val KEY_COUNTER = "demo_counter"
        const val KEY_FLAG = "demo_flag"
    }
}

object MmkvInitializer {
    @Volatile
    private var initialized = false

    fun init(context: Context): String {
        if (initialized) {
            return MMKV.getRootDir().orEmpty()
        }
        synchronized(this) {
            if (!initialized) {
                val root = MMKV.initialize(context.applicationContext)
                initialized = true
                return root
            }
        }
        return MMKV.getRootDir().orEmpty()
    }
}

package com.sys.androidkit.feature.system

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sys.androidkit.feature.system.databinding.ActivityBubbleBinding

/**
 * 气泡展开 Activity（allowEmbedded + resizeable）。
 * 由 Notification BubbleMetadata PendingIntent 拉起。
 */
class BubbleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBubbleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
            ?: getString(R.string.feature_system_bubble_default_message)
        binding.tvBubbleMessage.text = message
        binding.btnClose.setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_MESSAGE = "bubble_message"
    }
}

package com.sys.androidkit.feature.androidktx

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dengzii.ktx.android.content.intentExtra

/**
 * 供 Activity / startActivityForResult 演示回传结果。
 */
class AndroidKtxResultActivity : AppCompatActivity() {

    private val orderId by intentExtra("ext_order_id", -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
        }
        root.addView(
            TextView(this).apply {
                text = "AndroidKtxResultActivity\nintentExtra(\"ext_order_id\") = $orderId"
                textSize = 16f
            },
        )
        root.addView(
            Button(this).apply {
                text = "setResult(OK) 并 finish"
                setOnClickListener {
                    setResult(
                        Activity.RESULT_OK,
                        Intent().putExtra("result_msg", "orderId=$orderId"),
                    )
                    finish()
                }
            },
        )
        setContentView(root)
    }
}

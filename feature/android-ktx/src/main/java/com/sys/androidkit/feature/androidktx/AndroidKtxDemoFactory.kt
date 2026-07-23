package com.sys.androidkit.feature.androidktx

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dengzii.ktx.android.antiShakeClick
import com.dengzii.ktx.android.content.checkExtraExists
import com.dengzii.ktx.android.content.getColorCompat
import com.dengzii.ktx.android.content.getScreenHeight
import com.dengzii.ktx.android.content.getStatusBarHeight
import com.dengzii.ktx.android.content.getStringExtraOrDefault
import com.dengzii.ktx.android.content.isPermissionGranted
import com.dengzii.ktx.android.content.requestSelectFile
import com.dengzii.ktx.android.content.startActivity
import com.dengzii.ktx.android.content.startActivityForResult
import com.dengzii.ktx.android.content.update
import com.dengzii.ktx.android.gone
import com.dengzii.ktx.android.hide
import com.dengzii.ktx.android.setTextColorStateList
import com.dengzii.ktx.android.show
import com.dengzii.ktx.android.toRound
import com.dengzii.ktx.android.toggleVisible
import com.dengzii.ktx.createOrExistsFile
import com.dengzii.ktx.md5String
import com.dengzii.ktx.rename
import com.google.android.material.button.MaterialButton
import java.io.File

/**
 * 按 README 分类构建可交互演示。
 */
object AndroidKtxDemoFactory {

    fun create(fragment: Fragment, id: String): View {
        val context = fragment.requireContext()
        val root = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }

        fun tip(text: String) {
            root.addView(
                TextView(context).apply {
                    this.text = text
                    setPadding(0, 0, 0, dp(context, 12))
                },
            )
        }

        fun button(label: String, onClick: (View) -> Unit) {
            root.addView(
                MaterialButton(context).apply {
                    text = label
                    setOnClickListener { onClick(it) }
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    ).apply { bottomMargin = dp(context, 8) }
                },
            )
        }

        fun logView(): TextView = TextView(context).apply {
            textSize = 13f
            setTextIsSelectable(true)
            setPadding(0, dp(context, 8), 0, 0)
        }.also { root.addView(it) }

        when (id) {
            "view_click" -> {
                tip("View.gone / show / hide / toggleVisible + antiShakeClick（默认 300ms）")
                val target = TextView(context).apply {
                    text = "目标 View（VISIBLE）"
                    gravity = Gravity.CENTER
                    setBackgroundColor(Color.parseColor("#E3F2FD"))
                    setPadding(0, dp(context, 24), 0, dp(context, 24))
                }
                root.addView(target)
                button("gone()") { target.gone() }
                button("show()") { target.show() }
                button("hide() INVISIBLE") { target.hide() }
                button("toggleVisible()") { target.toggleVisible() }
                var clicks = 0
                val counter = logView()
                counter.text = "防抖点击次数：0（快速连点只会记一次）"
                MaterialButton(context).apply {
                    text = "antiShakeClick 点我"
                    antiShakeClick {
                        clicks++
                        counter.text = "防抖点击次数：$clicks"
                    }
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )
                    root.addView(this)
                }
            }
            "view_text" -> {
                tip("TextView.setTextColorStateList：按下变色")
                val tv = TextView(context).apply {
                    text = "按住我看颜色变化"
                    textSize = 18f
                    isClickable = true
                    isFocusable = true
                    setPadding(0, dp(context, 16), 0, dp(context, 16))
                    setTextColorStateList {
                        stateNormal = android.R.color.black
                        statePressed = android.R.color.holo_red_light
                    }
                }
                root.addView(tv)
                tip("EditText 可用 addTextWatcher；此处用简单 TextView 展示状态色 API。")
            }
            "context" -> {
                tip("Context 资源 / 屏幕 / 权限检查")
                val log = logView()
                fun refresh() {
                    val color = context.getColorCompat(android.R.color.holo_blue_dark)
                    val status = context.getStatusBarHeight()
                    val screenH = context.getScreenHeight()
                    val camera = context.isPermissionGranted(Manifest.permission.CAMERA)
                    log.text = buildString {
                        appendLine("getColorCompat(holo_blue_dark)=#${Integer.toHexString(color)}")
                        appendLine("getStatusBarHeight()=$status px")
                        appendLine("getScreenHeight()=$screenH px")
                        appendLine("isPermissionGranted(CAMERA)=$camera")
                    }
                }
                button("刷新 Context 信息") { refresh() }
                refresh()
            }
            "activity" -> {
                tip("startActivity / startActivityForResult（HiddenFragment 回调）")
                val log = logView()
                val activity = fragment.requireActivity() as AppCompatActivity
                button("startActivity { putExtra }") {
                    activity.startActivity<AndroidKtxResultActivity> {
                        putExtra("ext_order_id", 42)
                    }
                }
                button("startActivityForResult 回调") {
                    val intent = Intent(activity, AndroidKtxResultActivity::class.java)
                        .putExtra("ext_order_id", 100)
                    // 扩展定义在调用方 Activity 上（reified T = 当前 Activity），勿写目标 Activity 泛型
                    activity.startActivityForResult(intent) { req, res, data ->
                        val msg = data?.getStringExtra("result_msg")
                        log.text = "requestCode=$req resultCode=$res msg=$msg"
                        Toast.makeText(context, log.text, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            "intent" -> {
                tip("Intent.checkExtraExists / getStringExtraOrDefault")
                val log = logView()
                button("演示 Intent 扩展") {
                    val intent = android.content.Intent().apply {
                        putExtra("Key1", "A")
                        putExtra("Key3", "C")
                    }
                    val missing = mutableListOf<String>()
                    intent.checkExtraExists("Key1", "Key2", "Key3") { missing += it }
                    val def = intent.getStringExtraOrDefault("KeyX", "DefaultValue")
                    log.text = "缺失 keys=$missing\ngetStringExtraOrDefault(KeyX)=$def\n" +
                        "Key1=${intent.getStringExtraOrDefault("Key1", "?")}"
                }
            }
            "preferences" -> {
                tip("Preferences 委托：preference() + update{}")
                val log = logView()
                fun dump(cfg: DemoAppConfig) {
                    log.text = "userName=${cfg.userName}, age=${cfg.age}"
                }
                dump(DemoAppConfig(context))
                button("update { userName / age }") {
                    val cfg = DemoAppConfig(context).update {
                        userName = "AndroidKit"
                        age = 18
                    }
                    dump(cfg)
                }
                button("再读一次（应持久化）") {
                    dump(DemoAppConfig(context))
                }
            }
            "file" -> {
                tip("File.createOrExistsFile / rename / md5String")
                val log = logView()
                button("在 cache 创建并改名、算 md5") {
                    val dir = context.cacheDir
                    val file = File(dir, "android_ktx_demo.txt")
                    file.createOrExistsFile()
                    file.writeText("hello-android-ktx")
                    val renamed = file.rename("android_ktx_renamed.txt")
                    val target = File(dir, "android_ktx_renamed.txt")
                    log.text = "create ok, rename=$renamed\npath=${target.absolutePath}\n" +
                        "md5=${target.md5String()}"
                }
            }
            "bitmap" -> {
                tip("Bitmap.toRound；下方 ImageView 展示结果")
                val image = ImageView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(dp(context, 120), dp(context, 120))
                        .apply { bottomMargin = dp(context, 8) }
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
                root.addView(image)
                button("生成方图并 toRound()") {
                    val src = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888).also { bmp ->
                        Canvas(bmp).drawColor(Color.parseColor("#01A8E3"))
                        val p = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            color = Color.WHITE
                            textSize = 48f
                            textAlign = Paint.Align.CENTER
                        }
                        Canvas(bmp).drawText("Ktx", 100f, 115f, p)
                    }
                    image.setImageBitmap(src.toRound(borderSize = 6, borderColor = Color.DKGRAY))
                }
            }
            "uri" -> {
                tip("requestSelectFile + Uri.getRealPath（系统文件选择）")
                val log = logView()
                val activity = fragment.requireActivity() as AppCompatActivity
                button("选择图片并解析路径") {
                    activity.requestSelectFile("image/*") { file ->
                        log.text = if (file == null) {
                            "未选择或无法解析路径"
                        } else {
                            "File path=${file.absolutePath}\nexists=${file.exists()}"
                        }
                    }
                }
                tip("也可对 Intent.data 调用 uri.getRealPath(context)")
            }
            else -> tip("暂无演示：$id")
        }

        return ScrollView(context).apply {
            isFillViewport = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            addView(root)
        }
    }

    private fun dp(context: android.content.Context, v: Int): Int =
        (v * context.resources.displayMetrics.density).toInt()
}

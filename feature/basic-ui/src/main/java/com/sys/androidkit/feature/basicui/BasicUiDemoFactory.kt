package com.sys.androidkit.feature.basicui

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.peakmain.ui.adapter.flow.BaseFlowAdapter
import com.peakmain.ui.dialog.AlertDialog
import com.peakmain.ui.loading.CircleLoadingView
import com.peakmain.ui.loading.ShapeLoadingView
import com.peakmain.ui.navigationbar.DefaultNavigationBar
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.MultiTypeSupport
import com.peakmain.ui.recyclerview.adapter.ViewHolder
import com.peakmain.ui.recyclerview.creator.DefaultLoadViewCreator
import com.peakmain.ui.recyclerview.creator.DefaultRefreshViewCreator
import com.peakmain.ui.recyclerview.listener.OnItemClickListener
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView
import com.peakmain.ui.recyclerview.view.RefreshRecyclerView
import com.peakmain.ui.recyclerview.view.WrapRecyclerView
import com.peakmain.ui.tablayout.BaseTabLayout
import com.peakmain.ui.toast.TopToastUtils
import com.peakmain.ui.utils.TextUtils
import com.peakmain.ui.widget.AutoDeleteEditText
import com.peakmain.ui.widget.CustomPopupWindow
import com.peakmain.ui.widget.FlowLayout
import com.peakmain.ui.widget.ShapeTextView
import com.peakmain.ui.widget.listener.SimpleCustomKeyboardListener
import com.peakmain.ui.widget.menu.ListMenuView
import com.peakmain.ui.widget.password.CustomerKeyboard
import com.peakmain.ui.widget.password.PasswordEditText
import com.peakmain.ui.wheelview.view.GenderWheelView
import com.peakmain.ui.wheelview.view.SingleWheelWindow
import com.peakmain.ui.wheelview.view.TimePickerWheelView

/**
 * 按 Wiki 组件 id 构建可交互演示区（API 与官方 BasicUI 一致）。
 */
object BasicUiDemoFactory {

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

        when (id) {
            "dialog" -> {
                tip("AlertDialog.Builder：底部弹出 + 全宽 + 自定义布局（白底圆角 ActionSheet）")
                button("弹出底部 Dialog") {
                    AlertDialog.Builder(context)
                        .setContentView(R.layout.demo_basic_ui_dialog)
                        .fromBottom(true)
                        .setFullWidth()
                        .addOnClickListener(R.id.btnDialogOk) { it?.dismiss() }
                        .addOnClickListener(R.id.tvDialogAction) {
                            Toast.makeText(context, "点击了自定义条目", Toast.LENGTH_SHORT).show()
                            it?.dismiss()
                        }
                        .show()
                }
                button("居中缩放 Dialog") {
                    AlertDialog.Builder(context)
                        .setContentView(R.layout.demo_basic_ui_dialog)
                        .addDefaultAnimation()
                        .addOnClickListener(R.id.btnDialogOk) { it?.dismiss() }
                        .show()
                }
            }
            "navigation_bar" -> {
                tip(
                    "DefaultNavigationBar.Builder（对照 Wiki）。\n" +
                        "下方预览可点按钮切换各项配置；ActionBar 相关项需 AppCompatActivity。",
                )
                data class NavOptions(
                    var title: String = "BasicUI NavigationBar",
                    var leftText: String? = "关闭",
                    var showLeft: Boolean = true,
                    var showTitle: Boolean = true,
                    var showRight: Boolean = true,
                    var titleColorWhite: Boolean = true,
                    var leftColorWhite: Boolean = true,
                    var toolbarColorRes: Int = com.peakmain.ui.R.color.ui_color_01a8e3,
                    var navigationIcon: Boolean = true,
                    var rightRes: Boolean = true,
                    var boldTitle: Boolean = true,
                )
                val options = NavOptions()
                val status = TextView(context).apply {
                    setPadding(0, 0, 0, dp(context, 8))
                    textSize = 12f
                }
                val content = TextView(context).apply {
                    text = "内容区（FrameLayout 子 View，会被 NavigationBar topMargin 顶开）"
                    setPadding(dp(context, 16), dp(context, 16), dp(context, 16), dp(context, 16))
                    setBackgroundColor(Color.parseColor("#E3F2FD"))
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                }
                val host = FrameLayout(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(context, 200),
                    )
                    addView(content)
                }
                root.addView(host)
                root.addView(status)

                var bar: DefaultNavigationBar? = null
                fun rebuild() {
                    // 只移除 NavigationBar（index 0），保留内容子 View
                    while (host.childCount > 1) {
                        host.removeViewAt(0)
                    }
                    if (host.childCount == 1 && host.getChildAt(0) !== content) {
                        host.removeAllViews()
                        host.addView(content)
                    }
                    content.layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    ).also { it.topMargin = 0 }
                    val builder = DefaultNavigationBar.Builder(context, host)
                        .setTitleText(
                            options.title,
                            if (options.boldTitle) Typeface.DEFAULT_BOLD else Typeface.DEFAULT,
                        )
                        .setToolbarBackgroundColor(options.toolbarColorRes)
                        .setTitleTextColor(
                            if (options.titleColorWhite) {
                                android.R.color.white
                            } else {
                                com.peakmain.ui.R.color.ui_color_333333
                            },
                        )
                        .setLeftTextColor(
                            if (options.leftColorWhite) {
                                android.R.color.white
                            } else {
                                com.peakmain.ui.R.color.ui_color_333333
                            },
                        )
                        .setLeftClickListener {
                            Toast.makeText(context, "setLeftClickListener", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .setTitleClickListener {
                            Toast.makeText(context, "setTitleClickListener", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .setRightViewClickListener {
                            Toast.makeText(context, "setRightViewClickListener", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .setNavigationOnClickListener {
                            Toast.makeText(context, "setNavigationOnClickListener", Toast.LENGTH_SHORT)
                                .show()
                        }
                    if (options.showLeft) {
                        builder.setLeftText(options.leftText)
                    } else {
                        builder.hideLeftText()
                    }
                    if (!options.showTitle) builder.hideTitleText()
                    if (options.showRight) {
                        builder.showRightView()
                        if (options.rightRes) {
                            builder.setRightResId(com.peakmain.ui.R.drawable.ic_more)
                        }
                    } else {
                        builder.hideRightView()
                    }
                    if (options.navigationIcon) {
                        builder.setNavigationIcon(
                            androidx.appcompat.R.drawable.abc_ic_ab_back_material,
                        )
                    }
                    bar = builder.create()
                    status.text = buildString {
                        appendLine("当前配置：")
                        appendLine("· setTitleText / setTitleTextColor / setTitleClickListener")
                        appendLine("· setLeftText / setLeftTextColor / setLeftClickListener / hideLeftText")
                        appendLine("· hideTitleText / hideRightView / showRightView")
                        appendLine("· setRightResId / setRightViewClickListener")
                        appendLine("· setToolbarBackgroundColor / setNavigationIcon")
                        appendLine("· setNavigationOnClickListener")
                        append(
                            "· setDisplayHomeAsUpEnabled / setDisplayShowTitleEnabled / " +
                                "setHomeAsUpIndicator（需 AppCompatActivity）",
                        )
                    }
                }
                rebuild()

                tip("—— Builder 可修改项（点击切换并重建）——")
                button("setTitleText 切换标题") {
                    options.title =
                        if (options.title.startsWith("BasicUI")) "自定义标题" else "BasicUI NavigationBar"
                    rebuild()
                }
                button("setTitleTextColor 切换标题色") {
                    options.titleColorWhite = !options.titleColorWhite
                    rebuild()
                }
                button("setLeftText / hideLeftText") {
                    options.showLeft = !options.showLeft
                    rebuild()
                }
                button("setLeftTextColor 切换左侧色") {
                    options.leftColorWhite = !options.leftColorWhite
                    rebuild()
                }
                button("hideTitleText / 显示标题") {
                    options.showTitle = !options.showTitle
                    rebuild()
                }
                button("hideRightView / showRightView") {
                    options.showRight = !options.showRight
                    rebuild()
                }
                button("setRightResId（更多图标）") {
                    options.rightRes = !options.rightRes
                    options.showRight = true
                    rebuild()
                }
                button("setToolbarBackgroundColor 切换背景") {
                    options.toolbarColorRes =
                        if (options.toolbarColorRes == com.peakmain.ui.R.color.ui_color_01a8e3) {
                            com.peakmain.ui.R.color.ui_color_BE3468
                        } else {
                            com.peakmain.ui.R.color.ui_color_01a8e3
                        }
                    rebuild()
                }
                button("setNavigationIcon 显示/隐藏返回图标") {
                    options.navigationIcon = !options.navigationIcon
                    rebuild()
                }
                button("Title 粗体 / 常规") {
                    options.boldTitle = !options.boldTitle
                    rebuild()
                }
                button("setTitleTextSize(18f) 运行时") {
                    bar?.setTitleTextSize(18f)
                }
                button("setElevation(8f) 运行时") {
                    bar?.setElevation(8f)
                }
                tip(
                    "Wiki / BaseActivity 典型用法：\n" +
                        "Builder(activity, content)\n" +
                        "  .hideLeftText()\n" +
                        "  .setDisplayHomeAsUpEnabled(true)\n" +
                        "  .setNavigationOnClickListener { finish() }\n" +
                        "  .hideRightView()\n" +
                        "  .setToolbarBackgroundColor(R.color.ui_color_01a8e3)\n" +
                        "  .setTitleText(\"...\")\n" +
                        "  .create()",
                )
            }
            "auto_edit" -> {
                tip(
                    "Wiki：自带清除按钮；获得焦点且有内容时显示删除图标。\n" +
                        "属性：adet_text_size / adet_text_color / adet_hint_color / " +
                        "adet_hint / adet_isTop / adet_padding_top / adet_isSingle / " +
                        "android:inputType / adet_max_length / adet_tint_color / adet_delete_src",
                )
                val demo = LayoutInflater.from(context)
                    .inflate(R.layout.demo_basic_ui_auto_edit, root, false)
                demo.findViewById<AutoDeleteEditText>(R.id.autoEditName)
                    .setText("你好，我是 Peakmain")
                root.addView(demo)
            }
            "shape" -> {
                tip(
                    "Wiki：ShapeTextView / ShapeLinearLayout / ShapeConstraintLayout\n" +
                        "支持背景、描边、圆角、四角圆角、渐变、shape、按下色、禁用色、水波纹",
                )
                val demo = LayoutInflater.from(context)
                    .inflate(R.layout.demo_basic_ui_shape, root, false)
                val stvPressed = demo.findViewById<ShapeTextView>(R.id.stvPressed)
                val stvDisabled = demo.findViewById<ShapeTextView>(R.id.stvDisabled)
                stvPressed.setPressedColor(Color.GRAY)
                stvDisabled.setUnEnabledColor(Color.parseColor("#90CAF9"))
                demo.findViewById<View>(R.id.btnToggleEnable).setOnClickListener {
                    stvDisabled.isEnabled = !stvDisabled.isEnabled
                    Toast.makeText(
                        context,
                        if (stvDisabled.isEnabled) "已启用" else "已禁用",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                root.addView(demo)
            }
            "loading" -> {
                tip(
                    "Wiki：show()/hide() 弹 Dialog，不要先 addView 到布局（会已有 parent）。\n" +
                        "布局内嵌仅作预览动画；弹窗 Loading 需 new 后直接 show()。",
                )
                tip("—— 预览（嵌在布局里，仅看动画）——")
                root.addView(
                    CircleLoadingView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            dp(context, 80),
                        )
                    },
                )
                root.addView(
                    ShapeLoadingView(context).apply {
                        setLoadingName("数据正在加载…")
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            dp(context, 120),
                        ).apply { topMargin = dp(context, 8) }
                    },
                )
                tip("—— Dialog 弹窗（官方用法）——")
                button("CircleLoadingView.show() → 2s hide()") {
                    // 必须独立实例，不可复用已挂到布局的 View
                    val loading = CircleLoadingView(context)
                    loading.show()
                    loading.postDelayed({ loading.hide() }, 2000)
                }
                button("ShapeLoadingView.show() → 2s hide()") {
                    val loading = ShapeLoadingView(context)
                    loading.setLoadingName("数据正在加载…")
                    loading.show()
                    loading.postDelayed({ loading.hide() }, 2000)
                }
            }
            "flow_popup" -> {
                tip(
                    "Wiki：FlowLayout 流式标签 + CustomPopupWindow\n" +
                        "点标签弹出删除气泡；下方按钮演示 showAsDropDown",
                )
                val tags = mutableListOf(
                    "Kotlin", "Java", "Compose", "XML", "Hilt", "Room", "Flow", "Navigation",
                )
                val flow = FlowLayout(context).apply {
                    layoutParams = matchWidth(context)
                    // 避免外层 ScrollView 抢走点击
                    isNestedScrollingEnabled = false
                }
                fun bindFlow() {
                    flow.setAdapter(object : BaseFlowAdapter() {
                        override val count: Int get() = tags.size

                        override fun getView(position: Int, parent: ViewGroup?): View {
                            val tag = tags[position]
                            return TextView(context).apply {
                                text = tag
                                setPadding(
                                    dp(context, 14),
                                    dp(context, 8),
                                    dp(context, 14),
                                    dp(context, 8),
                                )
                                setTextColor(Color.WHITE)
                                textSize = 14f
                                setBackgroundColor(Color.parseColor("#01A8E3"))
                                isClickable = true
                                isFocusable = true
                                layoutParams = ViewGroup.MarginLayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                ).apply {
                                    leftMargin = dp(context, 8)
                                    topMargin = dp(context, 8)
                                }
                                setOnTouchListener { v, event ->
                                    when (event.actionMasked) {
                                        android.view.MotionEvent.ACTION_DOWN ->
                                            v.parent?.requestDisallowInterceptTouchEvent(true)
                                        android.view.MotionEvent.ACTION_UP,
                                        android.view.MotionEvent.ACTION_CANCEL,
                                        ->
                                            v.parent?.requestDisallowInterceptTouchEvent(false)
                                    }
                                    false
                                }
                                setOnClickListener { anchor ->
                                    val popup = CustomPopupWindow.PopupWindowBuilder(
                                        fragment.requireActivity(),
                                    )
                                        .setView(R.layout.demo_basic_ui_popup_item)
                                        .setFocusable(true)
                                        .enableOutsideTouchableDissmiss(true)
                                        .create()
                                    // 优先挂在标签下方，避免 showAtLocation 算坐标跑出屏幕
                                    popup.showAsDropDown(anchor, 0, dp(context, 4))
                                    popup.getView<View>(R.id.tvPopupDelete).setOnClickListener {
                                        tags.remove(tag)
                                        bindFlow()
                                        popup.dissmiss()
                                        Toast.makeText(context, "已删除 $tag", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            }
                        }
                    })
                }
                bindFlow()
                root.addView(flow)
                button("按钮下方 showAsDropDown") { anchorBtn ->
                    val content = TextView(context).apply {
                        text = "CustomPopupWindow · showAsDropDown"
                        setPadding(
                            dp(context, 24),
                            dp(context, 16),
                            dp(context, 24),
                            dp(context, 16),
                        )
                        setBackgroundColor(Color.WHITE)
                    }
                    CustomPopupWindow.PopupWindowBuilder(fragment.requireActivity())
                        .setView(content)
                        .setFocusable(true)
                        .enableOutsideTouchableDissmiss(true)
                        .create()
                        .showAsDropDown(anchorBtn)
                }
            }
            "top_toast" -> {
                tip(
                    "TopToastUtils：依赖 ActivityUtils 栈顶 Activity（BasicUIProvider 已 init）。\n" +
                        "ToastBar 挂到 decorView；DialogToast 为顶部 Dialog。",
                )
                val activity = fragment.requireActivity()
                button("showSuccessToast") {
                    TopToastUtils.showSuccessToast(activity, "操作成功")
                }
                button("showErrorToast") {
                    TopToastUtils.showErrorToast(activity, "操作失败")
                }
                button("showActionToast") {
                    TopToastUtils.showActionToast(activity, "这是一条普通提示")
                }
                button("showSuccessDialogToast") {
                    TopToastUtils.showSuccessDialogToast(activity, "Dialog 操作成功")
                }
                button("showErrorDialogToast") {
                    TopToastUtils.showErrorDialogToast(activity, "Dialog 操作失败")
                }
                button("无 Activity 参数（走栈顶）") {
                    TopToastUtils.showSuccessToast("栈顶 Activity 成功提示")
                }
            }
            "password" -> {
                tip("PasswordEditText + CustomerKeyboard")
                val pwd = PasswordEditText(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(context, 48),
                    )
                }
                val keyboard = CustomerKeyboard(context).apply {
                    layoutParams = matchWidth(context).apply { topMargin = dp(context, 12) }
                    setOnCustomerKeyboardClickListener(object : SimpleCustomKeyboardListener() {
                        override fun click(number: String?) {
                            number?.let { pwd.addPasswordNumber(it) }
                        }

                        override fun delete() {
                            pwd.deletePassWord()
                        }
                    })
                }
                root.addView(pwd)
                root.addView(keyboard)
            }
            "wheel" -> {
                tip(
                    "Wiki：底部弹窗选择器（白底 + 取消/标题/确认 + WheelView）\n" +
                        "TimePickerWheelView / GenderWheelView / SingleWheelWindow",
                )
                val activity = fragment.requireActivity()
                val resultTv = TextView(context).apply {
                    text = "选择结果：—"
                    setPadding(0, 0, 0, dp(context, 12))
                }
                root.addView(resultTv)
                button("时间选择器 TimePickerWheelView") {
                    TimePickerWheelView(activity) { time ->
                        resultTv.text = "选择结果：时间 $time"
                        Toast.makeText(context, "time:$time", Toast.LENGTH_SHORT).show()
                    }.setTitle("时间选择器").show()
                }
                button("性别选择器 GenderWheelView") {
                    GenderWheelView(activity) { gender ->
                        resultTv.text = "选择结果：性别 $gender"
                        Toast.makeText(context, gender, Toast.LENGTH_SHORT).show()
                    }.setTitle("性别选择器").show()
                }
                button("单列选择器 SingleWheelWindow（月份）") {
                    val months = (1..12).map { "${it}月" }
                    SingleWheelWindow(
                        activity,
                        object : SingleWheelWindow.OnSelectOptionCallback<String> {
                            override fun onSelectOption(option: String, position: Int) {
                                resultTv.text = "选择结果：$option（index=$position）"
                                Toast.makeText(context, option, Toast.LENGTH_SHORT).show()
                            }
                        },
                    ).apply {
                        setTitle("选择月份")
                        setWheelOptions(months)
                    }.show()
                }
            }
            "tab" -> {
                tip(
                    "Wiki：自定义 TabLayout 继承 BaseTabLayout，配合 ViewPager\n" +
                        "滑动时 ColorTrackTextView 左右变色过渡",
                )
                val titles = listOf("新闻", "直播", "推荐", "抗击肺炎", "视频", "图片", "段子", "精华")
                val demo = LayoutInflater.from(context)
                    .inflate(R.layout.demo_basic_ui_tab, root, false)
                val tabLayout = demo.findViewById<DemoBasicUiTabLayout>(R.id.tabLayout)
                val viewPager = demo.findViewById<ViewPager>(R.id.viewPager)
                viewPager.adapter = object : PagerAdapter() {
                    override fun getCount(): Int = titles.size

                    override fun isViewFromObject(view: View, `object`: Any): Boolean =
                        view === `object`

                    override fun instantiateItem(container: ViewGroup, position: Int): Any {
                        val page = TextView(context).apply {
                            text = titles[position]
                            textSize = 22f
                            gravity = Gravity.CENTER
                            setBackgroundColor(
                                if (position % 2 == 0) {
                                    Color.parseColor("#E3F2FD")
                                } else {
                                    Color.parseColor("#FFF3E0")
                                },
                            )
                        }
                        container.addView(
                            page,
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            ),
                        )
                        return page
                    }

                    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                        container.removeView(`object` as View)
                    }

                    override fun getPageTitle(position: Int): CharSequence = titles[position]
                }
                tabLayout.initIndicator(titles, viewPager)
                // 首项选中态
                viewPager.post {
                    viewPager.currentItem = 0
                }
                tabLayout.setOnTabItemClickListener(object : BaseTabLayout.OnTabItemClickListener {
                    override fun onTabItem(postition: Int) {
                        viewPager.currentItem = postition
                    }
                })
                root.addView(demo)
            }
            "rv_common" -> {
                tip(
                    "Wiki：CommonRecyclerAdapter + LoadRefreshRecyclerView\n" +
                        "默认：下拉刷新 / 上拉加载；另可切单布局、多布局、头尾部",
                )
                val host = FrameLayout(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1f,
                    )
                }
                val matchParentLp = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                fun showRefreshLoad() {
                    host.removeAllViews()
                    var index = 20
                    var lastIndex = 30
                    fun buildList(): MutableList<String> =
                        (0 until 20).map { "数据:$it" }.toMutableList()
                    val rv = LoadRefreshRecyclerView(context).apply {
                        layoutManager = LinearLayoutManager(context)
                        overScrollMode = View.OVER_SCROLL_NEVER
                        addItemDecoration(
                            DividerItemDecoration(context, DividerItemDecoration.VERTICAL),
                        )
                    }
                    val adapter = object : CommonRecyclerAdapter<String>(
                        context,
                        buildList(),
                        R.layout.demo_basic_ui_rv_item,
                    ) {
                        override fun convert(holder: ViewHolder, item: String) {
                            holder.setText(R.id.tvTitle, item)
                            holder.setText(R.id.tvSub, "下拉刷新 / 上拉加载")
                        }
                    }
                    // 官方顺序：先 adapter，再 Creator + Listener
                    rv.adapter = adapter
                    rv.addRefreshViewCreator(DefaultRefreshViewCreator())
                    rv.setOnRefreshListener(object : RefreshRecyclerView.OnRefreshListener {
                        override fun onRefresh() {
                            rv.postDelayed({
                                index = 20
                                lastIndex = 30
                                adapter.setData(buildList())
                                rv.onStopRefresh()
                                Toast.makeText(context, "刷新完成 setData", Toast.LENGTH_SHORT)
                                    .show()
                            }, 1500)
                        }
                    })
                    rv.addLoadViewCreator(DefaultLoadViewCreator())
                    rv.setOnLoadMoreListener(object : LoadRefreshRecyclerView.OnLoadMoreListener {
                        override fun onLoad() {
                            rv.postDelayed({
                                val more = ArrayList<String>()
                                while (index < lastIndex) {
                                    more.add("新数据:$index")
                                    index++
                                }
                                lastIndex = index + 10
                                adapter.addData(more)
                                rv.onStopLoad()
                                Toast.makeText(
                                    context,
                                    "加载完成 addData，共 ${adapter.dataSize} 条",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }, 1500)
                        }

                        override val isLoadMore: Boolean
                            get() = adapter.dataSize < 45
                    })
                    host.addView(rv, matchParentLp)
                }
                fun showSingle() {
                    host.removeAllViews()
                    val data = (0 until 20).map { "数据:$it" }.toMutableList()
                    val rv = RecyclerView(context).apply {
                        layoutManager = LinearLayoutManager(context)
                        addItemDecoration(
                            DividerItemDecoration(context, DividerItemDecoration.VERTICAL),
                        )
                        adapter = object : CommonRecyclerAdapter<String>(
                            context,
                            data,
                            R.layout.demo_basic_ui_rv_item,
                        ) {
                            override fun convert(holder: ViewHolder, item: String) {
                                holder.setText(R.id.tvTitle, item)
                                holder.setText(R.id.tvSub, "setText")
                            }
                        }.also { adapter ->
                            adapter.setOnItemClickListener(object : OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    Toast.makeText(
                                        context,
                                        "onItemClick $position",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            })
                        }
                    }
                    host.addView(rv, matchParentLp)
                }
                fun showMulti() {
                    host.removeAllViews()
                    val data = (0 until 20).map { "多类型:$it" }.toMutableList()
                    val support = object : MultiTypeSupport<String> {
                        override fun getLayoutId(item: String, position: Int): Int {
                            return if (position % 2 == 0) {
                                R.layout.demo_basic_ui_rv_item
                            } else {
                                R.layout.demo_basic_ui_rv_item_alt
                            }
                        }
                    }
                    val rv = RecyclerView(context).apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = object : CommonRecyclerAdapter<String>(
                            context,
                            data,
                            support,
                        ) {
                            override fun convert(holder: ViewHolder, item: String) {
                                holder.setText(R.id.tvTitle, item)
                                if (holder.getView<View>(R.id.tvSub) != null) {
                                    holder.setText(R.id.tvSub, "type A")
                                }
                            }
                        }
                    }
                    host.addView(rv, matchParentLp)
                }
                fun showHeaderFooter() {
                    host.removeAllViews()
                    val data = (0 until 15).map { "内容:$it" }.toMutableList()
                    val rv = WrapRecyclerView(context).apply {
                        layoutManager = LinearLayoutManager(context)
                    }
                    val adapter = object : CommonRecyclerAdapter<String>(
                        context,
                        data,
                        R.layout.demo_basic_ui_rv_item,
                    ) {
                        override fun convert(holder: ViewHolder, item: String) {
                            holder.setText(R.id.tvTitle, item)
                            holder.setText(R.id.tvSub, "body")
                        }
                    }
                    rv.adapter = adapter
                    val header = LayoutInflater.from(context)
                        .inflate(R.layout.demo_basic_ui_rv_header, rv, false)
                    header.setOnClickListener {
                        rv.removeHeaderView(header)
                        Toast.makeText(context, "已移除 Header", Toast.LENGTH_SHORT).show()
                    }
                    val footer = TextView(context).apply {
                        text = "Footer"
                        gravity = Gravity.CENTER
                        setPadding(0, dp(context, 16), 0, dp(context, 16))
                        setBackgroundColor(Color.parseColor("#FFE0E0E0"))
                    }
                    rv.addHeaderView(header)
                    rv.addFooterView(footer)
                    host.addView(rv, matchParentLp)
                }
                button("① 下拉刷新 / 上拉加载（默认）") { showRefreshLoad() }
                button("② 单布局 CommonRecyclerAdapter") { showSingle() }
                button("③ 多布局 MultiTypeSupport") { showMulti() }
                button("④ 头尾部 WrapRecyclerView") { showHeaderFooter() }
                root.addView(host)
                tip("列表在顶部下拉可刷新，滑到底部上拉加载更多")
                showRefreshLoad()
            }
            "rv_refresh" -> {
                tip(
                    "Wiki：LoadRefreshRecyclerView\n" +
                        "下拉刷新 onStopRefresh / 上拉加载 onStopLoad / 多状态布局",
                )
                val host = FrameLayout(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1f,
                    )
                }
                var index = 20
                var lastIndex = 30
                fun buildList(): MutableList<String> =
                    (0 until 20).map { "数据:$it" }.toMutableList()

                val rv = LoadRefreshRecyclerView(context).apply {
                    layoutManager = LinearLayoutManager(context)
                    addItemDecoration(
                        DividerItemDecoration(context, DividerItemDecoration.VERTICAL),
                    )
                }
                val adapter = object : CommonRecyclerAdapter<String>(
                    context,
                    buildList(),
                    R.layout.demo_basic_ui_rv_item,
                ) {
                    override fun convert(holder: ViewHolder, item: String) {
                        holder.setText(R.id.tvTitle, item)
                        holder.setText(R.id.tvSub, "pull / load")
                    }
                }
                rv.adapter = adapter
                rv.addRefreshViewCreator(DefaultRefreshViewCreator())
                rv.addLoadViewCreator(DefaultLoadViewCreator())
                rv.setOnRefreshListener(object : RefreshRecyclerView.OnRefreshListener {
                    override fun onRefresh() {
                        rv.postDelayed({
                            index = 20
                            lastIndex = 30
                            adapter.setData(buildList())
                            rv.onStopRefresh()
                            Toast.makeText(context, "刷新完成", Toast.LENGTH_SHORT).show()
                        }, 1500)
                    }
                })
                rv.setOnLoadMoreListener(object : LoadRefreshRecyclerView.OnLoadMoreListener {
                    override fun onLoad() {
                        rv.postDelayed({
                            val more = ArrayList<String>()
                            while (index < lastIndex) {
                                more.add("新数据:$index")
                                index++
                            }
                            lastIndex = index + 10
                            adapter.addData(more)
                            rv.onStopLoad()
                        }, 1500)
                    }

                    override val isLoadMore: Boolean
                        get() = adapter.dataSize < 40
                })
                host.addView(
                    rv,
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    ),
                )
                button("showEmptyView") {
                    adapter.setData(mutableListOf())
                    rv.showEmptyView()
                }
                button("showError") { rv.showError() }
                button("showNoNetwork") { rv.showNoNetwork() }
                button("showLoading") { rv.showLoading() }
                button("hideLoading + showContentView") {
                    rv.hideLoading()
                    if (adapter.dataSize == 0) {
                        adapter.setData(buildList())
                    }
                    rv.showContentView()
                }
                rv.setOnRetryClickListener {
                    Toast.makeText(context, "正在重新请求…", Toast.LENGTH_SHORT).show()
                    adapter.setData(buildList())
                    rv.showContentView()
                }
                root.addView(host)
                tip("提示：在列表区域下拉刷新，滑到底部加载更多")
            }
            "text_highlight" -> {
                tip("TextUtils.clipTextColor 文本高亮")
                val tv = TextView(context).apply {
                    textSize = 16f
                    text = TextUtils.clipTextColor(
                        "AndroidKit 接入 Peakmain BasicUI 文本高亮",
                        Color.parseColor("#C62828"),
                        0,
                        10,
                    )
                }
                root.addView(tv)
            }
            "list_menu" -> {
                tip(
                    "ListMenuView：顶部多 Tab + 下拉菜单 + 阴影遮罩（对照 Wiki「多条目菜单筛选」）。\n" +
                        "点 Tab 展开；点选项/确定关闭；点阴影关闭。",
                )
                root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                val listMenuView = LayoutInflater.from(context)
                    .inflate(R.layout.demo_basic_ui_list_menu, root, false) as ListMenuView
                val contentTv = listMenuView.findViewById<TextView>(R.id.tvListContent)
                val titles = listOf("推荐排序", "酒店品牌", "热门地点", "价格星级")
                val sort = listOf("智能排序", "好评优先", "低价优先", "高价优先", "距离优先")
                val brands = mutableListOf(
                    "如家", "汉庭", "全季", "亚朵", "桔子", "锦江之星", "维也纳", "喜来登",
                )
                val cities = mutableListOf(
                    "人民广场", "静安寺", "徐家汇", "陆家嘴", "虹桥", "浦东机场", "五角场", "中山公园",
                )
                val prices = listOf("不限", "¥150以下", "¥150-300", "¥300-500", "¥500以上", "四星及以上")
                listMenuView.setAdapter(
                    DemoListMenuAdapter(
                        context,
                        titles,
                        sort,
                        brands,
                        cities,
                        prices,
                    ) { result ->
                        contentTv?.text = "当前筛选：$result"
                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                    },
                )
                root.addView(
                    listMenuView,
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1f,
                    ),
                )
            }
            else -> tip("暂无演示：$id")
        }
        // Recycler / Tab / ListMenu 自带滚动或需占满高度；其它 Demo 包一层 ScrollView
        return if (id in setOf("rv_common", "rv_refresh", "tab", "list_menu")) {
            root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            root
        } else {
            ScrollView(context).apply {
                isFillViewport = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                addView(root)
            }
        }
    }

    private fun matchWidth(context: android.content.Context) =
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        ).apply { bottomMargin = dp(context, 8) }

    private fun dp(context: android.content.Context, v: Int): Int =
        (v * context.resources.displayMetrics.density).toInt()
}

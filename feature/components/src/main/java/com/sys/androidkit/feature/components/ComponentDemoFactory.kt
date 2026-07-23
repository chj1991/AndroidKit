package com.sys.androidkit.feature.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.SystemClock
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.Chronometer
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.sys.androidkit.core.ui.ext.showSnackbar

/**
 * 按组件 id 构建可交互演示 View，挂到 Showcase 容器中。
 */
object ComponentDemoFactory {

    fun create(fragment: Fragment, componentId: String): View {
        val context = fragment.requireContext()
        val root = column(context)
        when (componentId) {
            "sys_textview" -> {
                root.addView(label(context, "普通 / 可选中文本"))
                root.addView(
                    TextView(context).apply {
                        text = "Hello TextView — 长按可选中"
                        setTextIsSelectable(true)
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                    },
                )
            }
            "sys_edittext" -> {
                root.addView(label(context, "单行 / 多行输入"))
                root.addView(
                    EditText(context).apply {
                        hint = "单行 hint"
                        setSingleLine()
                        layoutParams = matchWidth()
                    },
                )
                root.addView(
                    EditText(context).apply {
                        hint = "多行输入"
                        minLines = 3
                        layoutParams = matchWidth()
                    },
                )
            }
            "sys_button" -> {
                root.addView(
                    Button(context).apply {
                        text = "Button"
                        setOnClickListener { toast(context, "Button clicked") }
                    },
                )
                root.addView(
                    ImageButton(context).apply {
                        setImageResource(android.R.drawable.ic_menu_compass)
                        contentDescription = "ImageButton"
                        setOnClickListener { toast(context, "ImageButton") }
                    },
                )
            }
            "sys_checkbox" -> {
                val status = TextView(context)
                val box = CheckBox(context).apply {
                    text = "同意条款"
                    setOnCheckedChangeListener { _, checked ->
                        status.text = "checked=$checked"
                    }
                }
                root.addView(box)
                root.addView(status)
            }
            "sys_radio" -> {
                val status = TextView(context)
                val group = RadioGroup(context).apply {
                    orientation = RadioGroup.VERTICAL
                    addView(RadioButton(context).apply { id = View.generateViewId(); text = "选项 A" })
                    addView(RadioButton(context).apply { id = View.generateViewId(); text = "选项 B" })
                    setOnCheckedChangeListener { _, checkedId ->
                        status.text = "checkedId=$checkedId"
                    }
                }
                root.addView(group)
                root.addView(status)
            }
            "sys_switch" -> {
                @Suppress("DEPRECATION")
                root.addView(
                    Switch(context).apply {
                        text = "系统 Switch"
                        setOnCheckedChangeListener { _, c -> toast(context, "switch=$c") }
                    },
                )
            }
            "sys_toggle" -> {
                root.addView(
                    ToggleButton(context).apply {
                        textOn = "ON"
                        textOff = "OFF"
                        isChecked = false
                    },
                )
            }
            "sys_seekbar" -> {
                val value = TextView(context).apply { text = "progress=0" }
                root.addView(
                    SeekBar(context).apply {
                        max = 100
                        layoutParams = matchWidth()
                        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(s: SeekBar?, p: Int, f: Boolean) {
                                value.text = "progress=$p"
                            }
                            override fun onStartTrackingTouch(s: SeekBar?) = Unit
                            override fun onStopTrackingTouch(s: SeekBar?) = Unit
                        })
                    },
                )
                root.addView(value)
            }
            "sys_progress" -> {
                root.addView(label(context, "Indeterminate"))
                root.addView(ProgressBar(context).apply { isIndeterminate = true })
                root.addView(label(context, "Determinate 60%"))
                root.addView(
                    ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
                        max = 100
                        progress = 60
                        layoutParams = matchWidth()
                    },
                )
            }
            "sys_spinner" -> {
                val items = listOf("Kotlin", "Java", "Compose")
                root.addView(
                    Spinner(context).apply {
                        adapter = ArrayAdapter(
                            context,
                            android.R.layout.simple_spinner_dropdown_item,
                            items,
                        )
                        layoutParams = matchWidth()
                    },
                )
            }
            "sys_rating" -> {
                val value = TextView(context)
                root.addView(
                    RatingBar(context).apply {
                        numStars = 5
                        stepSize = 0.5f
                        rating = 3.5f
                        onRatingBarChangeListener =
                            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                                value.text = "rating=$rating"
                            }
                    },
                )
                root.addView(value)
            }
            "sys_imageview" -> {
                root.addView(
                    ImageView(context).apply {
                        setImageResource(android.R.drawable.ic_menu_gallery)
                        adjustViewBounds = true
                        layoutParams = LinearLayout.LayoutParams(dp(context, 120), dp(context, 120))
                    },
                )
            }
            "sys_checkedtext" -> {
                root.addView(
                    CheckedTextView(context).apply {
                        text = "点我切换勾选"
                        setCheckMarkDrawable(android.R.drawable.checkbox_on_background)
                        isChecked = false
                        setPadding(dp(context, 12))
                        setOnClickListener {
                            isChecked = !isChecked
                        }
                    },
                )
            }
            "sys_chronometer" -> {
                val chrono = Chronometer(context).apply {
                    base = SystemClock.elapsedRealtime()
                    start()
                }
                root.addView(chrono)
                root.addView(
                    Button(context).apply {
                        text = "重置并继续"
                        setOnClickListener {
                            chrono.base = SystemClock.elapsedRealtime()
                            chrono.start()
                        }
                    },
                )
            }
            "sys_searchview" -> {
                root.addView(
                    SearchView(context).apply {
                        queryHint = "搜索…"
                        isIconified = false
                        layoutParams = matchWidth()
                        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                toast(context, "submit=$query")
                                return true
                            }
                            override fun onQueryTextChange(newText: String?): Boolean = false
                        })
                    },
                )
            }
            "sys_datepicker" -> {
                root.addView(
                    DatePicker(context).apply {
                        layoutParams = matchWidth()
                    },
                )
            }
            "sys_timepicker" -> {
                root.addView(
                    TimePicker(context).apply {
                        setIs24HourView(true)
                        layoutParams = matchWidth()
                    },
                )
            }
            "sys_numberpicker" -> {
                root.addView(
                    NumberPicker(context).apply {
                        minValue = 0
                        maxValue = 20
                        value = 5
                    },
                )
            }
            "sys_webview" -> {
                @SuppressLint("SetJavaScriptEnabled")
                root.addView(
                    WebView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            dp(context, 200),
                        )
                        settings.javaScriptEnabled = false
                        loadData(
                            "<html><body><h3>WebView Demo</h3><p>AndroidKit</p></body></html>",
                            "text/html",
                            "utf-8",
                        )
                    },
                )
            }
            "sys_scroll" -> {
                root.addView(label(context, "本页外层已是 ScrollView；内嵌纵向内容示意："))
                repeat(8) { i ->
                    root.addView(
                        TextView(context).apply {
                            text = "Scroll item #${i + 1}"
                            setPadding(0, dp(context, 8), 0, dp(context, 8))
                        },
                    )
                }
            }
            "m3_button" -> {
                root.addView(MaterialButton(context).apply { text = "Filled" })
                root.addView(
                    MaterialButton(
                        context,
                        null,
                        com.google.android.material.R.attr.materialButtonTonalStyle,
                    ).apply { text = "Tonal" },
                )
                root.addView(
                    MaterialButton(
                        context,
                        null,
                        com.google.android.material.R.attr.materialButtonOutlinedStyle,
                    ).apply { text = "Outlined" },
                )
                root.addView(
                    MaterialButton(
                        android.view.ContextThemeWrapper(
                            context,
                            com.google.android.material.R.style.Widget_Material3_Button_TextButton,
                        ),
                    ).apply { text = "Text" },
                )
            }
            "m3_toggle_group" -> {
                val group = MaterialButtonToggleGroup(context).apply {
                    isSingleSelection = true
                    addView(
                        MaterialButton(
                            context,
                            null,
                            com.google.android.material.R.attr.materialButtonOutlinedStyle,
                        ).apply {
                            id = View.generateViewId()
                            text = "Day"
                            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                        },
                    )
                    addView(
                        MaterialButton(
                            context,
                            null,
                            com.google.android.material.R.attr.materialButtonOutlinedStyle,
                        ).apply {
                            id = View.generateViewId()
                            text = "Week"
                            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                        },
                    )
                }
                root.addView(group.apply { layoutParams = matchWidth() })
            }
            "m3_fab" -> {
                val row = LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                }
                row.addView(
                    FloatingActionButton(context).apply {
                        setImageResource(android.R.drawable.ic_input_add)
                        contentDescription = "FAB"
                    },
                )
                row.addView(
                    ExtendedFloatingActionButton(context).apply {
                        text = "Extended"
                        setIconResource(android.R.drawable.ic_menu_edit)
                        setPadding(dp(context, 12))
                    },
                )
                root.addView(row)
            }
            "m3_chip" -> {
                val chips = ChipGroup(context).apply {
                    isSingleSelection = true
                    addView(Chip(context).apply { text = "Filter"; isCheckable = true; isChecked = true })
                    addView(Chip(context).apply { text = "Assist"; isCheckable = true })
                    addView(
                        Chip(context).apply {
                            text = "Input"
                            isCloseIconVisible = true
                            setOnCloseIconClickListener { visibility = View.GONE }
                        },
                    )
                }
                root.addView(chips)
            }
            "m3_textfield" -> {
                root.addView(
                    TextInputLayout(context).apply {
                        hint = "Outlined"
                        addView(TextInputEditText(context).apply { layoutParams = matchWidth() })
                        layoutParams = matchWidth()
                    },
                )
                root.addView(
                    TextInputLayout(
                        context,
                        null,
                        com.google.android.material.R.attr.textInputFilledStyle,
                    ).apply {
                        hint = "Filled"
                        addView(TextInputEditText(context).apply { layoutParams = matchWidth() })
                        layoutParams = matchWidth()
                    },
                )
            }
            "m3_switch" -> {
                root.addView(
                    MaterialSwitch(context).apply {
                        text = "MaterialSwitch"
                    },
                )
            }
            "m3_checkbox" -> {
                root.addView(MaterialCheckBox(context).apply { text = "MaterialCheckBox" })
            }
            "m3_radio" -> {
                val group = RadioGroup(context)
                group.addView(MaterialRadioButton(context).apply { text = "M3 A"; id = View.generateViewId() })
                group.addView(MaterialRadioButton(context).apply { text = "M3 B"; id = View.generateViewId() })
                root.addView(group)
            }
            "m3_slider" -> {
                val v = TextView(context)
                root.addView(
                    Slider(context).apply {
                        valueFrom = 0f
                        valueTo = 100f
                        value = 40f
                        addOnChangeListener { _, value, _ -> v.text = "value=$value" }
                        layoutParams = matchWidth()
                    },
                )
                root.addView(v)
                root.addView(label(context, "RangeSlider"))
                root.addView(
                    RangeSlider(context).apply {
                        valueFrom = 0f
                        valueTo = 100f
                        values = listOf(20f, 70f)
                        layoutParams = matchWidth()
                    },
                )
            }
            "m3_card" -> {
                root.addView(
                    MaterialCardView(context).apply {
                        layoutParams = matchWidth()
                        radius = dp(context, 12).toFloat()
                        cardElevation = dp(context, 2).toFloat()
                        setContentPadding(dp(context, 16), dp(context, 16), dp(context, 16), dp(context, 16))
                        addView(
                            TextView(context).apply {
                                text = "MaterialCardView\n点击/长按看涟漪（若可点击）"
                            },
                        )
                        isClickable = true
                        isFocusable = true
                    },
                )
            }
            "m3_toolbar" -> {
                root.addView(label(context, "Showcase 顶栏即 MaterialToolbar；此处再嵌一个示意："))
                root.addView(
                    com.google.android.material.appbar.MaterialToolbar(context).apply {
                        title = "Nested Toolbar"
                        subtitle = "Material 3"
                        layoutParams = matchWidth()
                    },
                )
            }
            "m3_tablayout" -> {
                root.addView(
                    TabLayout(context).apply {
                        addTab(newTab().setText("Tab 1"))
                        addTab(newTab().setText("Tab 2"))
                        addTab(newTab().setText("Tab 3"))
                        layoutParams = matchWidth()
                    },
                )
            }
            "m3_bottom_nav" -> {
                root.addView(
                    BottomNavigationView(context).apply {
                        menu.add(0, 1, 0, "Home").setIcon(android.R.drawable.ic_menu_compass)
                        menu.add(0, 2, 1, "Search").setIcon(android.R.drawable.ic_menu_search)
                        menu.add(0, 3, 2, "Profile").setIcon(android.R.drawable.ic_menu_myplaces)
                        layoutParams = matchWidth()
                    },
                )
            }
            "m3_progress" -> {
                root.addView(LinearProgressIndicator(context).apply {
                    isIndeterminate = true
                    layoutParams = matchWidth()
                })
                root.addView(
                    LinearProgressIndicator(context).apply {
                        progress = 65
                        layoutParams = matchWidth()
                    },
                )
                root.addView(CircularProgressIndicator(context).apply { isIndeterminate = true })
            }
            "m3_divider" -> {
                root.addView(TextView(context).apply { text = "上方内容" })
                root.addView(MaterialDivider(context).apply { layoutParams = matchWidth() })
                root.addView(TextView(context).apply { text = "下方内容" })
            }
            "m3_badge" -> {
                val anchor = MaterialButton(context).apply {
                    text = "带角标"
                    id = View.generateViewId()
                }
                root.addView(anchor)
                anchor.post {
                    val badge = BadgeDrawable.create(context).apply {
                        number = 8
                        backgroundColor = Color.parseColor("#C62828")
                    }
                    @SuppressLint("UnsafeOptInUsageError")
                    BadgeUtils.attachBadgeDrawable(badge, anchor)
                }
            }
            "m3_snackbar" -> {
                root.addView(
                    MaterialButton(context).apply {
                        text = "Show Snackbar"
                        setOnClickListener {
                            fragment.showSnackbar(
                                message = "Material Snackbar",
                                actionLabel = "OK",
                                action = { toast(context, "action") },
                            )
                        }
                    },
                )
            }
            "m3_dialog" -> {
                root.addView(
                    MaterialButton(context).apply {
                        text = "Show Dialog"
                        setOnClickListener {
                            MaterialAlertDialogBuilder(context)
                                .setTitle("MaterialAlertDialog")
                                .setMessage("M3 对话框示例")
                                .setPositiveButton("确定", null)
                                .setNegativeButton("取消", null)
                                .show()
                        }
                    },
                )
            }
            "m3_bottomsheet" -> {
                root.addView(
                    MaterialButton(context).apply {
                        text = "Show BottomSheet"
                        setOnClickListener {
                            val sheet = BottomSheetDialog(context)
                            sheet.setContentView(
                                TextView(context).apply {
                                    text = "Modal BottomSheet 内容区\n可下拉关闭"
                                    setPadding(dp(context, 24))
                                },
                            )
                            sheet.show()
                        }
                    },
                )
            }
            "m3_menu" -> {
                val btn = MaterialButton(context).apply { text = "PopupMenu" }
                btn.setOnClickListener {
                    PopupMenu(context, btn).apply {
                        menu.add("复制")
                        menu.add("分享")
                        menu.add("删除")
                        setOnMenuItemClickListener {
                            toast(context, it.title?.toString().orEmpty())
                            true
                        }
                        show()
                    }
                }
                root.addView(btn)
            }
            "m3_datepicker" -> {
                root.addView(
                    MaterialButton(context).apply {
                        text = "MaterialDatePicker"
                        setOnClickListener {
                            val picker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("选择日期")
                                .build()
                            picker.addOnPositiveButtonClickListener {
                                toast(context, "selection=$it")
                            }
                            picker.show(fragment.parentFragmentManager, "m3_date")
                        }
                    },
                )
            }
            "m3_timepicker" -> {
                root.addView(
                    MaterialButton(context).apply {
                        text = "MaterialTimePicker"
                        setOnClickListener {
                            val picker = MaterialTimePicker.Builder()
                                .setTimeFormat(TimeFormat.CLOCK_24H)
                                .setHour(9)
                                .setMinute(30)
                                .setTitleText("选择时间")
                                .build()
                            picker.addOnPositiveButtonClickListener {
                                toast(context, "${picker.hour}:${picker.minute}")
                            }
                            picker.show(fragment.parentFragmentManager, "m3_time")
                        }
                    },
                )
            }
            else -> {
                root.addView(
                    TextView(context).apply {
                        text = "暂无演示：$componentId"
                    },
                )
            }
        }
        return root
    }

    private fun column(context: Context) = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(context, 16))
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    private fun label(context: Context, text: String) = TextView(context).apply {
        this.text = text
        setPadding(0, 0, 0, dp(context, 8))
        setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleSmall)
    }

    private fun matchWidth() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    ).apply {
        bottomMargin = 16
    }

    private fun dp(context: Context, value: Int): Int =
        (value * context.resources.displayMetrics.density).toInt()

    private fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

package com.sys.androidkit.feature.components

enum class ComponentGroup {
    SYSTEM,
    MATERIAL3,
}

data class ComponentEntry(
    val id: String,
    val title: String,
    val className: String,
    val summary: String,
    val group: ComponentGroup,
    val tags: List<String> = emptyList(),
)

/** 系统 Widget + Material 3 组件目录（教学用一览）。 */
object ComponentCatalog {

    fun find(id: String): ComponentEntry? = all.find { it.id == id }

    fun filter(group: ComponentGroup?, query: String): List<ComponentEntry> {
        val q = query.trim().lowercase()
        return all.filter { entry ->
            (group == null || entry.group == group) &&
                (
                    q.isEmpty() ||
                        entry.title.lowercase().contains(q) ||
                        entry.className.lowercase().contains(q) ||
                        entry.summary.lowercase().contains(q) ||
                        entry.tags.any { it.lowercase().contains(q) }
                    )
        }
    }

    private val systemWidgets = listOf(
        ComponentEntry("sys_textview", "TextView", "android.widget.TextView", "文本展示", ComponentGroup.SYSTEM, listOf("Text")),
        ComponentEntry("sys_edittext", "EditText", "android.widget.EditText", "文本输入", ComponentGroup.SYSTEM, listOf("Input")),
        ComponentEntry("sys_button", "Button / ImageButton", "android.widget.Button", "按钮与图标按钮", ComponentGroup.SYSTEM, listOf("Button")),
        ComponentEntry("sys_checkbox", "CheckBox", "android.widget.CheckBox", "多选框", ComponentGroup.SYSTEM, listOf("Selection")),
        ComponentEntry("sys_radio", "RadioButton", "android.widget.RadioButton", "单选组", ComponentGroup.SYSTEM, listOf("Selection")),
        ComponentEntry("sys_switch", "Switch", "android.widget.Switch", "开关（系统控件）", ComponentGroup.SYSTEM, listOf("Toggle")),
        ComponentEntry("sys_toggle", "ToggleButton", "android.widget.ToggleButton", "切换按钮", ComponentGroup.SYSTEM, listOf("Toggle")),
        ComponentEntry("sys_seekbar", "SeekBar", "android.widget.SeekBar", "拖动进度", ComponentGroup.SYSTEM, listOf("Progress")),
        ComponentEntry("sys_progress", "ProgressBar", "android.widget.ProgressBar", "确定/不确定进度", ComponentGroup.SYSTEM, listOf("Progress")),
        ComponentEntry("sys_spinner", "Spinner", "android.widget.Spinner", "下拉选择", ComponentGroup.SYSTEM, listOf("Selection")),
        ComponentEntry("sys_rating", "RatingBar", "android.widget.RatingBar", "星级评分", ComponentGroup.SYSTEM, listOf("Selection")),
        ComponentEntry("sys_imageview", "ImageView", "android.widget.ImageView", "图片展示", ComponentGroup.SYSTEM, listOf("Image")),
        ComponentEntry("sys_checkedtext", "CheckedTextView", "android.widget.CheckedTextView", "可勾选文本", ComponentGroup.SYSTEM, listOf("Selection")),
        ComponentEntry("sys_chronometer", "Chronometer", "android.widget.Chronometer", "计时器", ComponentGroup.SYSTEM, listOf("Time")),
        ComponentEntry("sys_searchview", "SearchView", "android.widget.SearchView", "搜索框（AppCompat）", ComponentGroup.SYSTEM, listOf("Input")),
        ComponentEntry("sys_datepicker", "DatePicker", "android.widget.DatePicker", "日期选择", ComponentGroup.SYSTEM, listOf("Picker")),
        ComponentEntry("sys_timepicker", "TimePicker", "android.widget.TimePicker", "时间选择", ComponentGroup.SYSTEM, listOf("Picker")),
        ComponentEntry("sys_numberpicker", "NumberPicker", "android.widget.NumberPicker", "数字滚轮", ComponentGroup.SYSTEM, listOf("Picker")),
        ComponentEntry("sys_webview", "WebView", "android.webkit.WebView", "内嵌网页（简易）", ComponentGroup.SYSTEM, listOf("Web")),
        ComponentEntry("sys_scroll", "ScrollView", "android.widget.ScrollView", "纵向滚动容器", ComponentGroup.SYSTEM, listOf("Container")),
    )

    private val material3Components = listOf(
        ComponentEntry("m3_button", "MaterialButton", "com.google.android.material.button.MaterialButton", "Filled / Tonal / Outlined / Text", ComponentGroup.MATERIAL3, listOf("Button", "M3")),
        ComponentEntry("m3_toggle_group", "MaterialButtonToggleGroup", "….button.MaterialButtonToggleGroup", "互斥/多选按钮组", ComponentGroup.MATERIAL3, listOf("Button", "M3")),
        ComponentEntry("m3_fab", "FAB / ExtendedFAB", "….floatingactionbutton.*", "悬浮操作按钮", ComponentGroup.MATERIAL3, listOf("FAB", "M3")),
        ComponentEntry("m3_chip", "Chip / ChipGroup", "….chip.Chip", "筛选 / 输入 / 操作芯片", ComponentGroup.MATERIAL3, listOf("Chip", "M3")),
        ComponentEntry("m3_textfield", "TextInputLayout", "….textfield.TextInputLayout", "Outlined / Filled 输入框", ComponentGroup.MATERIAL3, listOf("Input", "M3")),
        ComponentEntry("m3_switch", "MaterialSwitch", "….materialswitch.MaterialSwitch", "M3 开关", ComponentGroup.MATERIAL3, listOf("Toggle", "M3")),
        ComponentEntry("m3_checkbox", "MaterialCheckBox", "….checkbox.MaterialCheckBox", "M3 复选框", ComponentGroup.MATERIAL3, listOf("Selection", "M3")),
        ComponentEntry("m3_radio", "MaterialRadioButton", "….radiobutton.MaterialRadioButton", "M3 单选", ComponentGroup.MATERIAL3, listOf("Selection", "M3")),
        ComponentEntry("m3_slider", "Slider / RangeSlider", "….slider.Slider", "单值 / 区间滑块", ComponentGroup.MATERIAL3, listOf("Slider", "M3")),
        ComponentEntry("m3_card", "MaterialCardView", "….card.MaterialCardView", "卡片容器", ComponentGroup.MATERIAL3, listOf("Card", "M3")),
        ComponentEntry("m3_toolbar", "MaterialToolbar", "….appbar.MaterialToolbar", "顶部应用栏", ComponentGroup.MATERIAL3, listOf("AppBar", "M3")),
        ComponentEntry("m3_tablayout", "TabLayout", "….tabs.TabLayout", "页签", ComponentGroup.MATERIAL3, listOf("Tabs", "M3")),
        ComponentEntry("m3_bottom_nav", "BottomNavigationView", "….bottomnavigation.BottomNavigationView", "底部导航", ComponentGroup.MATERIAL3, listOf("Navigation", "M3")),
        ComponentEntry("m3_progress", "ProgressIndicator", "….progressindicator.*", "线形 / 环形进度", ComponentGroup.MATERIAL3, listOf("Progress", "M3")),
        ComponentEntry("m3_divider", "MaterialDivider", "….divider.MaterialDivider", "分割线", ComponentGroup.MATERIAL3, listOf("Divider", "M3")),
        ComponentEntry("m3_badge", "BadgeDrawable", "….badge.BadgeDrawable", "角标", ComponentGroup.MATERIAL3, listOf("Badge", "M3")),
        ComponentEntry("m3_snackbar", "Snackbar", "com.google.android.material.snackbar.Snackbar", "轻量反馈条", ComponentGroup.MATERIAL3, listOf("Feedback", "M3")),
        ComponentEntry("m3_dialog", "MaterialAlertDialog", "….dialog.MaterialAlertDialogBuilder", "对话框", ComponentGroup.MATERIAL3, listOf("Dialog", "M3")),
        ComponentEntry("m3_bottomsheet", "BottomSheet", "….bottomsheet.BottomSheetDialog", "底部面板", ComponentGroup.MATERIAL3, listOf("BottomSheet", "M3")),
        ComponentEntry("m3_menu", "PopupMenu", "androidx.appcompat.widget.PopupMenu", "弹出菜单（Material 风格）", ComponentGroup.MATERIAL3, listOf("Menu", "M3")),
        ComponentEntry("m3_datepicker", "MaterialDatePicker", "….datepicker.MaterialDatePicker", "M3 日期选择", ComponentGroup.MATERIAL3, listOf("Picker", "M3")),
        ComponentEntry("m3_timepicker", "MaterialTimePicker", "….timepicker.MaterialTimePicker", "M3 时间选择", ComponentGroup.MATERIAL3, listOf("Picker", "M3")),
    )

    val all: List<ComponentEntry> = systemWidgets + material3Components
}

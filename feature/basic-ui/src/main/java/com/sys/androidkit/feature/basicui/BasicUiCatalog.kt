package com.sys.androidkit.feature.basicui

/**
 * 对照 [BasicUI Wiki](https://github.com/Peakmain/BasicUI/wiki) 的组件目录。
 * 源码落在 `:core:basicui`（包名 `com.peakmain.ui`，与官方文档一致）。
 */
enum class BasicUiGroup {
    WIDGET,
    RECYCLER,
    UTILS,
}

data class BasicUiEntry(
    val id: String,
    val title: String,
    val summary: String,
    val wiki: String,
    val group: BasicUiGroup,
)

object BasicUiCatalog {

    val all: List<BasicUiEntry> = listOf(
        BasicUiEntry(
            "dialog",
            "万能 Dialog",
            "AlertDialog.Builder：底部弹出 / 全宽 / 自定义布局",
            "Dialog的封装使用",
            BasicUiGroup.WIDGET,
        ),
        BasicUiEntry(
            "navigation_bar",
            "NavigationBar",
            "Builder 模式默认导航栏",
            "NavigationBar",
            BasicUiGroup.WIDGET,
        ),
        BasicUiEntry(
            "auto_edit",
            "AutoDeleteEditText",
            "自带清除按钮的输入框",
            "自带清除按钮的EditText",
            BasicUiGroup.WIDGET,
        ),
        BasicUiEntry(
            "shape",
            "Shape 系列",
            "Tv/Ll/Cl：圆角·描边·渐变·图文居中·按下/禁用/水波纹",
            "TextView、LinearLayout和ConstraintLayout的封装",
            BasicUiGroup.WIDGET,
        ),
        BasicUiEntry(
            "loading",
            "Loading",
            "CircleLoadingView / ShapeLoadingView",
            "Loading的封装",
            BasicUiGroup.WIDGET,
        ),
        BasicUiEntry(
            "flow_popup",
            "Flow + PopupWindow",
            "流式布局与自定义弹窗",
            "PopupWindow和流式布局的封装",
            BasicUiGroup.WIDGET,
        ),
        BasicUiEntry(
            "top_toast",
            "TopToastUtils",
            "顶部 Toast 提示",
            "TopToastUtils 顶部Toast提示组件",
            BasicUiGroup.WIDGET,
        ),
        BasicUiEntry(
            "password",
            "支付密码 + 键盘",
            "PasswordEditText + CustomerKeyboard",
            "自定义密码输入框 / 自定义键盘",
            BasicUiGroup.WIDGET,
        ),
        BasicUiEntry(
            "wheel",
            "WheelView 选择器",
            "时间 / 性别 / 单列滚轮底部弹窗",
            "选择器的封装",
            BasicUiGroup.WIDGET,
        ),
        BasicUiEntry(
            "tab",
            "ColorTrack TabLayout",
            "BaseTabLayout + ViewPager 滑动变色 / 下划线",
            "仿老版今日头条的TableLayout",
            BasicUiGroup.WIDGET,
        ),
        BasicUiEntry(
            "list_menu",
            "多条目菜单筛选",
            "ListMenuView + BaseListMenuAdapter（仿 58 同城筛选）",
            "多条目菜单筛选",
            BasicUiGroup.WIDGET,
        ),
        BasicUiEntry(
            "rv_common",
            "RecyclerView 封装",
            "单布局 / 多布局 / 头尾 / 下拉刷新 / 上拉加载",
            "RecyclerView的封装使用",
            BasicUiGroup.RECYCLER,
        ),
        BasicUiEntry(
            "rv_refresh",
            "下拉刷新 / 多状态",
            "LoadRefreshRecyclerView + 空/错/无网/Loading",
            "RecyclerView的封装使用",
            BasicUiGroup.RECYCLER,
        ),
        BasicUiEntry(
            "text_highlight",
            "文本高亮",
            "TextUtils 高亮片段",
            "文本高亮工具类的封装",
            BasicUiGroup.UTILS,
        ),
    )

    fun find(id: String): BasicUiEntry? = all.find { it.id == id }

    fun filter(group: BasicUiGroup?, query: String): List<BasicUiEntry> {
        val q = query.trim().lowercase()
        return all.filter { e ->
            (group == null || e.group == group) &&
                (
                    q.isEmpty() ||
                        e.title.lowercase().contains(q) ||
                        e.summary.lowercase().contains(q) ||
                        e.wiki.lowercase().contains(q)
                    )
        }
    }
}

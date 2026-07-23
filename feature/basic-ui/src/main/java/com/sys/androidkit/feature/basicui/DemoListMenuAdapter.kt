package com.sys.androidkit.feature.basicui

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.ui.adapter.menu.BaseListMenuAdapter
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.ViewHolder

/**
 * 对照 BasicUI [ListMenuAdapter]：多条目菜单筛选演示适配器。
 */
class DemoListMenuAdapter(
    private val context: Context,
    titles: List<String>,
    private val sortList: List<String>,
    private val brandList: MutableList<String>,
    private val cityList: MutableList<String>,
    private val priceList: List<String>,
    private val onFilterChanged: (String) -> Unit,
) : BaseListMenuAdapter(context, titles) {

    override val titleLayoutId: Int
        get() = R.layout.demo_basic_ui_menu_tab

    override fun getMenuLayoutId(position: Int): Int = when (position) {
        0, 3 -> R.layout.demo_basic_ui_menu_list
        else -> R.layout.demo_basic_ui_menu_grid
    }

    override fun openMenu(menuTabView: LinearLayout?, tabView: View) {
        if (menuTabView == null) return
        menuTabView.children.forEach { child ->
            child.findViewById<TextView>(com.peakmain.ui.R.id.tv_menu_tab_title)
                ?.setTextColor(ContextCompat.getColor(context, com.peakmain.ui.R.color.ui_color_272A2B))
            child.findViewById<ImageView>(R.id.iv_down)
                ?.setImageResource(R.drawable.demo_ic_triangle_down)
        }
        tabView.findViewById<TextView>(com.peakmain.ui.R.id.tv_menu_tab_title)
            ?.setTextColor(Color.parseColor("#6CBD9B"))
        tabView.findViewById<ImageView>(R.id.iv_down)
            ?.setImageResource(R.drawable.demo_ic_triangle_up)
    }

    override fun closeMenu(
        menuTabView: LinearLayout,
        tabView: View,
        position: Int,
        isSwitch: Boolean,
    ) {
        tabView.findViewById<TextView>(com.peakmain.ui.R.id.tv_menu_tab_title)
            ?.setTextColor(ContextCompat.getColor(context, com.peakmain.ui.R.color.ui_color_272A2B))
        tabView.findViewById<ImageView>(R.id.iv_down)
            ?.setImageResource(R.drawable.demo_ic_triangle_down)
    }

    override fun setMenuContent(menuView: View?, position: Int) {
        if (menuView == null) return
        when (position) {
            0 -> bindSortMenu(menuView)
            1 -> bindGridMenu(menuView, "品牌偏好", brandList, multiSelect = true) {
                onFilterChanged("品牌：$it")
                closeMenu()
            }
            2 -> bindGridMenu(menuView, "热门住宿地", cityList, multiSelect = false) {
                onFilterChanged("地点：$it")
                closeMenu()
            }
            else -> bindPriceMenu(menuView)
        }
    }

    private fun bindSortMenu(menuView: View) {
        val recyclerView = menuView.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = SortAdapter(context, sortList.toMutableList()) { item ->
            onFilterChanged("排序：$item")
            closeMenu()
        }
    }

    private fun bindPriceMenu(menuView: View) {
        val recyclerView = menuView.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = SortAdapter(context, priceList.toMutableList()) { item ->
            onFilterChanged("价格：$item")
            closeMenu()
        }
    }

    private fun bindGridMenu(
        menuView: View,
        title: String,
        data: MutableList<String>,
        multiSelect: Boolean,
        onConfirm: (String) -> Unit,
    ) {
        menuView.findViewById<TextView>(R.id.tv_brand_title).text = title
        val recyclerView = menuView.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        val adapter = ChipAdapter(context, data, multiSelect)
        recyclerView.adapter = adapter
        menuView.findViewById<View>(R.id.btnClear).setOnClickListener {
            adapter.clearSelection()
            Toast.makeText(context, "已清空", Toast.LENGTH_SHORT).show()
        }
        menuView.findViewById<View>(R.id.btnConfirm).setOnClickListener {
            val selected = adapter.selectedLabels()
            if (selected.isEmpty()) {
                Toast.makeText(context, "请先选择", Toast.LENGTH_SHORT).show()
            } else {
                onConfirm(selected.joinToString("、"))
            }
        }
    }

    private class SortAdapter(
        context: Context,
        data: MutableList<String>,
        private val onPick: (String) -> Unit,
    ) : CommonRecyclerAdapter<String>(context, data, R.layout.demo_basic_ui_menu_item_sort) {
        private var selectPosition = 0
        private var oldSelectPosition = 0

        override fun convert(holder: ViewHolder, item: String) {
            val tv = holder.getView<TextView>(R.id.tvSort)
            val check = holder.getView<TextView>(R.id.tvCheck)
            tv?.text = item
            val selected = holder.absoluteAdapterPosition == selectPosition
            tv?.setTextColor(
                Color.parseColor(if (selected) "#01A8E3" else "#272A2B"),
            )
            check?.visibility = if (selected) View.VISIBLE else View.GONE
            holder.setOnItemClickListener {
                oldSelectPosition = selectPosition
                selectPosition = holder.absoluteAdapterPosition
                notifyItemChanged(oldSelectPosition)
                notifyItemChanged(selectPosition)
                onPick(item)
            }
        }
    }

    private class ChipAdapter(
        context: Context,
        data: MutableList<String>,
        private val multiSelect: Boolean,
    ) : CommonRecyclerAdapter<String>(context, data, R.layout.demo_basic_ui_menu_item_chip) {
        private val selected = linkedSetOf<Int>()

        override fun convert(holder: ViewHolder, item: String) {
            val tv = holder.getView<TextView>(R.id.tvChip) ?: return
            tv.text = item
            val pos = holder.absoluteAdapterPosition
            val isSelected = selected.contains(pos)
            tv.setBackgroundColor(Color.parseColor(if (isSelected) "#E0F7FA" else "#F5F5F5"))
            tv.setTextColor(Color.parseColor(if (isSelected) "#01A8E3" else "#272A2B"))
            holder.setOnItemClickListener {
                if (multiSelect) {
                    if (!selected.add(pos)) selected.remove(pos)
                } else {
                    val previous = selected.toList()
                    selected.clear()
                    selected.add(pos)
                    previous.forEach { notifyItemChanged(it) }
                }
                notifyItemChanged(pos)
            }
        }

        fun clearSelection() {
            val previous = selected.toList()
            selected.clear()
            previous.forEach { notifyItemChanged(it) }
        }

        fun selectedLabels(): List<String> =
            selected.mapNotNull { pos -> getItem(pos) }
    }
}

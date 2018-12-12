package com.model.cjx.base.fragment

import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.model.cjx.MyApplication
import com.model.cjx.R
import java.util.*

/**
 * Created by cjx on 2018/5/14.
 * 树状选择列表
 */
abstract class BaseTreeSelectFragment<T> : BaseRecyclerFragment<T>(), TabLayout.OnTabSelectedListener {

    lateinit var tabLayout: TabLayout
    private var topMargin = 0

    private val trees = ArrayList<ArrayList<T>>()

    /**
     * 重写主界面ui
     */
    override fun createParentView(inflater: LayoutInflater): ViewGroup {
        val viewGroup = super.createParentView(inflater)

        val toolbarHeight = resources.getDimensionPixelOffset(R.dimen.toolbar_height)
        val tabLayoutHeight = resources.getDimensionPixelOffset(R.dimen.button_height)
        topMargin = toolbarHeight + tabLayoutHeight

        tabLayout = TabLayout(context)
        tabLayout.addOnTabSelectedListener(this)
        tabLayout.setSelectedTabIndicatorColor(MyApplication.instance.getColorPrimaryDark())
        tabLayout.setTabTextColors(ContextCompat.getColor(context!!, R.color.cjx_text_main_color), MyApplication.instance.getColorPrimaryDark())

        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, tabLayoutHeight)
        lp.topMargin = toolbarHeight
        viewGroup.addView(tabLayout, lp)

        return viewGroup
    }

    /**
     * 重写全屏布局
     */
    override fun getFullLayoutParams(): FrameLayout.LayoutParams {
        val lp = super.getFullLayoutParams()
        lp.topMargin = topMargin
        return lp
    }

    /**
     * 重写中心布局
     */
    override fun getCenterLayoutParams(width: Int, height: Int): FrameLayout.LayoutParams {
        val lp = super.getCenterLayoutParams(width, height)
        lp.topMargin = topMargin / 2
        return lp
    }

    override fun onTabReselected(tab: TabLayout.Tab?) = Unit

    override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val position = tab?.position ?: 0
        if (trees.size > position) {
            displayList(trees[position])
        }
    }

    /**
     * 添加一个tab
     */
    fun addTab(string: String) {
        tabLayout.addTab(tabLayout.newTab().setText(string), true)
    }

    /**
     * 展示列表数据
     */
    override fun displayList(list: ArrayList<T>?, isLastPage: Boolean, checkEmpty: Boolean, emptyTitle: String?, emptyIcon: Int) {
        if(list != null){
            trees.add(list)
        }
        super.displayList(list, isLastPage, checkEmpty, emptyTitle, emptyIcon)
    }

    /**
     * 刷新树结构
     */
    fun refreshTree() {
        val currentPosition = tabLayout.selectedTabPosition
        if (tabLayout.tabCount > currentPosition + 1) {
            for (i in tabLayout.tabCount - 1 downTo currentPosition + 1) {
                tabLayout.removeTabAt(i)
                trees.removeAt(i)
            }
        }
        reloadData()
    }

    /**
     * 获取当前分支完整路径
     */
    fun getTreePath(separation: String = ">"): StringBuffer {
        val tree = StringBuffer()
        val count = tabLayout.tabCount
        for (i in 1 until count) {
            val tab = tabLayout.getTabAt(i)
            if (i > 1) {
                tree.append(separation)
            }
            tree.append(tab?.text)
        }
        return tree
    }
}
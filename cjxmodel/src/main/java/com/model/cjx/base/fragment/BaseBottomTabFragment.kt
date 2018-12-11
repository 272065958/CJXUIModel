package com.model.cjx.base.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.model.cjx.MyApplication
import com.model.cjx.R
import com.model.cjx.base.bean.TabBean

/**
 * Created by cjx on 2018/4/4.
 * 底部放tab的分类界面
 */
abstract class BaseBottomTabFragment : BaseFragment() {
    /**
     * 默认选中位置
     */
    private var autoPosition = 0
    /**
     * 上次选中位置
     */
    private var prevIndex = -1
    /**
     * 用来替换界面的容器id
     */
    private var mainViewId = 0
    /**
     * 上次展示的界面
     */
    private var prevFragment: Fragment? = null
    /**
     * 显示底部tab的容器
     */
    private lateinit var tabContentView: ViewGroup
    /**
     * 底部tab点击事件
     */
    private lateinit var tabClickListener: View.OnClickListener
    /**
     * 存储底部icon的集合
     */
    private lateinit var iconViews: Array<ImageView?>
    /**
     * 存储底部text的集合
     */
    private lateinit var titleViews: Array<TextView?>
    /**
     * 存储底部消息提示的集合
     */
    private var badgeViews: Array<TextView?>? = null
    /**
     * 底部的tab
     */
    private lateinit var tabs: Array<TabBean>
    private lateinit var fragments: Array<Fragment?>

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {
        val view = View.inflate(activity, R.layout.activity_base_main_tab, null)
        tabContentView = view.findViewById(R.id.main_tab_content)
        mainViewId = R.id.main_view
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTabContent()
    }

    /**
     * 初始化底部tab
     */
    protected open fun initTabContent() {
        tabs = getTabs()
        val size = tabs.size
        if (size == 0) {
            return
        }
        initData(size)
        for (i in 0 until size) {
            tabContentView.addView(getItemTabView(i, tabs[i]))
        }
        selectPosition(autoPosition)
    }

    /**
     * 获取一个底部tab的view
     * @param position  所在的位置
     * @param res       显示的icon
     * @param title     显示的文字
     * @return          返回一个tabview
     */
    protected open fun getItemTabView(position: Int, tab: TabBean): View {
        val v = View.inflate(activity, R.layout.item_main_tab, null)
        v.tag = position
        v.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
        val iconView = v.findViewById<ImageView>(R.id.main_tab_icon)
        iconView.setImageResource(tab.icon)
        iconViews[position] = iconView
        val titleView = v.findViewById<TextView>(R.id.main_tab_text)
        titleView.text = tab.title
        titleViews[position] = titleView
        v.setOnClickListener(tabClickListener)
        return v
    }

    /**
     * 创建一个标签view
     */
    private fun getBadgeView(): TextView {
        val textView = TextView(activity)
        val size = resources.getDimensionPixelSize(R.dimen.fab_margin)
        val lp = FrameLayout.LayoutParams(size, size)
        lp.gravity = Gravity.CENTER_HORIZONTAL
        lp.leftMargin = resources.getDimensionPixelOffset(R.dimen.bigger_margin)
        textView.layoutParams = lp
        textView.setBackgroundResource(R.drawable.notice_bg)
        textView.gravity = Gravity.CENTER
        textView.textSize = 11f
        textView.setTextColor(Color.WHITE)
        textView.visibility = View.INVISIBLE
        return textView
    }

    /**
     * 获取指定下标的提示消息数的控件
     */
    fun getBadgeView(position: Int): TextView {
        if (badgeViews == null) {
            badgeViews = arrayOfNulls(titleViews.size)
        }
        val badgeView = if (badgeViews!![position] == null) {
            val view = getBadgeView()
            badgeViews!![position] = view
            (iconViews[position]?.parent as? ViewGroup)?.addView(view)
            view
        } else {
            badgeViews!![position]
        }
        return badgeView!!
    }

    /**
     * 设置指定下标的提示消息数
     */
    fun setBadge(position: Int, count: Int) {
        val badgeView = getBadgeView(position)
        if (count < 1) {
            badgeView.visibility = View.GONE
        } else {
            badgeView.visibility = View.VISIBLE
            badgeView.text = count.toString()
        }
    }

    /**
     * 设置当前选中位置
     * @param tabPosition   选中位置
     */
    private fun selectPosition(tabPosition: Int) {
        if (prevIndex == tabPosition) {
            return
        }
        val fragment = if (fragments[tabPosition] == null) {
            getFragment(tabPosition)
        } else {
            fragments[tabPosition]!!
        }
        val fragmentTransaction = childFragmentManager.beginTransaction()
        if (prevFragment != null) {
            fragmentTransaction.hide(prevFragment!!)
        }
        if (tabs[tabPosition].attach) {
            fragmentTransaction.show(fragment)
        } else {
            fragments[tabPosition] = fragment
            fragmentTransaction.add(mainViewId, fragment)
            tabs[tabPosition].attach = true
        }
        prevFragment = fragment
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.commit()
        setCurrentTab(tabPosition)
    }


    /**
     * 初始化数组
     */
    private fun initData(count: Int) {
        tabClickListener = View.OnClickListener { v: View ->
            selectPosition(v.tag as Int)
        }
        iconViews = arrayOfNulls(count)
        titleViews = arrayOfNulls(count)
        fragments = arrayOfNulls(count)
    }

    /**
     * 设置选中位置tab样式
     * @param currentTab    选中位置
     */
    protected open fun setCurrentTab(currentTab: Int) {
        if (prevIndex != -1) {
            iconViews[prevIndex]?.isSelected = false
            titleViews[prevIndex]?.setTextColor(ContextCompat.getColor(activity!!, R.color.cjx_text_title_color))
        }
        iconViews[currentTab]?.isSelected = true
        titleViews[currentTab]?.setTextColor(MyApplication.instance.getColorPrimaryDark())
        prevIndex = currentTab
    }

    /**
     * 获取底部tab的数组
     */
    abstract fun getTabs(): Array<TabBean>

    /**
     * 获取指定位置的view
     */
    abstract fun getFragment(position: Int): Fragment
}
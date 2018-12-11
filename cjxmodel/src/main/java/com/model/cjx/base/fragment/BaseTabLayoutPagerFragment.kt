package com.model.cjx.base.fragment

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.model.cjx.MyApplication
import com.model.cjx.R

/**
 * Created by cjx on 2018/5/2.
 * 顶部tab翻页
 */
abstract class BaseTabLayoutPagerFragment : BaseFragment(), TabLayout.OnTabSelectedListener {

    /**
     * 顶部tab控件
     */
    private lateinit var tabLayout: TabLayout
    /**
     * 翻页控件
     */
    private lateinit var viewPager: ViewPager

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {
        val view = View.inflate(context, R.layout.fragment_tablayout_pager, null)
        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)

        tabLayout.addOnTabSelectedListener(this)
        initTabTheme()

        return view
    }

    /**
     * 设置标题栏, 在view创建后调用, 比如在onActivityCreated
     */
    override fun setToolbar(title: String?, showBack: Boolean, backListener: View.OnClickListener?) {
        val viewGroup = view as? ViewGroup ?: return
        val toolView = LayoutInflater.from(context).inflate(MyApplication.instance.getToolbarLayout(), viewGroup, false)
        (view as ViewGroup).addView(toolView, 0)
        super.setToolbar(title, showBack, backListener)
    }

    // tabSelection回调
    override fun onTabReselected(tab: TabLayout.Tab?) = Unit

    override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

    override fun onTabSelected(tab: TabLayout.Tab?) = Unit

    /**
     * 重新fragment转场动画监听
     */
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation {
        return if(nextAnim < 1){
            setupView()
            super.onCreateAnimation(transit, enter, nextAnim)
        } else{
            val anim = AnimationUtils.loadAnimation(context, nextAnim)
            anim.setAnimationListener(object: Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) = Unit

                override fun onAnimationEnd(animation: Animation?) {
                    setupView()
                }

                override fun onAnimationStart(animation: Animation?) = Unit
            })
            anim
        }
    }

    /**
     * 设置tab的主题色
     */
    protected open fun initTabTheme() {
        val color = MyApplication.instance.getColorPrimaryDark()
        tabLayout.setSelectedTabIndicatorColor(color)
        tabLayout.setTabTextColors(ContextCompat.getColor(context!!, R.color.cjx_text_main_color),
                color)
    }

    /**
     * 绑定翻页控件
     */
    protected open fun setupView() {
        viewPager.adapter = ViewsAdapter(childFragmentManager, getTitles())
        tabLayout.setupWithViewPager(viewPager)
    }

    /**
     * 获取tab的文字数组
     */
    abstract fun getTitles(): Array<String>

    /**
     * 获取对应的fragment
     */
    abstract fun getFragment(position: Int): Fragment

    /**
     * 翻到下标page的页码
     */
    fun scrollTo(page: Int) {
        viewPager.setCurrentItem(page, false)
    }

    /**
     * pager的适配器
     */
    inner class ViewsAdapter(fm: FragmentManager, private val titles: Array<String>) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment = getFragment(position)

        override fun getCount(): Int = titles.size

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }
    }
}